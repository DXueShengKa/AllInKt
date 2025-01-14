package ant

import js.objects.JsoDsl
import js.objects.jso
import react.ChildrenBuilder
import react.FC
import react.Props
import react.PropsWithChildren
import react.ReactDsl
import react.ReactElement
import react.create
import react.dom.html.HTMLAttributes
import web.events.Event
import web.html.HTMLHtmlElement


fun <T> ChildrenBuilder.form(block: @ReactDsl FormProps<T>.() -> Unit) {
    Form.invoke(block)
}

fun ChildrenBuilder.layout(block: @ReactDsl LayoutProps.(LayoutFC) -> Unit) {
    Layout {
        block(Layout)
    }
}


@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class MenuDsl


@MenuDsl
class MenuBuilder<T: MenuItemType>() {

    val list = mutableListOf<T>()

    fun menu(block: @JsoDsl MenuItemType.() -> Unit) {
        list.add(jso(block))
    }

    fun subMenu(block: @JsoDsl SubMenuType.() -> Unit) {
        list.add(jso(block))
    }

    fun SubMenuType.items(vararg sub: SubMenuType.() -> Unit) {
        children = Array(sub.size) {
            jso(sub[it])
        }
    }

    fun MenuItemType.icon(block: @ReactDsl ChildrenBuilder.() -> Unit) {
        icon = FC(block).create()
    }


    fun MenuItemType.label(block: @ReactDsl ChildrenBuilder.() -> Unit) {
        label = FC(block).create()
    }
}

fun <T:MenuItemType> createMenuItems(block: @MenuDsl MenuBuilder<T>.() -> Unit): Array<T> {
    return MenuBuilder<T>().apply(block).list.toTypedArray()
}

external interface MenuOnClick {
    var key: String
    var keyPath: Array<String>
    var selectedKeys: Array<String>?
    var domEvent: Event
}


external interface AntChildrenProps : PropsWithChildren, HTMLAttributes<HTMLHtmlElement>

external interface AntProps : Props, HTMLAttributes<HTMLHtmlElement>


typealias UseMessage = Array<dynamic>

fun UseMessage.messageApi(): AntMessageApi = get(0)

fun UseMessage.contextHolder(): ReactElement<PropsWithChildren> = get(1)




