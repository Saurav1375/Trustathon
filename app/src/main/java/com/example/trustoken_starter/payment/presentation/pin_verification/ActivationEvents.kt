package com.example.trustoken_starter.payment.presentation.pin_verification

 sealed interface ActivationEvents {
     data class Error(val error : String) : ActivationEvents
     data object Success : ActivationEvents

}