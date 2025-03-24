package com.example.trustoken_starter.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.trustoken_starter.auth.domain.model.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing authentication state.
 * This ViewModel handles sign-in, sign-up, and state reset operations.
 */
class AuthViewModel : ViewModel() {

    // Mutable state flow to hold authentication state
    private val _state = MutableStateFlow(SignInState())

    // Exposed immutable state flow for UI observation
    val state = _state.asStateFlow()

    /**
     * Updates the authentication state based on sign-in result.
     *
     * @param result The result of the sign-in operation, containing user data or an error message.
     */
    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null, // Sign-in is successful if data is not null
                signInError = result.errorMessage // Store any error message if present
            )
        }
    }

    /**
     * Updates the authentication state based on sign-up result.
     *
     * @param result The result of the sign-up operation, containing user data or an error message.
     */
    fun onSignUpResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignUpSuccessful = result.data != null, // Sign-up is successful if data is not null
                signInError = result.errorMessage // Store any error message if present
            )
        }
    }

    /**
     * Resets the authentication state to its default values.
     * This is useful after the authentication process completes or on logout.
     */
    fun resetState() {
        _state.update { SignInState() }
    }
}
