@file:JsModule("@toolpad/core/useNotifications")
package toolpad.core

import react.FC
import react.PropsWithChildren

external interface NotificationsProps: PropsWithChildren

external val NotificationsProvider: FC<NotificationsProps>


external fun useNotifications(): Notifications
