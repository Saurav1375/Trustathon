package com.example.trustoken_starter.auth.presentation

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signInError : String? = null
    )


