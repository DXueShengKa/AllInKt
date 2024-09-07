package cn.allin.ui

import ant.Table
import ant.keyName
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

private val userVO = UserVO(
    1u, "name", "p",
)

val NavUserListFc = FC {

    var list: List<UserVO> by useState { emptyList() }

    div {
        useEffect(1) {
            list = ReqUser.getUserAll()
        }

        Table {
            dataSource = list.toTypedArray()


            columns = arrayOf(
                tableColumn<String> {
                    title = "mz"
                    dataIndex = userVO.keyName(UserVO::name)
                    render = { i ->
                        FC {
                            +i
                        }.create()
                    }
                    key = "0"
                },
                tableColumn<UInt> {
                    title = "id"
                    dataIndex = userVO.keyName(UserVO::userId)
                    key = "1"
                },
                tableColumn<LocalDate> {
                    title = "生日"
                    dataIndex = userVO.keyName(UserVO::birthday)
                    key = "3"
                }
            )
        }
    }
}

