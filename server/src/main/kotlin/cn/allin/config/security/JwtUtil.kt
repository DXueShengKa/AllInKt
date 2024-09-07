package cn.allin.config.security

import java.nio.ByteBuffer
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

object JwtUtil {
    private const val secret = "Zuc5p/uT4K4r7jcoR/IJtwG1V8sgFxRNyncJXtwyuu8="
    private val secretKeyFactory = SecretKeyFactory.getInstance("DES").run {
        generateSecret(DESKeySpec(secret.toByteArray()))
    }
    private val encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding").apply {
        init(Cipher.ENCRYPT_MODE, secretKeyFactory)
    }
    private val decrypt = Cipher.getInstance("DES/ECB/PKCS5Padding").apply {
        init(Cipher.DECRYPT_MODE, secretKeyFactory)
    }
    private val INT_BUFFER = ByteBuffer.allocate(4)

//    private val key = Jwts.SIG.HS256.key()
//        .build()


    fun generateToken(user: Int): String {
        INT_BUFFER.clear()
        INT_BUFFER.putInt(user)

        val e = encrypt.doFinal(INT_BUFFER.array())
        return Base64.getEncoder().encodeToString(e)
    }

    fun generateTokena(user: String?): String? {
        user ?: return null

//        DESKeySpec

        val e = encrypt.doFinal(user.toByteArray())
        return Base64.getEncoder().encodeToString(e)
//        return Jwts.builder()
//            .content(user)
//            .encodePayload(false)
////            .issuedAt(Date())
////            .expiration(Date(System.currentTimeMillis() + 60 * 1000))
//            .signWith(key)
//            .compact()
    }

    fun extractInt(token: String): Int {
        val d = Base64.getDecoder().decode(token)

        INT_BUFFER.clear()
        INT_BUFFER.put(decrypt.doFinal(d))
        return INT_BUFFER.getInt(0)
//        return Jwts.parser().verifyWith(key).build().parseSignedContent(token).payload.let { String(it) }
    }

    fun validateToken(token: String): Boolean {
        return token.startsWith("user")
    }
}