package cn.allin


import ant.Button
import ant.Input
import ant.Variant
import cn.allin.ui.AddUserFC
import cn.allin.ui.UserFC
import emotion.react.css
import js.objects.jso
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.router.RouteObject
import react.router.useNavigate
import web.cssom.Color
import kotlin.js.Date

external interface AppP : Props {
//    var router: Router
}

val App = FC<AppP> {

    val navigate = useNavigate()
    div {

        css {
            color = Color("red")
        }

        +Date().toString()
    }


    Button {

        onClick = {
            navigate("/user")
        }
        +"user"
    }
    Button {

        onClick = {
           navigate("/addUser")
        }
        +"addUser"
    }

    Input {
        placeholder = "Input"
        variant = Variant.borderless
    }
}


val appRoutes: Array<RouteObject> = arrayOf(
    jso {
        path = "/"
        element = App.create()
        errorElement = FC {
            p { +"错误" }
        }.create()
    },
    jso {
        path = "/user"
        element = UserFC.create()
    },
    jso {
        path = "/addUser"
        element = AddUserFC.create()
    }
)
