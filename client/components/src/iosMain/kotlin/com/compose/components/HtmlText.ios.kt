package com.compose.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSAttributedStringEnumerationOptions
import platform.Foundation.NSRange
import platform.Foundation.NSUnicodeStringEncoding
import platform.UIKit.create
import platform.UIKit.NSDocumentTypeDocumentAttribute
import platform.UIKit.NSHTMLTextDocumentType
import platform.UIKit.NSLinkAttributeName
import platform.Foundation.NSAttributedString
import platform.Foundation.enumerateAttributesInRange
import platform.Foundation.attributedSubstringFromRange
import platform.Foundation.NSString
import platform.Foundation.dataUsingEncoding
import platform.Foundation.NSURL
import platform.Foundation.NSMakeRange
import platform.Foundation.length


actual fun String.htmlToAnnotatedString(
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
): AnnotatedString {
    val attr = toAttributedString()
    return attrToAnnotated(attr, linkStyles, linkInteractionListener) ?:
    buildAnnotatedString { append(this@htmlToAnnotatedString)}
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun String.toAttributedString(): NSAttributedString? {
    try {
        return NSAttributedString.create(
            data = (this as NSString).dataUsingEncoding(NSUnicodeStringEncoding) ?: return null,
            options = mapOf<Any?,Any?>(NSDocumentTypeDocumentAttribute to NSHTMLTextDocumentType),
            documentAttributes = null,
            error = null
        )
    } catch (e:Throwable){
        return null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun attrToAnnotated(
    attr: NSAttributedString?,
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
): AnnotatedString? {
    attr ?: return null

    val enumerationRange: CValue<NSRange> = NSMakeRange(loc = 0UL, len = attr.length)
    val options: NSAttributedStringEnumerationOptions = 0UL
    val builder = AnnotatedString.Builder()

    attr.enumerateAttributesInRange(
        enumerationRange = enumerationRange,
        options = options
    ) { attributes: Map<Any?, *>?, range: CValue<NSRange>, bool: CPointer<BooleanVar>? ->
        val plainString = attr.attributedSubstringFromRange(range = range).string

        val start = range.useContents {
            location.toInt()
        }
        val end = range.useContents {
            (location + length).toInt()
        }

        attributes?.forEach { (key, value) ->
            when (key) {
                NSLinkAttributeName -> {
                    val url: String? = if (value is NSURL) {
                        value.path
                    } else {
                        value?.toString()
                    }
                    if (url != null) builder.addLink(
                        LinkAnnotation.Url(url.substring(1), linkStyles, linkInteractionListener),
                        start, end
                    )
                }

                else -> {
                }
            }
        }

        builder.append(plainString)
    }

    return builder.toAnnotatedString()
}