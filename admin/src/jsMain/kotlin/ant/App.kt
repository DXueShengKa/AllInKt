@file:JsModule("antd")

package ant

import react.Context
import react.FC
import react.PropsWithChildren


external val App: AppFC

external interface AppProps : AntChildrenProps {
    @JsName("message")
    var messageConfig: AntMessageConfigOptions

}


external interface AppFC : FC<PropsWithChildren> {
    fun useApp(): UseAppProps
}


external val AppContext: Context<UseAppProps>

@JsName("useAppProps")
external interface UseAppProps {
    val message: AntMessageApi
    val modal: dynamic
    val notification: dynamic
}


