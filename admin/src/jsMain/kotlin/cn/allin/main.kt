package cn.allin

import cn.allin.data.DataDI
import cn.allin.net.MainDI
import cn.allin.utils.DayLocalZhCn
import cn.allin.utils.KoinFC
import cn.allin.utils.dayjs
import cn.allin.utils.useKoinApplication
import muix.pickers.AdapterDayjs
import muix.pickers.LocalizationProvider
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module
import react.FC
import react.create
import react.dom.client.createRoot
import web.dom.ElementId
import web.dom.document


fun main() {

    dayjs.locale(DayLocalZhCn)
    val reactElement = LocalizationProvider.create {
        dateAdapter = AdapterDayjs
        adapterLocale = "zh-cn"
        MainFC()
    }

    createRoot(document.getElementById(ElementId("root"))!!)
        .render(reactElement)
}


private val MainFC = FC {
    val koin = useKoinApplication(koinApplication {
        modules(MainDI, DataDI.module)
    })

    KoinFC(koin) {

        react.router.RouterProvider {
            router = AppBrowserRouter
        }
    }
}
