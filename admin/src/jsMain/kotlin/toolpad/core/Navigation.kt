package toolpad.core

import react.ReactNode

typealias Navigation = Array<NavigationObj>

external interface NavigationObj {
    var segment: String?
    var kind: String?
    var title: String?
    var icon: ReactNode?
}