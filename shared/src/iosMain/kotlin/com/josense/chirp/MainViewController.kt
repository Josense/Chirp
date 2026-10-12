package com.josense.chirp

import androidx.compose.ui.window.ComposeUIViewController
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.setUnhandledExceptionHook
import kotlin.native.terminateWithUnhandledException

fun MainViewController() = ComposeUIViewController { App() }

@OptIn(ExperimentalNativeApi::class)
fun installUnhandledExceptionLogger() {
    setUnhandledExceptionHook { throwable ->
        println("UNHANDLED KOTLIN EXCEPTION: ${throwable.stackTraceToString()}")
        terminateWithUnhandledException(throwable)
    }
}
