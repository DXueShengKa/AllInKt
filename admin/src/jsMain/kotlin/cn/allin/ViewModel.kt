package cn.allin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import react.ChildrenBuilder
import react.FC
import react.Props
import react.useEffectOnceWithCleanup
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.createInstance

abstract class ViewModel {
    val viewModelScope = CoroutineScope(EmptyCoroutineContext)


    open fun onCleared() {
        viewModelScope.cancel()
    }
}


external interface ViewModelProps : Props {
    var viewModel: ViewModel
}


@OptIn(ExperimentalJsReflectionCreateInstance::class)
fun <VM : ViewModel> viewModelFc(clazz: KClass<VM>, fc: ChildrenBuilder.(VM) -> Unit): FC<Props> {
    val vm: VM = clazz.createInstance()
    return FC {
        useEffectOnceWithCleanup {
            onCleanup(vm::onCleared)
        }
        fc(vm)
    }
}


inline fun <reified VM : ViewModel> viewModelFc(noinline fc: ChildrenBuilder.(VM) -> Unit): FC<Props> {
    return viewModelFc(VM::class,fc)
}



