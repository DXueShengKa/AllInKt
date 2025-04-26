package cn.allin.ui

import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import react.dom.html.ReactHTML.form
import web.cssom.px

private val TagAddFC = FC {
    Stack {
        sx {
            width = 600.px
        }
        spacing = responsive(2)
        direction = responsive(StackDirection.column)
        component = form

    }
}


val RouteTagAdd = routes("tag/add", "添加标签", TagAddFC)
