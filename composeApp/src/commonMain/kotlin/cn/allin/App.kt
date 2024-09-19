package cn.allin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import cn.allin.data.repository.UserRepository
import cn.allin.vo.UserVO
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import org.koin.compose.getKoin


@Composable
fun App() {
    MaterialTheme {
        Column {


//            AppTest()
            val get = getKoin().get<UserRepository>()
            val list = remember { mutableStateListOf<UserVO>() }
            LaunchedEffect(Unit) {
                list.addAll(get.getUser())
            }

            LazyColumn {
                item {
                    Text("--->")
                }
                items(list) {
                    Text(it.toString())
                }
            }

        }
    }
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