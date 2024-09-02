package cn.allin


import androidx.navigation.react.navController
import androidx.navigation.react.navHost
import androidx.navigation.react.react
import cn.allin.net.HeaderAuthorization
import cn.allin.ui.NavAuth
import cn.allin.ui.NavUserListFc
import cn.allin.ui.RouteAuth
import cn.allin.ui.RouteUserList


val appNavController = navController()

val App = navHost(appNavController,
    if (HeaderAuthorization == null) RouteAuth else RouteUserList
) {
    react(RouteAuth, NavAuth)
    react(RouteUserList, NavUserListFc)

}

