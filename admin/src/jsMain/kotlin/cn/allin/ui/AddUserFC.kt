package cn.allin.ui

import cn.allin.ViewModel
import cn.allin.net.ReqUser
import cn.allin.vo.Gender
import kotlinx.coroutines.launch
import kotlin.js.Date


const val RouteAddUser = "addUser"

private class AddUserVM : ViewModel() {
    init {
        println("addUserVM")
    }

    fun add(addUser: AddUser) {
        viewModelScope.launch {
            ReqUser.addUser(addUser)
        }
    }
}



external interface AddUser {
    var name: String
    var password: String
    var birthday: Date?
    var address: String?
    var gender: Gender?
}
