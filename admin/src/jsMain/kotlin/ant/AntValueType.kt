package ant

import seskar.js.JsValue

sealed external interface Variant {
    companion object {
        @JsValue("outlined")
        val outlined: Variant

        @JsValue("borderless")
        val borderless: Variant

        @JsValue("filled")
        val filled: Variant
    }
}


sealed external interface FormLayout {
    companion object {
        @JsValue("horizontal")
        val horizontal: FormLayout

        @JsValue("vertical")
        val vertical: FormLayout
    }
}

sealed external interface LabelAlign {
    companion object {

        @JsValue("left")
        val left: LabelAlign

        @JsValue("right")
        val right: LabelAlign
    }
}


sealed external interface ButtonType {
    companion object {
        @JsValue("default")
        val default: ButtonType

        @JsValue("primary")
        val primary: ButtonType

        @JsValue("dashed")
        val dashed: ButtonType

        @JsValue("link")
        val link: ButtonType

        @JsValue("text")
        val text: ButtonType
    }
}




