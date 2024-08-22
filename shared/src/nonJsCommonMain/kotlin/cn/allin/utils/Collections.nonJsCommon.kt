package cn.allin.utils

import androidx.collection.IntList
import androidx.collection.LongList
import androidx.collection.MutableIntIntMap
import androidx.collection.MutableLongLongMap
import androidx.collection.intListOf
import androidx.collection.longListOf
import androidx.collection.mutableIntListOf
import androidx.collection.mutableLongListOf


fun <T> Sequence<T>.toLongList(transform: (T) -> Long): LongList {
    val it = iterator()
    if (!it.hasNext())
        return longListOf()
    val element = it.next()
    if (!it.hasNext())
        return longListOf(transform(element))

    val dst = mutableLongListOf()
    dst.add(transform(element))
    while (it.hasNext()) dst.add(transform(it.next()))
    return dst
}


@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
fun LongList.toArray(): LongArray {
    return content
}


fun <T> Sequence<T>.toIntList(transform: (T) -> Int): IntList {
    val it = iterator()
    if (!it.hasNext())
        return intListOf()
    val element = it.next()
    if (!it.hasNext())
        return intListOf(transform(element))

    val dst = mutableIntListOf()
    dst.add(transform(element))
    while (it.hasNext()) dst.add(transform(it.next()))
    return dst
}

fun intIntMapKvOf(vararg kevValue: Int): MutableIntIntMap {
    if (kevValue.size % 2 != 0)
        error("kevValue 必须是成对的")

    return MutableIntIntMap(kevValue.size / 2).apply {
        var i = 0
        while (i < kevValue.size) {
            set(kevValue[i], kevValue[i + 1])
            i += 2
        }
    }
}

fun longLongMapKvOf(vararg kevValue: Long): MutableLongLongMap {
    if (kevValue.size % 2 != 0)
        error("kevValue 必须是成对的")

    return MutableLongLongMap(kevValue.size / 2).apply {
        var i = 0
        while (i < kevValue.size) {
            set(kevValue[i], kevValue[i + 1])
            i += 2
        }
    }
}