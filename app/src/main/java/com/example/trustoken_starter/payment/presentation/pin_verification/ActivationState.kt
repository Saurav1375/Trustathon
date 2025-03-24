package com.example.trustoken_starter.payment.presentation.pin_verification

data class ActivationState (
    val isLoading : Boolean = false,
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
)