package cn.allin.ui

import ant.Table
import ant.keyName
import ant.renderKey
import ant.tableColumn
import cn.allin.net.ReqUser
import cn.allin.vo.UserVO
import kotlinx.datetime.LocalDate
import react.FC
import react.create
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useState


const val RouteUserList = "UserList"

private var _vo: UserVO? = UserVO(
    1, "n", "p", LocalDate.fromEpochDays(1)
)

private val KeyName = _vo!!.keyName(UserVO::name)
private val KeyId = _vo!!.keyName(UserVO::userId)
private val KeyBirthday = _vo!!.keyName(UserVO::birthday)


val NavUserListFc = FC {
    _vo = null

    var list: List<UserVO> by useState { emptyList() }

    useEffect(1) {
        list = ReqUser.getUserAll().onEach {
            it.asDynamic()["key"] = it.userId.toString()
        }
    }
    div {

        Table {
            dataSource = list.toTypedArray()


            console.log(UserVO)
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
                }
            )
        }
    }
}

