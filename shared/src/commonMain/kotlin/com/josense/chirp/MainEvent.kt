package com.josense.chirp

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}