package androidx.navigation.react

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import react.FC


class ReactNavigator : Navigator<ReactNavigator.Destination>() {

    internal val transitionsInProgress
        get() = state.transitionsInProgress

    val backStack
        get() = state.backStack

    var isPop = false


    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        entries.forEach { entry -> state.pushWithTransition(entry) }
        isPop = false
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
        isPop = true
    }


    fun prepareForTransition(entry: NavBackStackEntry) {
        state.prepareForTransition(entry)
    }


    fun onTransitionComplete(entry: NavBackStackEntry) {
        state.markTransitionComplete(entry)
    }


    class Destination(
        navigator: ReactNavigator,
        val content: FC<NavProps>
    ) : NavDestination(navigator)

    override fun createDestination(): Destination {
        return Destination(this, FC { })
    }
}
