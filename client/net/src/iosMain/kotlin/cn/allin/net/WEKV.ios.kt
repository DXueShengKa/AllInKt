package cn.allin.net

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.io.files.SystemPathSeparator
import okio.Path.Companion.toPath

actual fun WEKV.initialize(context: Any?) {

    weKv = PreferenceDataStoreFactory.createWithPath {
        "${context}${SystemPathSeparator}weKv.preferences_pb".toPath()
    }

//    userDataStore = DataStoreFactory.create(
//        storage = OkioStorage(
//            FileSystem.SYSTEM,
//            serializer = object : OkioSerializer<User> {
//                override val defaultValue: User
//                    get() = User()
//
//                override suspend fun readFrom(source: BufferedSource): User =
//                    ProtoBuf.decodeFromByteArray(source.readByteArray())
//
//
//                override suspend fun writeTo(t: User, sink: BufferedSink) {
//                    sink.write(ProtoBuf.encodeToByteArray(t))
//                }
//            },
//            producePath = {
//                "${context}${SystemPathSeparator}userData".toPath()
//            }
//        ),
//        corruptionHandler = null,
//        migrations = listOf(),
//        scope = CoroutineScope(Dispatchers.IO)
//    )
}
