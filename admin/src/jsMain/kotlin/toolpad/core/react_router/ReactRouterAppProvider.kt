@file:JsModule("@toolpad/core/react-router")

package toolpad.core.react_router

import mui.material.styles.Theme
import react.FC
import react.PropsWithChildren
import toolpad.core.Authentication
import toolpad.core.Navigation
import toolpad.core.Session


external val ReactRouterAppProvider: FC<ReactRouterAppProviderProps>


external interface ReactRouterAppProviderProps : PropsWithChildren {
    var navigation: Navigation
    var branding: Branding
    var theme: Theme
    var session: Session?
    var authentication: Authentication
}
