@file:JsModule("antd")
package ant

import ant.locale.Locale
import react.FC
import react.PropsWithChildren

external interface ConfigProviderProps : PropsWithChildren {
    var locale: Locale?

    var componentSize: SpaceSize

    var theme: ThemeConfig
}

external val ConfigProvider: FC<ConfigProviderProps>