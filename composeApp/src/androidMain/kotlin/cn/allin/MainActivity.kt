package cn.allin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.allin.data.DataDI
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainTheme {
                MainApp(
                    application = {
                        modules(DataDI.module,
//                            AppKoinViewModel
                        )
                    }
                )
            }
        }
    }
}
