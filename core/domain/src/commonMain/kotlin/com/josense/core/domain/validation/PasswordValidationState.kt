package com.josense.core.domain.validation

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasDigit: Boolean = false,
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasUppercase && hasDigit
}
