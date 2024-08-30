package androidx.navigation.react

import androidx.navigation.NavHostController

fun navController(): NavHostController {
    return createNavController()
}

private fun createNavController() =
    NavHostController().apply {
        navigatorProvider.addNavigator(ReactNavGraphNavigator(navigatorProvider))
        navigatorProvider.addNavigator(ReactNavigator())
    }

