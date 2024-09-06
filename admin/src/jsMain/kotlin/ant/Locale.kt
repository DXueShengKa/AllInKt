@file:JsModule("antd/lib/locale")

package ant

import react.FC
import react.PropsWithChildren

external interface Locale {
    var locale: String
}

external interface LocaleProviderProps : PropsWithChildren {
    var locale: Locale
}


//@JsName("default")
external val LocaleProvider: FC<LocaleProviderProps>
