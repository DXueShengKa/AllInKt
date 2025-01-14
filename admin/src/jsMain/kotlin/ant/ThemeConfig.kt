package ant

import js.objects.JsoDsl
import js.objects.jso

external interface ThemeConfig {
    var token: SeedToken
}


inline fun AliasToken(block: @JsoDsl AliasToken.() -> Unit,): AliasToken = jso(block)


external interface SeedToken {

    var colorPrimary: String
}


external interface MapToken : SeedToken {
    /**
     * 	默认尺寸
     */
    var size: Int
}

external interface AliasToken : MapToken {
    /**
     * 控制元素的内间距。
     */
    var padding: Int
}



