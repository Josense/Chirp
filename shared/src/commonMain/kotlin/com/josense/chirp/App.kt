package com.josense.chirp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.josense.core.designsystem.theme.ChirpTheme
import com.josense.feature.auth.presentation.register.RegisterRoot

@Composable
@Preview
fun App() {
    ChirpTheme {
        RegisterRoot(onRegisterSuccess = {})
    }
}