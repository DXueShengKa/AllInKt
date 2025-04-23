@file:JsModule("@toolpad/core/useDialogs")

package toolpad.core

import react.FC
import react.PropsWithChildren
import react.ReactNode
import kotlin.js.Promise

external interface DialogsProps : PropsWithChildren {
    var unmountAfter: Number?
}


external val DialogsProvider: FC<DialogsProps>


external interface DialogOptions<R> {
    var onClose: (result: R) -> Promise<Unit>
}


external interface ConfirmOptions : DialogOptions<Boolean> {
    var title: ReactNode?
    var okText: ReactNode?
    var severity: SeverityStr?
    var cancelText: ReactNode?
}

external interface Dialog {

    fun confirm(mag: ReactNode?, option: ConfirmOptions? = definedExternally): Promise<Boolean>

    fun <R> onClose(dialog: Promise<R> = definedExternally, result: R = definedExternally): Promise<R>
}


external fun useDialogs(): Dialog
