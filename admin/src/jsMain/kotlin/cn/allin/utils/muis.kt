package cn.allin.utils

import js.objects.jso
import mui.material.styles.ThemeOptions
import mui.system.responsive
import react.createContext
import react.use
import toolpad.core.Session


val <T : Any> T.rsv
    get() = responsive(this)

fun ThemeOptions.cssVariables(
    colorSchemeSelector: String? = null
) {
    val t = asDynamic()
    if (colorSchemeSelector != null) {
        val d: dynamic = jso()
        d.colorSchemeSelector = colorSchemeSelector
        t.cssVariables = d
    }
}

fun ThemeOptions.colorSchemes(
    light: Boolean,
    dark: Boolean,
) {
    asDynamic().colorSchemes = jso {
        this.light = light
        this.dark = dark
    }
}

external interface SessionContextValue {
    var session: Session?
    var set: (Session?) -> Unit
}

val SessionContext = createContext<SessionContextValue>(jso {
    set = {
        session = it
    }
})

fun useSessionContext(): SessionContextValue {
    return use(SessionContext)
}
