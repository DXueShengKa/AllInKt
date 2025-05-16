package cn.allin.ui

import js.objects.unsafeJso
import react.router.RouteObject
import toolpad.core.NavigationObj

interface Routes {
    val routeObj: RouteObject
    val navigation: NavigationObj
}

fun routes(routePath: String, routeTitle: String, component: react.ComponentType<*>): Routes = object : Routes {

    override val navigation: NavigationObj = unsafeJso {
        title = routeTitle
        segment = routePath
    }

    override val routeObj: RouteObject = unsafeJso {
        Component = component
        path = routePath
    }
}
