package com.josense.feature.auth.presentation.register_success

import com.josense.core.presentation.util.UiText

data class RegisterSuccessState(
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationEmailError: UiText? = null,
    val email: UiText,
)