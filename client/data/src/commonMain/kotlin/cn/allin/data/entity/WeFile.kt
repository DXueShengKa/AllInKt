package cn.allin.data.entity

import kotlinx.io.files.Path

class WeFile(
    val path: Path,
    val isDir: Boolean,
    val thumbnail: String? = null
)
