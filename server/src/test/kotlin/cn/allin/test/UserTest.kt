package cn.allin.test

import cn.allin.Application
import cn.allin.repository.UserRepository
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.random.Random

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
class UserTest {


    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun showUser(){
        userRepository.getUserAll().forEach {
            println(it)
        }
    }


    @Test
    fun add(){
        userRepository.add(UserVO(
            name = "name"+ Random.nextInt(),
            address = Random.nextDouble().toString(),
            gender = if (Random.nextBoolean()) Gender.Male else Gender.Female,
            password = "1234",
            birthday = LocalDate(2008,1,1)
        ))
    }
}