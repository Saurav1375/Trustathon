package com.example.trustoken_starter.payment.presentation.home_screen

import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.payment.domain.model.Transaction
import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import com.example.trustoken_starter.payment.domain.model.Wallet


data class HomeState(
    val userData: UserData? = null,
    val selectedUser : UserData? = null,
    val transaction: TransactionEntity? = null,
    val wallet: Wallet? = null,
    val allUsers : List<UserData> = emptyList(),
    val allTransactions : List<TransactionEntity> = emptyList(),
    val isLoading: Boolean = false,
)