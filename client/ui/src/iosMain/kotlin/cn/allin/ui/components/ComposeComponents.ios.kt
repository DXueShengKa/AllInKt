package cn.allin.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.uikit.LocalKeyboardOverlapHeight
import androidx.compose.ui.unit.dp

@Composable
actual fun PreviewMediaDialog(path: Any, isVideo: Boolean, onDismissRequest: () -> Unit) {

}

@Composable
actual fun onBackPressed(): () -> Unit {
    return {}
}

@OptIn(InternalComposeUiApi::class)
actual val WindowInsetsIsImeVisible: Boolean
    @NonRestartableComposable
    @Composable
    get() = LocalKeyboardOverlapHeight.current > 10.dp
