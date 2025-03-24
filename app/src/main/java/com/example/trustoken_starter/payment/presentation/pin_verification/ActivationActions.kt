package com.example.trustoken_starter.payment.presentation.pin_verification

sealed interface ActivationActions {
    data object OnActivate : ActivationActions
    data class OnEnterNumber(val number: Int?, val index: Int): ActivationActions
    data class OnChangeFieldFocused(val index: Int): ActivationActions
    data object OnKeyboardBack: ActivationActions
}