package androidx.navigation.react

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.react.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.react.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.react.ReactViewModelStoreOwner
import androidx.navigation.*
import react.FC
import react.Props
import react.useContext
import react.useEffectOnceWithCleanup

external interface NavProps : Props {
    var backStackEntry: NavBackStackEntry
}

fun navHost(navHostController: NavHostController, navGraph: NavGraph): FC<Props> {
    val reactViewModelStoreOwner = ReactViewModelStoreOwner()
    navHostController.setViewModelStore(reactViewModelStoreOwner.viewModelStore)
    navHostController.graph = navGraph

    return FC {

        val lifecycleOwner: LifecycleOwner? = useContext(LocalLifecycleOwner)

        useEffectOnceWithCleanup {
            if (lifecycleOwner != null) {
                navHostController.setLifecycleOwner(lifecycleOwner)
            }
            onCleanup(reactViewModelStoreOwner::dispose)
        }

        val (entry, setEntry) = navHostController.currentBackStackEntryFlow.asState()
        if (entry != null) {
            LocalViewModelStoreOwner.Provider(reactViewModelStoreOwner) {
                (entry.destination as ReactNavigator.Destination).content {
                    backStackEntry = entry
                }
            }
        }
    }
}


fun navHost(navHostController: NavHostController, startDestination: String, route: String? = null, builder: NavGraphBuilder.() -> Unit): FC<Props> {
    return navHost(navHostController, navHostController.createGraph(startDestination, route, builder))
}