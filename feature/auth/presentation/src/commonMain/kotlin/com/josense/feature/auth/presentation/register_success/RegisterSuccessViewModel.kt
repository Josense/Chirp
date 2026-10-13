package com.josense.feature.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josense.core.domain.auth.AuthService
import com.josense.core.domain.onFailure
import com.josense.core.domain.onSuccess
import com.josense.core.presentation.util.UiText
import com.josense.core.presentation.util.toUiText
import com.josense.feature.auth.presentation.register.RegisterState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterSuccessViewModel(
    private val authService: AuthService,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<RegisterSuccessEvent>()
    val events = eventChannel.receiveAsFlow()

    private val email = savedStateHandle.get<String>("email")
        ?: throw IllegalStateException("No email passed to register success screen")

    private val _state = MutableStateFlow(RegisterSuccessState(email = UiText.DynamicString(email)))
    val state: StateFlow<RegisterSuccessState> = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterSuccessState(email = UiText.DynamicString(email))
        )


    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            RegisterSuccessAction.OnResendVerificationEmailClick -> resendVerificationEmail()
            else -> Unit
        }
    }

    private fun resendVerificationEmail() {
        if (_state.value.isResendingVerificationEmail) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(
                isResendingVerificationEmail = true
            ) }

            authService
                .resendVerificationEmail(email)
                .onSuccess {
                    _state.update { it.copy(
                        isResendingVerificationEmail = false
                    ) }
                    eventChannel.send(RegisterSuccessEvent.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        isResendingVerificationEmail = false,
                        resendVerificationEmailError = error.toUiText()
                    ) }
                }
        }
    }
}