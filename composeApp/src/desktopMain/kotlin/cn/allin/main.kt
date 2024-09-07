package cn.allin

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AllInKt",
    ) {
//        KoinApplication(application = {
//            modules(dataModule, defaultModule)
//        }) {
//            App()
            val navController = rememberNavController()
            NavHost(navController,"a"){
                composable("a"){
                    Button({
                        navController.navigate("b")
                    }){
                        Text("to b")
                    }
                }
                composable("b"){ e ->


//                val current = LocalLifecycleOwner.current
                    LaunchedEffect(Unit){
                        e.lifecycle.currentStateFlow.collect{
                            println(it)
                        }
                    }

                    Button({
                        navController.navigateUp()
                    }){
                        Text("to a")
                    }
                }
            }
        }
//    }
}