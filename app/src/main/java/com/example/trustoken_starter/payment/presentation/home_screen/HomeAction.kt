package com.example.trustoken_starter.payment.presentation.home_screen

import com.example.trustoken_starter.auth.domain.model.UserData


sealed interface HomeAction {
    class OnUserClick(val user: UserData) : HomeAction
    data object OnSendClick : HomeAction
}