package cn.allin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import cn.allin.data.repository.UserRepository
import cn.allin.vo.UserVO
import org.koin.compose.getKoin


@Composable
fun App() {
    MaterialTheme {
        Column {

        Text("")

            Text("")
        }

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