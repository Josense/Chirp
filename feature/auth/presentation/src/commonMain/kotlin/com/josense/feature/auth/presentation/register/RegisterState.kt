package com.josense.feature.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import com.josense.core.domain.validation.PasswordValidationState
import com.josense.core.domain.validation.PasswordValidator
import com.josense.core.presentation.util.UiText
import com.josense.feature.auth.domain.validation.EmailValidator

data class RegisterState(
    val emailTextState: TextFieldState = TextFieldState(),
    val emailError: UiText? = null,

    val usernameTextState: TextFieldState = TextFieldState(),
    val usernameError: UiText? = null,

    val passwordTextState: TextFieldState = TextFieldState(),
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,

    val isRegistering: Boolean = false,
    val registrationError: UiText? = null,
    val canRegister: Boolean = false,
)