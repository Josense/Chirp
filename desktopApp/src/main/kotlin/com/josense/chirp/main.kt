package com.josense.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.josense.chirp.di.initKoin
import org.koin.logger.SLF4JLogger

fun main() = application {
    initKoin {
        logger(SLF4JLogger())
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Chirp",
    ) {
        App()
    }
}