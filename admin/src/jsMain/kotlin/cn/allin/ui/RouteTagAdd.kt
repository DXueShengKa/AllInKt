package cn.allin.ui

import cn.allin.api.ApiQandaTag
import cn.allin.components.AdminForm
import cn.allin.components.useAdminForm
import cn.allin.description
import cn.allin.tagName
import cn.allin.utils.reactNode
import cn.allin.utils.useInject
import cn.allin.vo.QaTagVO
import js.objects.unsafeJso
import mui.material.FormControl
import mui.material.TextField
import react.FC
import react.router.RouteObject
import react.useState
import toolpad.core.NavigationObj

private val TagAddFC = FC {
    val apiQandaTag: ApiQandaTag = useInject()
    var tagForm by useState {
        QaTagVO(0, "")
    }
    val adminForm = useAdminForm()
    AdminForm {
        formState = adminForm
        dataId = "tagId"
        getData = {
            tagForm = apiQandaTag.get(it.toInt())
        }
        onSubmit = { fa ->
            if (tagForm.id < 1) {
                apiQandaTag.add(tagForm)
                tagForm = QaTagVO(0, "")
            } else {
                apiQandaTag.update(tagForm)
            }
        }

        FormControl {
            TextField {
                label = reactNode("标签名")
                with(adminForm) {
                    register(QaTagVO.tagName.name, tagForm.tagName) {
                        tagForm = tagForm.copy(tagName = it)
                    }
                }
            }
        }

        FormControl {
            TextField {
                label = reactNode("介绍标签")

                adminForm.apply {
                    register(QaTagVO.description.name, tagForm.description) {
                        tagForm = tagForm.copy(description = it)
                    }
                }
            }
        }

    }
}


val RouteTagAdd = object : Routes {

    override val navigation: NavigationObj = unsafeJso {
        title = "添加标签"
        segment = "tag/add/-1"
    }

    override val routeObj: RouteObject = unsafeJso {
        Component = TagAddFC
        path = "tag/add/:tagId"
    }
}

