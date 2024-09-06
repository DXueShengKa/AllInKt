@file:JsModule("antd")
package ant

import react.FC
import react.PropsWithChildren

external interface ConfigProviderProps : PropsWithChildren {
    var locale: Locale?
}

external val ConfigProvider: FC<ConfigProviderProps>