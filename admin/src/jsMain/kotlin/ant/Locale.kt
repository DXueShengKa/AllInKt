@file:JsModule("antd/lib/locale")
//@file:JsQualifier("zh_CN")
//@file:JsModule("../../node_modules/antd/lib/locale/index.js")
//@file:JsNonModule

package ant

import react.FC
import react.PropsWithChildren

external interface Locale {
    var locale: String
}

external interface LocaleProviderProps : PropsWithChildren {
    var locale: Locale
}

@JsName("default")
external fun useLocale(): Locale

@JsName("default")
external val LocaleProvider: FC<LocaleProviderProps>

//external val locale: Locale
//external val _


//@JsModule("locale/zh_CN")
//@JsQualifier("zh_Cn")
//@JsName("zh_Cn")
//external val zh: Locale
