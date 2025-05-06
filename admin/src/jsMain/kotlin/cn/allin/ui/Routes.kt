package cn.allin.ui

import js.objects.jso
import react.router.RouteObject
import toolpad.core.NavigationObj

interface Routes {
    val routeObj: RouteObject
    val navigation: NavigationObj
}

fun routes(routePath: String, routeTitle: String, component: react.ComponentType<*>): Routes = object : Routes {

    override val navigation: NavigationObj = jso {
        title = routeTitle
        segment = routePath
    }

    override val routeObj: RouteObject = jso {
        Component = component
        path = routePath
    }
}
