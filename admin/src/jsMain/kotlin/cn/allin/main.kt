package cn.allin

import react.create
import react.dom.client.createRoot
import react.router.RouterProvider
import react.router.createMemoryRouter
import react.router.dom.createBrowserRouter
import web.dom.document


fun main() {

    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(container)
        .render(RouterProvider.create {
            router = createMemoryRouter(
                routes = appRoutes
            )
        })

}