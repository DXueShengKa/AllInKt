package cn.allin.ui.components

import android.net.Uri
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.Ref
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
actual fun PreviewMediaDialog(
    path: Any,
    isVideo: Boolean,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest
    ) {
        if (isVideo) VideoDialog(path, onDismissRequest)
        else AsyncImage(
            path, null,
            Modifier.clickable(
                remember { MutableInteractionSource() }, null,
                onClick = onDismissRequest
            ),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun VideoDialog(path: Any, onCancel: () -> Unit) {
    Box(
        Modifier.clickable(
            remember { MutableInteractionSource() }, null,
            onClick = onCancel
        ),
        contentAlignment = Alignment.Center
    ) {
        Column {

            val controller = remember { Ref<MediaController.MediaPlayerControl>() }
            AndroidView(
                factory = {
                    VideoView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                        )
                        if (path is Uri) {
                            setVideoURI(path)
                        } else {
                            setVideoPath(path.toString())
                        }
                        start()
                    }
                },
                Modifier.clickable {
                    controller.value?.also {
                        if (it.isPlaying) it.pause() else it.start()
                    }
                }
            ) {
                controller.value = it
            }

            var v by remember { mutableFloatStateOf(0f) }

            Slider(
                value = v,
                onValueChange = {
                    controller.value?.apply {
                        if (isPlaying) pause()
                    }
                    v = it
                },
                onValueChangeFinished = {
                    controller.value?.apply {
                        seekTo((duration * v).toInt())
                        start()
                    }
                }
            )

            LaunchedEffect(Unit) {
                val c = controller.value ?: return@LaunchedEffect
                while (isActive) {
                    if (c.isPlaying)
                        v = c.currentPosition.toFloat() / c.duration

                    delay(1000)
                }
            }

        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
actual val WindowInsetsIsImeVisible: Boolean
    @Composable
    @NonRestartableComposable
    get() = WindowInsets.isImeVisible


@Composable
actual fun onBackPressed(): () -> Unit {
    val current = LocalOnBackPressedDispatcherOwner.current
    return { current?.onBackPressedDispatcher?.onBackPressed() }
}
