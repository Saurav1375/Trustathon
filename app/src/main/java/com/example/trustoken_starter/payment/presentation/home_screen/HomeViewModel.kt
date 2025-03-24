package com.example.trustoken_starter.payment.presentation.home_screen

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.data.service.TokenService
import com.example.trustoken_starter.payment.domain.model.Transaction
import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import com.example.trustoken_starter.payment.domain.model.Wallet
import com.example.trustoken_starter.payment.domain.service.TransactionService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class HomeViewModel(
    private val userWalletService: UserWalletService,
    private val transactionService: TransactionService,
    private val application: Application
) : ViewModel() {


    private val _state = MutableStateFlow(HomeState())
    private val _allUsers = MutableStateFlow<List<UserData>>(emptyList())
    private val _userData = MutableStateFlow<UserData?>(null)
    private val _walletData = MutableStateFlow<Wallet?>(null)
    private val _fromWallet = MutableStateFlow<Wallet?>(null)
    private val _toWallet = MutableStateFlow<Wallet?>(null)
    val state = combine(
        _state,
        _userData,
        _walletData,
        _allUsers
    ) { state, userData, wallet, allUsers ->
        state.copy(
            userData = userData,
            wallet = wallet,
            allUsers = allUsers
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState()
    )


    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val amount = mutableDoubleStateOf(0.0)
    private val reason = mutableStateOf("")

    init {
        fetchUserData()
        fetchWalletData()
        fetchAllTransactions()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.LoadInitialData -> {
                fetchWalletData()
                fetchUserData()
                fetchAllTransactions()
            }

            is HomeAction.Transaction -> {


                if (amount.doubleValue > (_walletData.value?.balance!!)) {
                    viewModelScope.launch {
                        _events.send(HomeEvent.Error("Insufficient balance"))
                    }
                    return
                }
                initiateTransaction()
                _state.value.transaction?.let { findTransaction(it.id) }
                TokenService.logoutUSB()
            }

            is HomeAction.OnSendClick -> {
                fetchUsers()
            }

            is HomeAction.OnTransactionClick -> {
                _state.update {
                    it.copy(
                        transaction = action.transaction
                    )
                }
            }

            is HomeAction.OnRequestClick -> {
                fetchAllTransactions()
            }

            is HomeAction.OnUserClick -> {
                _state.update {
                    it.copy(
                        selectedUser = action.user
                    )
                }
            }

            is HomeAction.ConfirmPaymentRequest -> {
                if (TokenService.detectUSB(application)) {
                    viewModelScope.launch {
                        _events.send(HomeEvent.NavigateToPayment(true))
                    }
                } else {
                    viewModelScope.launch {
                        _events.send(HomeEvent.Error("FAILED TO DETECT TOKEN PLEASE TRY AGAIN OR ATTACH USB TOKEN"))

                    }
                }
            }

            is HomeAction.Confirm -> {
                //update amount and reason in transaction in the state
                amount.doubleValue = action.amount
                reason.value = action.reason
                if (TokenService.detectUSB(application)) {
                    viewModelScope.launch {
                        _events.send(HomeEvent.NavigateToPayment(false))
                    }
                } else {
                    viewModelScope.launch {
                        _events.send(HomeEvent.Error("FAILED TO DETECT TOKEN PLEASE TRY AGAIN OR ATTACH USB TOKEN"))

                    }
                }

            }
        }
    }


    private fun fetchUserData() {
        viewModelScope.launch {
            userWalletService.getCurrentUser().collect { userData ->
                _userData.update {
                    userData
                }
            }
        }
    }

    private fun fetchWalletData() {
        viewModelScope.launch {
            userWalletService.getCurrentUserWallet().collect { wallet ->
                _walletData.update {
                    wallet
                }
            }
        }
    }


    private fun fetchUsers() {
        viewModelScope.launch {
            userWalletService.getAllUsers()
                .catch { error ->
                }
                .collect { users ->
                    _allUsers.update {
                        users
                    }
                }
        }
    }

    @SuppressLint("NewApi")
    private fun initiateTransaction() {
        val currentTransaction = _state.value.transaction
        val currentUserWallet = state.value.userData?.walletAddress

        if (currentTransaction != null && currentTransaction.toAddress == currentUserWallet) {
            viewModelScope.launch {
                if (!TokenService.verifyToken(
                        currentTransaction.signature ?: "",
                        currentTransaction.transactionString ?: ""
                    )
                ) {
                    _events.send(HomeEvent.Error("SIGNATURE VERIFICATION FAILED"))
                    _state.update {
                        it.copy(
                            transaction = it.transaction?.copy(status = "FAILED")
                        )
                    }
                    if (transactionService.saveTransaction(_state.value.transaction!!)) {
                        _events.send(HomeEvent.PaymentSuccessful)
                    } else {
                        _events.send(HomeEvent.PaymentError("PAYMENT/REQUEST FAILED"))
                    }
                    return@launch
                }

                _state.update {
                    it.copy(transaction = it.transaction?.copy(status = "CONFIRMED"))
                }

                if (transactionService.saveTransaction(_state.value.transaction!!)) {
                    _events.send(HomeEvent.PaymentSuccessful)
                } else {
                    _events.send(HomeEvent.PaymentError("PAYMENT/REQUEST FAILED"))
                }

                // **Fetch wallets before updating balances**
                val fromWallet = userWalletService.getUserWalletByAddress(
                    _state.value.transaction?.fromAddress ?: ""
                ).firstOrNull()

                val toWallet = userWalletService.getUserWalletByAddress(
                    _state.value.transaction?.toAddress ?: ""
                ).firstOrNull()

                if (fromWallet == null || toWallet == null) {
                    _events.send(HomeEvent.Error("Wallets not found"))
                    return@launch
                }

                val transactionAmount = _state.value.transaction?.amount ?: 0.0

                // **Update balances safely**
                val updatedFromWallet = fromWallet.copy(balance = fromWallet.balance - transactionAmount)
                val updatedToWallet = toWallet.copy(balance = toWallet.balance + transactionAmount)

                // **Save updated wallets and verify**
                val fromSaveResult = userWalletService.saveWallet(updatedFromWallet)
                val toSaveResult = userWalletService.saveWallet(updatedToWallet)

                if (fromSaveResult && toSaveResult) {
                    _events.send(HomeEvent.PaymentSuccessful)
                } else {
                    _events.send(HomeEvent.PaymentError("Failed to update wallets"))
                }
            }
            return
        }

        // **Create new transaction**
        val transactionObject = Transaction(
            id = UUID.randomUUID().toString(),
            fromAddress = state.value.userData?.walletAddress ?: "",
            toAddress = state.value.selectedUser?.walletAddress ?: "",
            amount = amount.doubleValue,
            reason = reason.value,
            timestamp = LocalDateTime.now(),
            status = "PENDING",
        )

        val transactionEntity = TransactionEntity(
            id = transactionObject.id,
            fromAddress = transactionObject.fromAddress,
            toAddress = transactionObject.toAddress,
            amount = transactionObject.amount,
            reason = transactionObject.reason,
            status = transactionObject.status,
            timestamp = transactionObject.timestamp.toInstant(ZoneOffset.UTC).toEpochMilli(),
            transactionString = transactionObject.toString(),
            signature = TokenService.signTransaction(application, transactionObject.toString())
        )

        _state.update {
            it.copy(transaction = transactionEntity)
        }

        // **Save transaction in database**
        viewModelScope.launch {
            if (transactionService.saveTransaction(transactionEntity)) {
                _events.send(HomeEvent.PaymentSuccessful)
            } else {
                _events.send(HomeEvent.PaymentError("PAYMENT FAILED"))
            }
        }
        fetchUserData()
        fetchWalletData()
    }


    @SuppressLint("NewApi")
    private fun findTransaction(id: String) {
        viewModelScope.launch {
            transactionService.getTransactionByIdAsFlow(id).collect { tt ->
                if (tt != null) {
                    _state.update {
                        it.copy(
                            transaction = tt
                        )
                    }
                }
            }


        }

    }

    private fun fetchAllTransactions() {
        viewModelScope.launch {
            transactionService.getAllTransactionsAsFlow()
                .catch { error ->
                    println("error")
                }
                .collect { transactions ->
                    _state.update {
                        it.copy(
                            allTransactions = transactions
                        )
                    }
                }
        }

    }

}

