package com.compose.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    urlStyle: TextLinkStyles = TextLinkStyles(
        SpanStyle(
            color = MaterialTheme.colorScheme.primaryContainer,
            textDecoration = TextDecoration.Underline
        )
    ),
    color: Color = LocalContentColor.current.copy(alpha = 0.5f),
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
