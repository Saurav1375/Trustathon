package com.example.trustoken_starter.payment.presentation.home_screen

import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.payment.domain.Wallet


data class HomeState(
    val userData: UserData? = null,
    val selectedUser : UserData? = null,
    val wallet: Wallet? = null,
    val allUsers : List<UserData> = emptyList(),
    val isLoading: Boolean = false,
)