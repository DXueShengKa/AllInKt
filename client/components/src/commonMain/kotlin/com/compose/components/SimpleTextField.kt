package com.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: SolidColor = SolidColor(MaterialTheme.colorScheme.primary),
    enabled: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    placeholder: @Composable () -> Unit = {},
) {

    BasicTextField(
        value,
        onValueChange,
        modifier,
        textStyle = textStyle.let { if (it.color == Color.Unspecified) it.copy(LocalContentColor.current) else it },
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        enabled = enabled,
        maxLines = maxLines
    ) {
        it()
        AnimatedVisibility(
            !interactionSource.collectIsFocusedAsState().value && value.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            label = "SimpleTextField"
        ){
            CompositionLocalProvider(
                LocalTextStyle provides textStyle.copy(MaterialTheme.colorScheme.secondary),
                content = placeholder
            )
        }
    }
}
