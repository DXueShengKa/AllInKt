package androidx.navigation.react

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get
import react.ChildrenBuilder
import react.FC
import react.ReactDsl

fun NavGraphBuilder.react(
    route: String,
    content: FC<NavProps>,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList()
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

fun NavGraphBuilder.react(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @ReactDsl ChildrenBuilder.(NavProps) -> Unit
) {
    destination(
        ReactNavigatorDestinationBuilder(
            provider[ReactNavigator::class],
            route,
            FC(content)
        ).apply {
            arguments.forEach { (argumentName, argument) -> argument(argumentName, argument) }
            deepLinks.forEach { deepLink -> deepLink(deepLink) }
        }
    )
}
