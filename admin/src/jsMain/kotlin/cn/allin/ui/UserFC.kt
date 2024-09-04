package cn.allin.ui

import ant.Table
import ant.keyName
import ant.tableColumn
import cn.allin.net.ReqUser
import cn.allin.vo.UserVO
import react.FC
import react.create
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useState


const val RouteUserList = "UserList"

val NavUserListFc = FC {

    var list: List<UserVO> by useState { emptyList() }

    div {
        useEffect(1) {
            list = ReqUser.getUserAll()
        }

        Table {

            dataSource = list.toTypedArray()

            val userVO = UserVO(
                1u, "2u", "n",
                //Instant.DISTANT_PAST.getDateTime()
            )

            columns = arrayOf(
                tableColumn<String> {
                    title = "mz"
                    dataIndex = userVO.keyName(UserVO::name)
                    render = { i ->
                        FC {
                            +i
                        }.create()
                    }
                    key = dataIndex
                },
                tableColumn<Int> {
                    title = "id"
                    dataIndex = userVO.keyName(UserVO::userId)
                    key = dataIndex
                }
            )
        }
    }
}

