package cn.allin.test

import cn.allin.Application
import cn.allin.vo.UserVO
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import kotlin.test.Test


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
class CacheTest {


    @Autowired
    lateinit var reactiveRedisTemplateByte: ReactiveRedisTemplate<String, ByteArray>


    @Test
    fun showAll(){
        reactiveRedisTemplateByte.opsForValue().get("cacheTest").subscribe {
            println(ProtoBuf.decodeFromByteArray<UserVO>(it))
        }
    }


    @Test
    fun cacheOne(){
        reactiveRedisTemplateByte.opsForValue()
            .set("cacheTest", ProtoBuf.encodeToByteArray(UserVO(name = "aaa", id = 222222)))
            .subscribe()

    }

}
