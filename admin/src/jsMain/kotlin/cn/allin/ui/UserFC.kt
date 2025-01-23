package cn.allin.ui

import ant.Table
import ant.keyName
import ant.renderKey
import ant.tableColumn
import cn.allin.net.ReqUser
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import kotlinx.datetime.LocalDate
import react.FC
import react.create
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useState


const val RouteUserList = "UserList"

private var _vo: UserVO? = UserVO(
    1, "n", "p", LocalDate.fromEpochDays(1), "r", "a", Gender.Male
)

private val KeyName = _vo!!.keyName(UserVO::name)
private val KeyId = _vo!!.keyName(UserVO::userId)
private val KeyBirthday = _vo!!.keyName(UserVO::birthday)
private val KeyAddress = _vo!!.keyName(UserVO::address)
private val KeyGender = _vo!!.keyName(UserVO::gender)


val NavUserListFc = FC {
    _vo = null

    var userList: Array<UserVO> by useState { emptyArray() }

    useEffect(1) {
        userList = ReqUser.getUserAll().onEach {
            it.asDynamic()["key"] = it.userId.toString()
        }.toTypedArray()
    }

    div {

        Table {

            dataSource = userList

            columns = arrayOf(
                tableColumn<String> {
                    title = "名字"
                    dataIndex = KeyName
                },
                tableColumn<Long> {
                    title = "id"
                    dataIndex = KeyId
                    renderKey {
                        +it.toString()
                    }
                },
                tableColumn {
                    title = "生日"
                    dataIndex = KeyBirthday

                    render = { localDate, p, i ->
                        FC {
                            +localDate.toString()
                        }.create()
                    }
                },
                tableColumn<Gender?> {
                    title = "性别"
                    dataIndex = KeyGender
                    renderKey {
                        + when (it) {
                            Gender.Male -> "男"
                            Gender.Female -> "女"
                            null -> "不显示"
                        }
                    }
                },
                tableColumn<String> {
                    title = "地址"
                    dataIndex = KeyAddress
                }
            )
        }
    }
}

