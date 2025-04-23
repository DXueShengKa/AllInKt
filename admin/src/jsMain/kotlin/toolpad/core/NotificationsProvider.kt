@file:JsModule("@toolpad/core/useNotifications")

package toolpad.core

import mui.material.SnackbarProps
import react.ElementType
import react.FC
import react.PropsWithChildren


external interface NotificationsProviderSlots {
    var snackbar: ElementType<SnackbarProps>?
}

external interface NotificationsProps : PropsWithChildren {
    var slots: NotificationsProviderSlots
}


external interface NotificationsConfig {
    var severity: SeverityStr
    var autoHideDuration: Int
}


external val NotificationsProvider: FC<NotificationsProps>

external interface Notifications {
    fun show(text: String, config: NotificationsConfig): Any
    fun close(key: Any = definedExternally)
}

external fun useNotifications(): Notifications
