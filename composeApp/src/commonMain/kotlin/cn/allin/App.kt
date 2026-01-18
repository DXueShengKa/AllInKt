package cn.allin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cn.allin.ksp.navigation.NavRoute
import org.koin.core.annotation.KoinInternalApi
import org.koin.dsl.KoinAppDeclaration


internal const val RouteApp = "MainApp"

@NavRoute(routeString = RouteApp)
@Composable
internal fun App() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
        }
        item {
        }
        item {
        }
    }
}

@Composable
private fun AppItem(img: Painter, text: String, onItemClicked: () -> Unit) {
//    ElevatedCard(onClick = onItemClicked, Modifier.size(100.dp)) {
//        Icon(img, text)
//        Text(text)
//    }
}

@OptIn(KoinInternalApi::class)
@Composable
internal fun MainApp(
    application: KoinAppDeclaration,
) {

}
