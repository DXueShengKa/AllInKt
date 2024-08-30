package androidx.navigation.react

import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider

class ReactNavGraphNavigator(navigatorProvider: NavigatorProvider) : NavGraphNavigator(navigatorProvider) {

    override fun createDestination(): NavGraph {
        return ReactNavGraph(this)
    }

    class ReactNavGraph(navGraphNavigator: Navigator<out NavGraph>) : NavGraph(navGraphNavigator)
}