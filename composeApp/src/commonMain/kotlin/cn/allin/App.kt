package cn.allin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders


@Composable
fun App() {
    Column {
        Text("LocalNavController = staticCompositionLocalOf<NavController> ", fontSize = 20.sp)
        Text(HttpHeaders.AccessControlAllowHeaders, color = Color.Red)
        Text(HttpHeaders.ContentLanguage, color = Color.Blue)
        Text(ContentType.Application.Zip.toString(), color = Color.Gray, fontFamily = FontFamily.Monospace)
        CircularProgressIndicator()
    }
}
