package com.josense.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class DynamicString(val value: String): UiText

    class Resource(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun UiText.asString(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> stringResource(id, *args)
        }
    }


    suspend fun UiText.asStringAsync(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> getString(id, *args)
        }
    }
}

