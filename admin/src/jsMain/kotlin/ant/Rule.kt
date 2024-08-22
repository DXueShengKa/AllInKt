package ant

import js.objects.jso
import kotlin.js.Promise
import kotlin.js.RegExp

external interface Rule {
    var len: Int
    var min: Int
    var max: Int

    var message: String
    var required: Boolean
    var pattern: RegExp

    var enum: Array<dynamic>

    /**
     * 类型，常见有 string |number |boolean |url | email。更多请参考[此处](https://github.com/react-component/async-validator#type)
     */
    var type: String?

    var validator: (Rule, dynamic) -> Promise<dynamic>
}

fun <T> Rule.validator(v: (Rule, T) -> Promise<dynamic>){
    validator = v
}


fun rule(builder: Rule.() -> Unit): Rule = jso(builder)