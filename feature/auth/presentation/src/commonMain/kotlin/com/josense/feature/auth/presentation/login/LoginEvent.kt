package com.josense.feature.auth.presentation.login

sealed interface LoginEvent {
    data object Success: LoginEvent
}