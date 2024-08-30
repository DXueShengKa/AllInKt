package androidx.navigation.react

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get
import react.FC

fun NavGraphBuilder.react(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: FC<NavProps>
) {
    destination(
        ReactNavigatorDestinationBuilder(
            provider[ReactNavigator::class],
            route,
            content
        ).apply {
            arguments.forEach { (argumentName, argument) -> argument(argumentName, argument) }
            deepLinks.forEach { deepLink -> deepLink(deepLink) }
        }
    )
}