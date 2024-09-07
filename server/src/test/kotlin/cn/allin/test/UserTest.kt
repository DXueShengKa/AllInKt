package cn.allin.test

import cn.allin.Application
import cn.allin.exposed.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
@AutoConfigureMockMvc
class UserTest {


    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun user(){

    }

    @Test
    @WithMockUser(username = "5", password = "1234")
    fun userS(){
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    val k = "234345sdf"
    @Test
    fun add(){

        println( (-11).toUInt())

        val desKeySpec = DESKeySpec(k.toByteArray())
        val secretKeyFactory = SecretKeyFactory.getInstance("DES")
        val key = secretKeyFactory.generateSecret(desKeySpec)

        val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE,key)
        val bytes = cipher.doFinal("12".toByteArray())
        println("-> ${Base64.getEncoder().encodeToString(bytes)}")

        cipher.init(Cipher.DECRYPT_MODE,key)


        println(">> ${String(cipher.doFinal(bytes))}")

/*        repeat(10){
            userRepository.add(
                UserVO(
//                    birthday = java.time.LocalDate.now().toKotlinLocalDate(),
                    name = Random.nextLong().toString(),
                    password = "1234"
                )
            )
        }*/

    }
}