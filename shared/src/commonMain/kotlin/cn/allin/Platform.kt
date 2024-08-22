package cn.allin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform