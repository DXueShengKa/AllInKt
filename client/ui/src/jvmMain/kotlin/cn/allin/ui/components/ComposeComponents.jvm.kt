package cn.allin.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import kotlin.system.exitProcess

@Composable
actual fun PreviewMediaDialog(
    path: Any,
    isVideo: Boolean,
    onDismissRequest: () -> Unit,
) {

}

actual val WindowInsetsIsImeVisible: Boolean
    @NonRestartableComposable
    @Composable
    get() = false


@Composable
actual fun onBackPressed(): () -> Unit {
    return {
        exitProcess(1)
    }
}
