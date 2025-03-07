package cn.allin.net

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore


actual fun WEKV.initialize(context: Any?) {
    if (context !is Context) error("必须传入 android.content.Context")
    weKv = context.weKv
//    userDataStore = context.userDataStore

//    if (BuildConfig.DEBUG)
//        Log.d("WEKV", "初始化preferencesDataStore")
}


private val Context.weKv by preferencesDataStore(name = "weKv")

//@OptIn(ExperimentalSerializationApi::class)
//private val Context.userDataStore: DataStore<User> by dataStore(
//    fileName = "userData",
//    corruptionHandler = ReplaceFileCorruptionHandler<User> {
//        User()
//    },
//    serializer = object : Serializer<User> {
//        override val defaultValue: User
//            get() = User()
//
//        override suspend fun readFrom(input: InputStream): User = ProtoBuf.decodeFromByteArray(input.readBytes())
//
//
//        override suspend fun writeTo(t: User, output: OutputStream) {
//            output.write(ProtoBuf.encodeToByteArray(t))
//        }
//    }
//)
