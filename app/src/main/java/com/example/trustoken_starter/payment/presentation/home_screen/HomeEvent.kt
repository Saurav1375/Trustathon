package com.example.trustoken_starter.payment.presentation.home_screen

sealed interface HomeEvent {
    data class NavigateToPayment(val isConfirmation : Boolean) : HomeEvent
    data class Error(val error: String): HomeEvent
    class PaymentError(val error: String) : HomeEvent
    data object Success:  HomeEvent
    data object PaymentSuccessful : HomeEvent
}