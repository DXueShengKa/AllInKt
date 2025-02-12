@file:JsModule("@toolpad/core/react-router")

package toolpad.core.react_router

import mui.material.styles.Theme
import react.FC
import react.PropsWithChildren
import toolpad.core.Navigation


external val ReactRouterAppProvider: FC<ReactRouterAppProviderProps>


external interface ReactRouterAppProviderProps : PropsWithChildren {
    var navigation: Navigation
    var branding: Branding
    var theme: Theme
}