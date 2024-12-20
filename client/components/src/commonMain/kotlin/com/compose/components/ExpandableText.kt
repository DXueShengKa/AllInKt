package com.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight


@Stable
private class ExpandableTextState(
    text: String,
    maxLine: Int,
    private val showMoreText: String,
    showMoreStyle: SpanStyle,
    private val showLessText: String,
    showLessStyle: SpanStyle
) {
    var isExpanded by mutableStateOf(false)
    private var lastCharIndex = 0
    private var clickable by mutableStateOf(false)

    val annotatedText by derivedStateOf {
        buildAnnotatedString {
            if (clickable) {
                if (isExpanded) {
                    append(text)
                    val index = pushStringAnnotation(showLessText, showLessText)
                    pushStyle(showLessStyle)
                    append(showLessText)
                    pop(index)
                } else {
                    append(
                        text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { it.isWhitespace() || it == '.' }
                    )
                    val index = pushStringAnnotation(showMoreText, showMoreText)
                    pushStyle(style = showMoreStyle)
                    append(showMoreText)
                    pop(index)
                }
            } else {
                append(text)
            }
        }
    }

    var onExpanded: (Boolean) -> Unit = {
        isExpanded = it
    }

    var onClick: (()->Unit)? = null

    private fun onTextClick (offset: Int) {
        when{
            annotatedText.getStringAnnotations(
                tag = showLessText,
                start = offset,
                end = offset + showLessText.length
            ).firstOrNull() != null -> onExpanded(false)
            annotatedText.getStringAnnotations(
                tag = showMoreText,
                start = offset,
                end = offset + showMoreText.length
            ).firstOrNull() != null -> onExpanded(true)
            else -> onClick?.invoke()
        }
    }

    internal val textClickable = Modifier.pointerInput(this){
        detectTapGestures { pos ->
            onTextClick(textLayoutResult.getOffsetForPosition(pos))
        }
    }

    private lateinit var textLayoutResult:TextLayoutResult

    internal val onTextLayout: (TextLayoutResult) -> Unit = { textLayoutResult ->
        this.textLayoutResult = textLayoutResult
        if (!isExpanded && textLayoutResult.hasVisualOverflow) {
            lastCharIndex = textLayoutResult.getLineEnd(maxLine - 1)
            clickable = true
        }
    }

}

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLine: Int = 3,
    style: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    showMoreText: String = "... 更多",
    showMoreStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.W500,
        color = MaterialTheme.colors.primaryVariant
    ),
    showLessText: String = " 更少",
    showLessStyle: SpanStyle = showMoreStyle,
    onClick: (()->Unit)? = null,
    isExpanded: Boolean? = null,
    onExpanded: ((Boolean) -> Unit)? = null
) {

    val state = remember(text,maxLine) {
        ExpandableTextState(
            text,
            maxLine,
            showMoreText,
            showMoreStyle,
            showLessText,
            showLessStyle
        ).also {
            if (onExpanded != null) it.onExpanded = onExpanded
            it.onClick = onClick
        }
    }

    if (isExpanded != null)
        state.isExpanded = isExpanded

    Text(
        modifier = modifier.animateContentSize().then(state.textClickable),
        text = state.annotatedText,
        maxLines = if (state.isExpanded) Int.MAX_VALUE else maxLine,
        onTextLayout = state.onTextLayout,
        style = style,
    )

}