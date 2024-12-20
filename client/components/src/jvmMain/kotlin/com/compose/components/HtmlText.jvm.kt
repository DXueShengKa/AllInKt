package com.compose.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode


actual fun String.htmlToAnnotatedString(
    linkStyles: TextLinkStyles?,
    linkInteractionListener: LinkInteractionListener?
): AnnotatedString {
    val body = Jsoup.parseBodyFragment(this)
        .body()

    return buildAnnotatedString {

        body.forEachNode { node ->
            if (node.nameIs("body"))
                return@forEachNode

            when (node.nodeName()) {
                "a" -> {
                    node as Element
                    val text = node.text()
                    val start = length
                    val end = length + text.length

                    addLink(LinkAnnotation.Url(node.attr("href"), linkStyles, linkInteractionListener), start, end)
                }

                "#text" -> {
                    node as TextNode
                    append(node.text())
                }
            }
        }
    }
}