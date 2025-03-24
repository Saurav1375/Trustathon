package com.example.trustoken_starter.payment.presentation.home_screen

import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.payment.domain.model.TransactionEntity


sealed interface HomeAction {
    class OnUserClick(val user: UserData) : HomeAction
    class Confirm(val amount: Double, val reason: String) : HomeAction
    data object Transaction : HomeAction
    data class OnTransactionClick(val transaction: TransactionEntity) : HomeAction
    data object ConfirmPaymentRequest : HomeAction

    data object OnSendClick : HomeAction
    data object OnRequestClick : HomeAction
    data object LoadInitialData : HomeAction
}