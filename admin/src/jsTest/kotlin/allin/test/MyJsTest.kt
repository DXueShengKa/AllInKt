package allin.test

import cn.allin.utils.getDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.js.Date
import kotlin.test.Test

class MyJsTest {

    @Test
    fun date(){
        console.log(Date().toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()))
        console.log(TimeZone.currentSystemDefault())

        console.log(Date().toKotlinInstant().getDateTime())
    }
}
