//@Suppress("INVISIBLE_MEMBER","INVISIBLE_REFERENCE")可以强行调用internal修饰的代码
package com.compose.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml


actual fun String.htmlToAnnotatedString(
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
): AnnotatedString {
    return AnnotatedString.fromHtml(this,linkStyles, linkInteractionListener)
}
