package cn.allin

import js.reflect.newInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import react.ChildrenBuilder
import react.FC
import react.Props
import react.useEffectOnceWithCleanup
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass

abstract class ViewModel {
    val viewModelScope = CoroutineScope(EmptyCoroutineContext)


    open fun onCleared() {
        viewModelScope.cancel()
    }
}


external interface ViewModelProps : Props {
    var viewModel: ViewModel
}


fun <VM : ViewModel> viewModelFc(clazz: KClass<VM>, fc: ChildrenBuilder.(VM) -> Unit): FC<Props> {
    val vm: VM = clazz.js.newInstance()
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



