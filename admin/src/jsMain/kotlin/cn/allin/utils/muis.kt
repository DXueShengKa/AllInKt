package cn.allin.utils

import js.objects.unsafeJso
import mui.material.BaseTextFieldProps
import mui.material.InputProps
import mui.material.styles.ThemeOptions
import mui.system.responsive


val <T : Any> T.rsv
    get() = responsive(this)

fun ThemeOptions.cssVariables(
    colorSchemeSelector: String? = null
) {
    val t = asDynamic()
    if (colorSchemeSelector != null) {
        val d: dynamic = unsafeJso()
        d.colorSchemeSelector = colorSchemeSelector
        t.cssVariables = d
    }
}

fun ThemeOptions.colorSchemes(
    light: Boolean,
    dark: Boolean,
) {
    asDynamic().colorSchemes = unsafeJso {
        this.light = light
        this.dark = dark
    }
}

//external interface SessionContextValue {
//    var session: Session?
//    var set: (Session?) -> Unit
//}
//
//val SessionContext = createContext<SessionContextValue>(unsafeJso {
//    set = {
//        session = it
//    }
//})

//fun useSessionContext(): SessionContextValue {
//    return use(SessionContext)
//}


fun BaseTextFieldProps.slotProps(
    input: InputProps? = null,
){
    asDynamic().slotProps = unsafeJso {
        if (input != null) {
            this.input = input
        }
    }
}
