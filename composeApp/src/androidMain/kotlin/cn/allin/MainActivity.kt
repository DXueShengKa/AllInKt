package cn.allin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import cn.allin.navigation.appNavGraphs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainApp(
                    application = {
                        modules(AppKoinViewModel)
                    }
                ) {
                    appNavGraphs()
                }
            }
        }
    }
}
