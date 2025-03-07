package cn.allin.net

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
actual fun WEKV.initialize(context: Any?) {
    weKv = PreferenceDataStoreFactory.create {
        File(context.toString(), "weKv.preferences_pb")
    }

//    userDataStore = DataStoreFactory.create(
//        serializer = object : Serializer<com.zanbaike.wepedias.data.entitys.User> {
//            override val defaultValue: com.zanbaike.wepedias.data.entitys.User
//                get() = com.zanbaike.wepedias.data.entitys.User()
//
//            override suspend fun readFrom(input: InputStream): com.zanbaike.wepedias.data.entitys.User =
//                ProtoBuf.decodeFromByteArray(input.readBytes())
//
//
//            override suspend fun writeTo(t: com.zanbaike.wepedias.data.entitys.User, output: OutputStream) {
//                output.write(ProtoBuf.encodeToByteArray(t))
//            }
//        }
//    ) {
//        File(context.toString(), "userData")
//    }
}
