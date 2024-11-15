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
    1, "name", "p", LocalDate(1996, 1, 1)
)

val NavUserListFc = FC {

    var list: List<UserVO> by useState { emptyList() }

    div {
        useEffect(1) {
            list = ReqUser.getUserAll().onEach {
                it.asDynamic()["key"] = it.userId.toString()
            }
        }

        Table {
            dataSource = list.toTypedArray()


            columns = arrayOf(
                tableColumn<String> {
                    title = "mz"
                    dataIndex = userVO.keyName(UserVO::name)
                    render = { y, _, _ ->
                        console.log(y)
                        FC {
                            +y
                        }.create()
                    }
                },
                tableColumn<UInt> {
                    title = "id"
                    dataIndex = userVO.keyName(UserVO::userId)
                },
                tableColumn {
                    title = "生日"
                    dataIndex = userVO.keyName(UserVO::birthday)

                    render = { localDate, p, i ->
                        console.log(p, i)
                        FC {
                            +localDate.toString()
                        }.create()
                    }
                }
            )
        }
    }
}

