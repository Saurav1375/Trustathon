package com.example.trustoken_starter.payment.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.domain.Wallet
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userWalletService: UserWalletService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    private val _allUsers = MutableStateFlow<List<UserData>>(emptyList())
    private val _userData = MutableStateFlow<UserData?>(null)
    private val _walletData = MutableStateFlow<Wallet?>(null)
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

    init {
        if (!hasLoadedInitialData) {
            fetchUserData()
            fetchWalletData()
            hasLoadedInitialData = true
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSendClick -> {
                fetchUsers()
            }

            is HomeAction.OnUserClick -> {
                _state.update {
                    it.copy(
                        selectedUser = action.user
                    )
                }
            }
        }
    }


    private fun fetchUserData() {
        viewModelScope.launch {
            _userData.value = userWalletService.getCurrentUser()
        }
    }

    private fun fetchWalletData() {
        viewModelScope.launch {
            _walletData.value = userWalletService.getCurrentUserWallet()
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

}