package com.josense.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.josense.core.designsystem.theme.ChirpTheme
import com.josense.core.designsystem.theme.extended

enum class ChirpButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT,
    ;

    @Composable
    fun toButtonColors(): ButtonColors {
        return when (this) {
            PRIMARY -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
                disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
            )

            DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
                disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
            )

            SECONDARY -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
            )

            DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.error,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
            )

            TEXT -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
            )
        }
    }

    @Composable
    fun toButtonBorder(enabled: Boolean): BorderStroke? {
        val defaultBorderStroke = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extended.disabledOutline
        )
        return when {
            this == PRIMARY && !enabled -> defaultBorderStroke
            this == SECONDARY -> defaultBorderStroke
            this == DESTRUCTIVE_PRIMARY && !enabled -> defaultBorderStroke
            this == DESTRUCTIVE_SECONDARY -> {
                val borderColor = if(enabled) {
                    MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
                } else {
                    MaterialTheme.colorScheme.extended.disabledOutline
                }
                BorderStroke(
                    width = 1.dp,
                    color = borderColor
                )
            }
            else -> null
        }
    }
}

@Composable
fun ChirpButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChirpButtonStyle = ChirpButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = style.toButtonColors(),
        border = style.toButtonBorder(enabled),
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(15.dp)
                    .alpha(if (isLoading) 1F else 0F),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(if (isLoading) 0F else 1F)
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
@Preview
fun ChirpButtonPreview() {
    ChirpTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.PRIMARY,
                enabled = true,
                isLoading = false,
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY,
                enabled = true,
                isLoading = false,
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.SECONDARY,
                enabled = true,
                isLoading = false,
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.DESTRUCTIVE_SECONDARY,
                enabled = true,
                isLoading = false,
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.TEXT,
                enabled = true,
                isLoading = false,
            )

            ChirpButton(
                text = "Hello world",
                onClick = {},
                style = ChirpButtonStyle.TEXT,
                enabled = false,
                isLoading = true,
            )
        }
    }
}
