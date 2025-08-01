package cn.allin.components

import cn.allin.api.ApiRegion
import cn.allin.utils.reactNode
import cn.allin.utils.useInject
import cn.allin.vo.RegionVO
import js.objects.unsafeJso
import mui.material.Box
import mui.material.FormControl
import mui.material.InputLabel
import mui.material.MenuItem
import mui.material.Select
import react.FC
import react.Props
import react.useEffect
import react.useState
import web.cssom.px
import web.dom.ElementId

external interface SelectAddressProps : Props {
    var onValue: (String) -> Unit
}

val SelectAddress = FC<SelectAddressProps> { props ->
    var selectProvince by useState<RegionVO?>(null)
    var selectCity by useState<RegionVO?>(null)
    var selectCounty by useState<RegionVO?>(null)

    var provinceList by useState<List<RegionVO>>(emptyList())
    var cityList by useState<List<RegionVO>>(emptyList())
    var countyList by useState<List<RegionVO>>(emptyList())

    val apiRegion: ApiRegion = useInject()


    useEffect(Unit) {
        val province: List<RegionVO> = apiRegion.getAllProvince()
        selectProvince = province[0]
        provinceList = province
    }

    useEffect(selectProvince) {
        val p = selectProvince
        if (p == null) return@useEffect

        cityList = apiRegion.getCity(p.id).also {
            selectCity = it[0]
        }
    }

    useEffect(selectCity) {
        val c = selectCity
        if (c == null) return@useEffect

        countyList = apiRegion.getCounty(c.id).also {
            selectCounty = it[0]
        }
    }

    Box {

        ProvinceSelect {
            value = selectProvince
            onValue = {
                selectProvince = it
            }
            regionList = provinceList

            id = "省份"
        }

        ProvinceSelect {
            value = selectCity
            onValue = {
                selectCity = it
            }
            regionList = cityList
            id = "城市"
        }

        ProvinceSelect {
            value = selectCounty
            onValue = {
                selectCounty = it
                props.onValue(selectProvince?.name + selectCity?.name + it.name)
            }
            regionList = countyList
            id = "县区"
        }
    }
}

private external interface SelectProps : Props {
    var value: RegionVO?
    var onValue: (RegionVO) -> Unit
    var id: String
    var regionList: List<RegionVO>?
}

private val ProvinceSelect = FC<SelectProps> { p ->
    FormControl {
        InputLabel {
            id = ElementId(p.id)
            htmlFor = "select".asDynamic()
            +p.id
        }

        val regionList = p.regionList ?: emptyList()

        Select {
            sx = unsafeJso {
                minWidth = 200.px
            }

            id = p.id.asDynamic()
            label = reactNode(p.id)

            value = p.value?.name ?: ""
            onChange = { e, r ->
                val n = e.target.value
                regionList.find { it.name == n }?.let {
                    p.onValue(it)
                }
            }
            MenuItem {
                value = ""
                +"请选择${p.id}"
            }
            regionList.forEach {
                MenuItem {
                    key = it.id.toString()
                    value = it.name
                    +it.name
                }
            }
        }

    }
}
