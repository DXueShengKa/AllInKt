package cn.allin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem

val LocalNavController = staticCompositionLocalOf<NavController> { error("未初始化导航") }


@Composable
fun App() {

}


@Composable
fun LazyTableSimpleScreen(
    onBackClick: () -> Unit,
) {

    val columns = 10
    val rows = 30
    val cells = remember { createCells(columns, rows) }


    LazyTable(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = cells,
            layoutInfo = { LazyTableItem(it.first, it.second) }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "$it")
            }
        }

    }
}

fun createCells(columns: Int = COLUMNS, rows: Int = ROWS): List<Pair<Int, Int>> =
    buildList {
        repeat(rows) { row ->
            repeat(columns) { column ->
                add(column to row)
            }
        }
    }

private const val COLUMNS = 10
private const val ROWS = 30
