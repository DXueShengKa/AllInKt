package cn.allin.config.security

object JwtUtil {
    private const val secret = "Zuc5p/uT4K4r7jcoR/IJtwG1V8sgFxRNyncJXtwyuu8="

//    private val key = Jwts.SIG.HS256.key()
//        .build()

    fun generateToken(user: String?): String {
        return "user:$user"
//        return Jwts.builder()
//            .content(user)
//            .encodePayload(false)
////            .issuedAt(Date())
////            .expiration(Date(System.currentTimeMillis() + 60 * 1000))
//            .signWith(key)
//            .compact()
    }

    fun extractUsername(token: String): String {
        return token.substring(token.indexOf(':') + 1)
//        return Jwts.parser().verifyWith(key).build().parseSignedContent(token).payload.let { String(it) }
    }

    fun validateToken(token: String): Boolean {
        return token.startsWith("user")
    }
}