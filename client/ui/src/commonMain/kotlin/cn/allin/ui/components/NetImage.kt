package cn.allin.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import cn.allin.BuildConfig
import coil3.ComponentRegistry
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.disk.DiskCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import coil3.util.Logger
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


@Immutable
class ImageUrl @JvmOverloads constructor(
    val data: Any? = null,
    private val imageRequestBuilder: (ImageRequest.Builder.() -> Unit)? = null
) {
    private var _imageRequest: ImageRequest? = null

    //只要文件名做缓存key
    val key: String = if (data is String) data.substringBefore('?').substringAfterLast('/') else data.toString()

    fun imageRequest(context: PlatformContext): ImageRequest? = when {
        data is ImageRequest? -> data

        _imageRequest != null -> _imageRequest

        data is String -> {

            ImageRequest.Builder(context)
                .data(data)
                .diskCacheKey(key)
                .memoryCacheKey(key)
                .extendBuild()
        }

        else -> ImageRequest.Builder(context)
            .data(data)
            .extendBuild()
    }

    private fun ImageRequest.Builder.extendBuild(): ImageRequest {
        imageRequestBuilder?.invoke(this)
        val req = build()
        _imageRequest = req
        return req
    }

    companion object {

        @JvmStatic
        val ImageUrlDefault = ImageUrl()

        @JvmStatic
        fun initNetImage(
            path: String,
            componentBuilder: ComponentRegistry.Builder.() -> Unit,
        ) {
            SingletonImageLoader.setSafe {
                ImageLoader.Builder(it)
                    .crossfade(true)
                    .logger(if (BuildConfig.TEST) DebugLogger(Logger.Level.Debug) else null)
                    .diskCache(DiskCache.Builder().directory(path.toPath()).build())
                    .components(componentBuilder)
                    .build()
            }

        }
    }
}


fun String?.asImageUrl() = ImageUrl(this)

fun String?.asImageUrl(imageRequestBuilder: ImageRequest.Builder.() -> Unit) = ImageUrl(this, imageRequestBuilder)


val AsyncImagePlaceholder: Painter = ColorPainter(Color.LightGray)

/**
 * [AsyncImage]
 */
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@Composable
fun NetImage(
    imageUrl: ImageUrl,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    fallback: Painter? = placeholder,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    if (imageUrl.data is DrawableResource) {
        Image(painterResource(imageUrl.data), contentDescription, modifier, alignment, contentScale, alpha, colorFilter)
    } else {
        val context = LocalPlatformContext.current
        AsyncImage(
            imageUrl.imageRequest(context),
            contentDescription,
            SingletonImageLoader.get(context),
            modifier,
            coil3.compose.internal.transformOf(placeholder, error, fallback),
            onState,
            alignment,
            contentScale,
            alpha,
            colorFilter,
            filterQuality
        )
    }
}

