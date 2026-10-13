package com.josense.chirp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.josense.chirp.di.initKoin
import com.josense.chirp.navigation.DeepLinkListener
import com.josense.chirp.navigation.ExternalUriHandler
import org.koin.logger.SLF4JLogger
import java.awt.Desktop

fun main() = application {
    initKoin {
        logger(SLF4JLogger())
    }

    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_OPEN_URI)) {
            Desktop.getDesktop().setOpenURIHandler { event ->
                val uri = event.uri.toString()
                ExternalUriHandler.onNewUri(uri)
            }
        }
    } catch (e: Exception) {
        println("当前系统不支持 AWT OpenURIHandler: ${e.message}")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Chirp",
    ) {
        App()
    }
}