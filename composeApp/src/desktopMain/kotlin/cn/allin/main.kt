package cn.allin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

                    Column {

                        var s = remember { mutableStateOf("") }

                        LazyTableSimpleScreen {

                        }

                        Button({
                            navController.navigateUp()
                        }){
                            Text("to a")
                        }
                    }
                }
            }
        }
//    }
}

