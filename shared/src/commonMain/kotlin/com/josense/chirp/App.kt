package com.josense.chirp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.josense.chirp.navigation.DeepLinkListener
import com.josense.chirp.navigation.NavigationRoot
import com.josense.core.designsystem.theme.ChirpTheme

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    DeepLinkListener(navController)

    ChirpTheme {
        NavigationRoot(navController)
    }
}