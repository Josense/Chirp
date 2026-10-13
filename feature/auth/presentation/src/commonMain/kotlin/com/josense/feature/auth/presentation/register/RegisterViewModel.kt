package com.josense.feature.auth.presentation.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_account_exists
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import com.josense.core.domain.DataError
import com.josense.core.domain.auth.AuthService
import com.josense.core.domain.onFailure
import com.josense.core.domain.onSuccess
import com.josense.core.domain.validation.PasswordValidator
import com.josense.core.presentation.util.UiText
import com.josense.core.presentation.util.toUiText
import com.josense.feature.auth.domain.validation.EmailValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService,
): ViewModel() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidateState()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    private fun observeValidateState() {
        combine(
            snapshotFlow { state.value.emailTextState.text.toString() },
            snapshotFlow { state.value.usernameTextState.text.toString() },
            snapshotFlow { state.value.passwordTextState.text.toString() },
            snapshotFlow { state.value.isRegistering }
        ) { email, username, password, isRegistering ->
            _state.update {
                it.copy(
                    canRegister = email.isNotEmpty() && username.isNotEmpty()
                            && password.isNotEmpty() && isRegistering.not()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnLoginClick -> Unit
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                _state.update { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
            }
            else -> Unit
        }
    }

    private fun register() {
        if (validateFormInputs().not()) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isRegistering = true) }
            val email = state.value.emailTextState.text.toString()
            val username = state.value.usernameTextState.text.toString()
            val password = state.value.passwordTextState.text.toString()
            authService.register(email, username, password)
                .onSuccess {
                    _state.update { it.copy(isRegistering = false) }
                    eventChannel.send(RegisterEvent.Success(email))
                }.onFailure { error ->
                    val registerError = when (error) {
                        DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_account_exists)
                        else -> error.toUiText()
                    }
                    _state.update { it.copy(isRegistering = false, registrationError = registerError) }
                }
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