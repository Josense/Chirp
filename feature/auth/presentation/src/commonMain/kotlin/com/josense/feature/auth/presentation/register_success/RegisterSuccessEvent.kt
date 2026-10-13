package com.josense.feature.auth.presentation.register_success

sealed interface RegisterSuccessEvent {

    data object ResendVerificationEmailSuccess: RegisterSuccessEvent
}