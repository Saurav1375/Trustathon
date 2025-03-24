package com.example.trustoken_starter.payment.presentation.pin_verification

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trustoken_starter.payment.data.service.TokenService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivationViewModel(
    private val application: Application
) : ViewModel() {
    private val _state = MutableStateFlow(ActivationState())
    val state = _state.asStateFlow()

    private val _events = Channel<ActivationEvents>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ActivationActions) {
        when (action) {
            is ActivationActions.OnActivate -> {

            }

            is ActivationActions.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            is ActivationActions.OnChangeFieldFocused -> {
                _state.update {
                    it.copy(
                        focusedIndex = action.index
                    )
                }

            }

            is ActivationActions.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) {
                                null
                            } else {
                                number
                            }
                        },
                        focusedIndex = previousIndex
                    )
                }
            }

        }

    }


    private fun activateAccount(pin: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            if (TokenService.loginUser(application, pin)
            ) {
                _state.update {
                    it.copy(isLoading = false)
                }
                _events.send(ActivationEvents.Success)

            } else {
                _state.update {
                    it.copy(isLoading = false)
                }
                _events.send(ActivationEvents.Error("LOGIN FAILED"))
            }


        }

    }


    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update {
            it.copy(
                code = newCode,
                focusedIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
            )
        }

        if (newCode.none { it == null }) {
            activateAccount(newCode.joinToString(""))
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }

        if (currentFocusedIndex == 5) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}