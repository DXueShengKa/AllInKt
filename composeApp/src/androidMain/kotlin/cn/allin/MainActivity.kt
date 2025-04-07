package cn.allin

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.allin.data.dataModule
import cn.allin.di.androidModule
import cn.allin.di.appModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule

class MainActivity : ComponentActivity() {

    private val contextModule = module {
        single<Application> {
            application
        } bind Context::class
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainTheme {
                MainApp(
                    application = {
                        modules(contextModule, dataModule, defaultModule, AppKoinViewModel, androidModule, appModule)
                    }
                ) {
                    appNavGraphs()
                }
            }
        }
    }
}
