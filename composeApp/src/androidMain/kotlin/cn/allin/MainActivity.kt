package cn.allin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.allin.data.dataModule
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.defaultModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KoinApplication(application = {
                modules(dataModule, defaultModule,AppKoinViewModel)
            }) {
                App()
            }
        }
    }
}
