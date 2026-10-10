package com.josense.feature.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.josense.core.designsystem.theme.ChirpTheme

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {

}

@Composable
@Preview
private fun Preview() {
    ChirpTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
        )
    }
}