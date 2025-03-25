package cn.allin.ui.jlota

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.allin.ota.JlOta
import org.koin.compose.getKoin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JlOtaScreen(){
    val jlOta: JlOta = getKoin().get()
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = JlOta.version)
    }

}
