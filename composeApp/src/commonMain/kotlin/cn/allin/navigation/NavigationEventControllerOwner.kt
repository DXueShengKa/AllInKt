package cn.allin.navigation

import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import com.logic.navigation.NavDisplayController
import com.logic.navigation.SubrouteSceneStrategy

class NavigationEventControllerOwner<T : Any>(
    private val navigationEvent: NavigationEventDispatcher?,
    val controller: NavDisplayController<T>,
) : NavigationEventDispatcherOwner {
    val subrouteStrategy = SubrouteSceneStrategy<T>()

    private var _navigationEvent: NavigationEventDispatcher? = null

    override val navigationEventDispatcher: NavigationEventDispatcher
        get() =
            navigationEvent ?: _navigationEvent ?: NavigationEventDispatcher(
                onBackCompletedFallback = controller.onBack,
            ).also { _navigationEvent = it }
}
