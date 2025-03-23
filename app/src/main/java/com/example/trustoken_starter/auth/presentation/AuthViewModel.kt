package com.example.trustoken_starter.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.trustoken_starter.auth.domain.model.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update {it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        )

        }
    }

    fun onSignUpResult(result : SignInResult) {
        _state.update {
            it.copy(
                isSignUpSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}

