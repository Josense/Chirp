package com.josense.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import com.josense.core.domain.validation.PasswordValidator
import com.josense.core.presentation.util.UiText
import com.josense.feature.auth.domain.validation.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )


    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnLoginClick -> validateFormInputs()
            else -> Unit
        }
    }

    private fun clearAllTextFieldsErrors() {
        _state.update {
            it.copy(
                emailError = null,
                usernameError = null,
                passwordError = null,
                registrationError = null,
            )
        }
    }

    private fun validateFormInputs(): Boolean {
        clearAllTextFieldsErrors()

        val currentState = state.value

        val isEmailValid = EmailValidator.validate(currentState.emailTextState.text.toString())
        val isUsernameValid = currentState.usernameTextState.text.toString().length in 3..20
        val passwordValidationState = PasswordValidator.validate(currentState.passwordTextState.text.toString())

        _state.update {
            it.copy(
                emailError = if (isEmailValid.not()) {
                    UiText.Resource(Res.string.error_invalid_email)
                } else null,
                passwordError = if (passwordValidationState.isValidPassword.not()) {
                    UiText.Resource(Res.string.error_invalid_password)
                } else null,
                usernameError = if (isUsernameValid.not()) {
                    UiText.Resource(Res.string.error_invalid_username)
                } else null,
            )
        }
        return isUsernameValid && isEmailValid && passwordValidationState.isValidPassword
    }
}