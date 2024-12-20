package com.compose.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString

actual fun String.htmlToAnnotatedString(
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
): AnnotatedString {
//TODO
//    val c = NSCoder().also {
//        val s = this as NSString
//        it.encodeDataObject(s.dataUsingEncoding(NSUTF8StringEncoding)?:return AnnotatedString(this))
//    }
//    val a = NSAttributedString.create(this)
    return buildAnnotatedString {
        append(this@htmlToAnnotatedString)
    }
}