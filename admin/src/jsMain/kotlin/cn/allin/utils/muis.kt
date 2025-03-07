import js.objects.jso
import mui.material.styles.ThemeOptions


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
