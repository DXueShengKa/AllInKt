package cn.allin.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

object JwtUtil {
    // JWT 配置
    private const val TOKEN_VALIDITY = 3600 * 1000L // 1小时
    private const val ISSUER = "allin-server"

    // 使用 HMAC-SHA256 签名密钥（至少 256 位）
    private val key: SecretKey =
        Jwts.SIG.HS256
            .key()
            .build()

    /**
     * 生成 JWT Token
     * @param userId 用户ID
     * @return JWT Token 字符串
     */
    fun generateToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + TOKEN_VALIDITY)

        return Jwts
            .builder()
            .subject(userId.toString())
            .issuer(ISSUER)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    /**
     * 从 Token 中提取用户ID
     * @param token JWT Token
     * @return 用户ID
     */
    fun extractUserId(token: String): Long =
        Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
            .toLongOrNull()
            ?: throw IllegalArgumentException("Invalid token: userId is missing or malformed")

    /**
     * 验证 Token 是否有效
     * @param token JWT Token
     * @return 是否有效
     */
    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parser()
                .verifyWith(key)
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
}
