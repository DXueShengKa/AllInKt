package cn.allin.test

import cn.allin.Application
import cn.allin.exposed.UserRepository
import cn.allin.exposed.entity.User
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.random.Random
import kotlin.random.nextUInt

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
class UserTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun user(){
        userRepository.getUserAll().forEach {
            println(it)
        }

    }

    @Test
    fun add(){

        repeat(10){
            userRepository.add(
                UserVO(
                    age = Random.nextInt().toUByte(),
                    name = Random.nextLong().toString()
                )
            )
        }

    }
}