package cn.allin.ksp

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

class VoField(
    val name: String,
    val type: KSType,
)

class VoType(
    val voName: ClassName,
    val fields: List<VoField>,
)
