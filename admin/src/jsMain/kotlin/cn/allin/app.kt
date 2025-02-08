package cn.allin


import js.objects.jso
import react.CSSProperties
import web.cssom.Auto
import web.cssom.Color
import web.cssom.px
import web.cssom.vh


private val SiderStyle: CSSProperties = jso {
    height = 100.vh
    overflow = Auto.auto
}

private val HeaderStyle: CSSProperties = jso {
    padding = 0.px
    height = 64.px
    paddingInline = 48.px
    color = Color("#fff")
}

private val ContentStyle: CSSProperties = jso {
    minHeight = 120.px
}

