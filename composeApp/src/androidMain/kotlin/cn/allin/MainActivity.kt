package cn.allin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.allin.data.dataModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.defaultModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainTheme {
                MainApp(
                    application = {
                        modules(dataModule, defaultModule, AppKoinViewModel)
                    }
                ) {
                    appNavGraphs()
                }
            }
        }
    }
}
