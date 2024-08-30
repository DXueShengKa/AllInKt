package androidx.navigation.react

import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavType
import react.FC
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ReactNavigatorDestinationBuilder: NavDestinationBuilder<ReactNavigator.Destination> {

    private val composeNavigator: ReactNavigator
    private val content: FC<NavProps>

    constructor(
        navigator: ReactNavigator,
        route: String,
        content: FC<NavProps>
    ) : super(navigator, route) {
        this.composeNavigator = navigator
        this.content = content
    }

    constructor(
        navigator: ReactNavigator,
        route: KClass<*>,
        typeMap: Map<KType, NavType<*>>,
        content: FC<NavProps>
    ) : super(navigator, route, typeMap) {
        this.composeNavigator = navigator
        this.content = content
    }

    override fun instantiateDestination(): ReactNavigator.Destination {
        return ReactNavigator.Destination(composeNavigator, content)
    }
}