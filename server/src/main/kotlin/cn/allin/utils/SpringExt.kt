package cn.allin.utils

import cn.allin.config.UserRole
import cn.allin.vo.PageVO
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.babyfish.jimmer.Page
import org.springframework.cache.Cache
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.reflect.jvm.jvmName

val DefaultVirtualExecutor: ExecutorService = Executors.newThreadPerTaskExecutor(
    Thread.ofVirtual().name("devt#").factory()
)

val DispatchersVirtual = DefaultVirtualExecutor.asCoroutineDispatcher()


val Authentication.id: Int
    get() = principal as Int


typealias SpringUser = org.springframework.security.core.userdetails.User


val UserDetails.id: Int
    get() = password.toInt()


inline fun <reified T> Cache.getValue(key: Any): T? {
    return this.get(key, T::class.java)
}


//fun newAuthenticationToken(id: Int, password: String) =
//    UsernamePasswordAuthenticationToken(id, password)
//
//
//fun newAuthenticationToken(id: Int, password: String, authorities: Collection<GrantedAuthority>) =
//    UsernamePasswordAuthenticationToken(id, password, authorities)


//fun newAuthenticationToken(id: Long, password: String) =
//    UsernamePasswordAuthenticationToken(id, password)


fun newAuthenticationToken(id: Long, password: String, authorities: List<UserRole>) =
    UserAuthenticationToken(id, password, authorities)


private object UserAuthenticationSerializable : KSerializer<UserAuthenticationToken> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(UserAuthenticationToken::class.jvmName) {
        element<Long>("id")
        element<String>("username")
        element<IntArray>("roles")
    }

    override fun serialize(encoder: Encoder, value: UserAuthenticationToken) {
        encoder.encodeLong(value.id)
        encoder.encodeString(value.username)
        val va: IntArray = value.roles.mapToIntArray { it.ordinal }
        encoder.encodeSerializableValue(IntArraySerializer(), va)
    }

    override fun deserialize(decoder: Decoder): UserAuthenticationToken {
        return UserAuthenticationToken(
            decoder.decodeLong(),
            decoder.decodeString(),
            decoder.decodeSerializableValue(IntArraySerializer()).map { UserRole.entries[it] }
        )
    }
}


@Serializable(UserAuthenticationSerializable::class)
class UserAuthenticationToken(
    val id: Long,
    val username: String,
    val roles: List<UserRole>,
) : AbstractAuthenticationToken(roles) {

    init {
        isAuthenticated = true
    }

    override fun getPrincipal(): Any? {
        return id
    }

    override fun getCredentials(): Any? {
        return username
    }

}


fun createTriggerUpdateTimestamp(tableName: String) =
    """
         create trigger "${tableName}_update_timestamp"
             before insert or update
             on "$tableName"
             for each row
         execute procedure update_timestamp_column();
    """.trimIndent()


fun <T,R> Page<T>.toPageVO(transform: (T) -> R): PageVO<R> {
    return PageVO(
        rows = this.rows.map(transform),
        totalRow = this.totalRowCount.toInt(),
        totalPage = this.totalPageCount.toInt()
    )
}
