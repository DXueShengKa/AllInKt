package cn.allin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp


@Composable
fun App() {
    Text("LocalNavController = staticCompositionLocalOf<NavController> ", fontSize = 20.sp)
}
