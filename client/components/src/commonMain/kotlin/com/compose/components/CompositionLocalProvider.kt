package com.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.currentComposer

/**
 * @see androidx.compose.runtime.CompositionLocalProvider
 */
@Composable
@OptIn(InternalComposeApi::class)
inline fun CompositionLocalProviderInline(vararg values: ProvidedValue<*>, content: @Composable () -> Unit) {
    currentComposer.startProviders(values)
    content()
    currentComposer.endProviders()
}
