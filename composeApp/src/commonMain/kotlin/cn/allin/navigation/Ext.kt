package cn.allin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavEntryDecoratorDefaults
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.SaveableStateHolderNavEntryDecorator
import com.logic.navigation.NavDisplayController
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun NavDisplayController<NavKey>.navigate(
    route: String,
    top: Boolean = false,
) {
    navigate(StringNavKey(route), top)
}

fun NavDisplayController<NavKey>.navigateFixed(route: String) {
    navigateFixed(StringNavKey(route))
}

@Serializable
@SerialName("StringNavKey")
class StringNavKey(
    val route: String,
) : NavKey {
    override fun hashCode(): Int = route.hashCode()

    override fun toString(): String = "$route@${super.hashCode()}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as StringNavKey

        return route == other.route
    }
}

@Composable
fun <T : Any> rememberNavEntryDecorators(
    saveableStateHolder: SaveableStateHolder = rememberSaveableStateHolder(),
    viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        },
    removeViewModelStoreOnPop: () -> Boolean = ViewModelStoreNavEntryDecoratorDefaults.removeViewModelStoreOnPop(),
): List<NavEntryDecorator<T>> =
    remember(saveableStateHolder, viewModelStoreOwner, removeViewModelStoreOnPop) {
        listOf(
            SaveableStateHolderNavEntryDecorator(saveableStateHolder),
            ViewModelStoreNavEntryDecorator(viewModelStoreOwner.viewModelStore, removeViewModelStoreOnPop),
        )
    }
