package androidx.navigation.react

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.react.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.react.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.react.ReactViewModelStoreOwner
import androidx.navigation.*
import kotlinx.coroutines.flow.*
import react.*

external interface NavProps : Props {
    var backStackEntry: NavBackStackEntry
}

fun navHost(navController: NavHostController, navGraph: NavGraph): FC<Props> {
    val reactViewModelStoreOwner = ReactViewModelStoreOwner()
    navController.setViewModelStore(reactViewModelStoreOwner.viewModelStore)
    navController.graph = navGraph

    return FC {
        val lifecycleOwner: LifecycleOwner? = useContext(LocalLifecycleOwner)

        useEffectOnceWithCleanup {
            if (lifecycleOwner != null) {
                navController.setLifecycleOwner(lifecycleOwner)
            }
            onCleanup(reactViewModelStoreOwner::dispose)
        }

        val composeNavigator = navController.navigatorProvider.get<ReactNavigator>(ReactNavigator.NAME)

        val (entry, setEntry) = navController.currentBackStackEntryFlow.asState()

        if (entry != null) {
            LocalLifecycleOwner.Provider(entry) {
                LocalViewModelStoreOwner.Provider(entry) {
                    (entry.destination as ReactNavigator.Destination).content {
                        backStackEntry = entry
                    }
                }
            }
        }

        useEffect(entry) {
            navController.visibleEntries.map {
                it.filter { entry ->
                    entry.destination.navigatorName == ReactNavigator.NAME
                }
            }.firstOrNull()?.forEach {
                composeNavigator.onTransitionComplete(it)
            }
        }
    }
}


fun navHost(
    navHostController: NavHostController,
    startDestination: String,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
): FC<Props> {
    return navHost(navHostController, navHostController.createGraph(startDestination, route, builder))
}