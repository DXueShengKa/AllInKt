package cn.allin.test

import cn.allin.Application
import cn.allin.repository.UserRepository
import cn.allin.vo.UserVO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

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

    @Test
    fun add(){

        repeat(100){
            userRepository.add(
                UserVO(
//                    birthday = java.time.LocalDate.now().toKotlinLocalDate(),
                    name = Random.nextLong().toString(),
                    password = "1234"
                )
            )
        }

    }
}