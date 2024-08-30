package androidx.lifecycle.viewmodel.react

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.react.collectAsState
import react.StateInstance
import react.createContext
import react.useContext
import kotlin.reflect.KClass

val LocalLifecycleOwner = createContext<LifecycleOwner>()

val LocalViewModelStoreOwner = createContext<ViewModelStoreOwner>()


fun Lifecycle.currentStateAsState(): StateInstance<Lifecycle.State> = currentStateFlow.collectAsState()


class ReactViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
    fun dispose() {
        viewModelStore.clear()
    }
}

fun <VM : ViewModel> useViewModel(
    modelClass: KClass<VM>,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(useContext(LocalViewModelStoreOwner)) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }
): VM = viewModelStoreOwner.get(modelClass, key, factory, extras)


internal fun <VM : ViewModel> ViewModelStoreOwner.get(
    modelClass: KClass<VM>,
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras
): VM {
    console.log(modelClass)
    console.log(this)
    val provider = if (factory != null) {
        ViewModelProvider.create(this.viewModelStore, factory, extras)
    } else if (this is HasDefaultViewModelProviderFactory) {
        ViewModelProvider.create(this.viewModelStore, this.defaultViewModelProviderFactory, extras)
    } else {
        ViewModelProvider.create(this)
    }
    console.log(provider)
    return if (key != null) {
        provider[key, modelClass]
    } else {
        provider[modelClass]
    }
}