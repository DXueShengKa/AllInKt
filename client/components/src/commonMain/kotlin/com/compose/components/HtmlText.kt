package com.compose.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    urlStyle: TextLinkStyles = TextLinkStyles(
        SpanStyle(
            color = MaterialTheme.colors.primaryVariant,
            textDecoration = TextDecoration.Underline
        )
    ),
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    inlineContent: Map<String, InlineTextContent> = mapOf()
){
    val uriHandler = LocalUriHandler.current

    val annotatedString = produceState(AnnotatedString(""), text){
        value = text.htmlToAnnotatedString(urlStyle) {
            if (it is LinkAnnotation.Url){
                uriHandler.openUri(it.url)
            }
        }
    }

    BasicText(
        modifier = modifier,
        text = annotatedString.value,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = {
            onTextLayout?.invoke(it)
        },
        style = style.copy(
            color = style.color.takeOrElse { color }
        ),
        inlineContent = inlineContent
    )
}

expect fun String.htmlToAnnotatedString(
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
):AnnotatedString