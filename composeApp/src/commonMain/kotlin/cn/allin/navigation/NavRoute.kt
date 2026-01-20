package com.logic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.NavKey
import com.logic.base.BaseViewModel
import kotlinx.coroutines.Job
import org.koin.compose.currentKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.component.KoinScopeComponent
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras
import org.koin.viewmodel.resolveViewModel

/**
 * 行为类似activity，用于导航路由UI
 *
 * route的伴生对象字段名为*clazzContentKey*和*metadata*的会通过ksp传入[com.logic.ui.entryRoute]中名字一样的参数
 */
abstract class NavRoute<VM : ViewModel, K : NavKey> :
    LifecycleOwner,
    ViewModelStoreOwner,
    KoinScopeComponent {
    lateinit var navController: NavDisplayController<NavKey>
        private set

    private val lifecycleRegistry =
        LifecycleRegistry(this)
            .apply {
                handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            }

    private var viewModelStoreOwner: ViewModelStoreOwner? = null
    private lateinit var koinScope: Scope

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = viewModelStoreOwner!!.viewModelStore

    override val scope: Scope
        get() = koinScope

    @Suppress("UNCHECKED_CAST")
    protected open val vm: VM = (BaseViewModel.Companion as VM)

    private var eventJob: Job? = null

    protected lateinit var key: K

    /**
     * 开启[handleEvent]订阅事件
     */
    protected fun enableEvent() {
        val job = eventJob
        if (job == null || !job.isActive) {
            TODO()
        }
    }

    @Composable
    fun InitCompose(key: K) {
        this.key = key
        navController = NavDisplayController.current()
        viewModelStoreOwner = LocalViewModelStoreOwner.current
        koinScope = currentKoinScope()
        DisposableEffect(this) {
            val vm = vm
            if (vm is BaseViewModel<*>) {
                vm.setRoute(this@NavRoute)
            }

            if (lifecycle.currentState == Lifecycle.State.CREATED) {
                initView()
                initData()
            }

            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

            if (eventJob != null) {
                enableEvent()
            }

            onDispose {
                if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                }
            }
        }
    }

    @Composable
    abstract fun Content()

    open fun initView() {}

    open fun initData() {}

    open fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @OptIn(KoinInternalApi::class)
    protected inline fun <reified VM : ViewModel> viewModels(
        key: String? = null,
        extras: CreationExtras = defaultExtras(this),
        qualifier: Qualifier? = null,
        noinline parameters: (() -> ParametersHolder)? = null,
    ): Lazy<VM> =
        lazy(LazyThreadSafetyMode.NONE) {
            resolveViewModel(
                VM::class,
                viewModelStore,
                key,
                extras,
                qualifier,
                scope,
                parameters,
            )
        }

    @OptIn(KoinInternalApi::class)
    protected inline fun <reified VM : ViewModel> koinViewModel(
        key: String? = null,
        extras: CreationExtras = defaultExtras(this),
        qualifier: Qualifier? = null,
        noinline parameters: (() -> ParametersHolder)? = null,
    ) = resolveViewModel(
        VM::class,
        viewModelStore,
        key,
        extras,
        qualifier,
        scope,
        parameters,
    )
}
