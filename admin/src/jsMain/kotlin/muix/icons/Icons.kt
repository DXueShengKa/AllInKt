package muix.icons

import mui.material.SvgIconProps
import react.FC

/**
 * 用mui kt封装库的，打包时不会被压缩，生成产物过大, 需要图标再手动声明
 */
typealias SvgIconFC = FC<SvgIconProps>
