package cn.allin.components

import cn.allin.ValidatorError
import cn.allin.VoValidatorMessage
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useRefInit
import js.objects.unsafeJso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.AlertColor
import mui.material.BaseTextFieldProps
import mui.material.Button
import mui.material.FormHelperTextOwnProps
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.useEffectOnceWithCleanup
import react.useState
import web.cssom.px
import web.form.FormData
import web.html.ButtonType
import web.html.submit

external interface AdminFormProps : PropsWithChildren {

    var formState: AdminFormState

    var dataId: String?

    var getData: suspend (Number) -> Unit
    var onSubmit: suspend (FormData) -> Unit

}


class AdminFormState(
    val errorHelperState: StateInstance<VoValidatorMessage?>
) {

    fun BaseTextFieldProps.register(fieldName: String, fieldValue: Any?, onValue: (String) -> Unit) {
        name = fieldName
        if (fieldValue != null) {
            value = fieldValue
        }
        onChange = {
            onValue(it.target.asDynamic().value)
        }
        errorHelperState.component1()?.also {
           if (it.field == fieldName) {
               error = true
               helperText = "${it.code},${it.message}".asDynamic()
           } else {
               error = false
           }
        }
    }

    fun FormHelperTextOwnProps.register(fieldName: String): String? {
        errorHelperState.component1()?.also {
            if (it.field == fieldName) {
                error = true
                return "${it.code},${it.message}"
            } else {
                error = false
            }
        }
        return null
    }

    fun setErrorHelper(error: VoValidatorMessage?) {
        errorHelperState.component2()(error)
    }
}

fun useAdminForm(): AdminFormState {
    val errorHelperState = useState<VoValidatorMessage>()
    val formState = useRefInit {
        AdminFormState(errorHelperState)
    }
    return formState
}

val AdminForm = FC<AdminFormProps> { props ->
    val formState = props.formState
    val cs = useCoroutineScope()
//todo    val formParam = useParams()
    var submitResult: Pair<AlertColor, String>? by useState()
    val (errorHelper) = formState.errorHelperState

    var formSubmitName by useState("")

//    useEffectOnceWithCleanup {
//        val id = props.dataId?.let { formParam[it] }?.toIntOrNull()
//        formSubmitName = if (id != null && id > 0) {
//            cs.launch {
//                props.getData(id)
//            }
//            "更新"
//        } else {
//            "添加 "
//        }
//
//    }

    Stack {
        sx = unsafeJso {
            width = 600.px
        }
        spacing = responsive(2)
        direction = responsive(StackDirection.column)
        component = form
        onSubmit = submit@{ formEvent ->
            formEvent.preventDefault()
            cs.launch(
                CoroutineExceptionHandler { _, t ->
                    if (t is ValidatorError)
                        formState.setErrorHelper(t.validatorMessage)
                    submitResult = AlertColor.error to "${formSubmitName}失败"
                }
            ) {
                props.onSubmit(FormData(formEvent.target))
                submitResult = AlertColor.success to "${formSubmitName}成功"

                delay(2000)
                submitResult = null
            }
        }

        +props.children

        Button {
            type = ButtonType.submit
            +formSubmitName
        }

        submitResult?.also { (c, s) ->
            Alert {
                severity = c.asDynamic()
                +s
            }
        }
    }
}
