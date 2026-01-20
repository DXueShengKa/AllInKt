package cn.allin.utils

import androidx.collection.IntList
import androidx.collection.LongList
import androidx.collection.MutableIntIntMap
import androidx.collection.MutableLongLongMap
import androidx.collection.intListOf
import androidx.collection.longListOf
import androidx.collection.mutableIntListOf
import androidx.collection.mutableLongListOf

inline infix fun <T, reified R> Array<T>.map(transform: (T) -> R) = Array(size) { transform(this[it]) }

inline infix fun <T, reified R> Array<T>.mapIndexed(transform: (index: Int, T) -> R) = Array(size) { transform(it, this[it]) }

inline infix fun <T, reified R> List<T>.mapToArray(transform: (T) -> R) = Array(size) { transform(this[it]) }

inline infix fun <T> List<T>.mapToIntArray(transform: (T) -> Int) = IntArray(size) { transform(this[it]) }

inline fun <T, reified R> List<T>.mapToArrayIndexed(transform: (index: Int, T) -> R) = Array(size) { transform(it, this[it]) }

@Suppress("UNCHECKED_CAST")
fun <T> Iterator<Any>.nextType(): T = next() as T

fun <T> Sequence<T>.toLongList(transform: (T) -> Long): LongList {
    val it = iterator()
    if (!it.hasNext()) {
        return longListOf()
    }
    val element = it.next()
    if (!it.hasNext()) {
        return longListOf(transform(element))
    }

    val dst = mutableLongListOf()
    dst.add(transform(element))
    while (it.hasNext()) dst.add(transform(it.next()))
    return dst
}

fun LongList.toArray(): LongArray = LongArray(size) { this[it] }

fun <T> Sequence<T>.toIntList(transform: (T) -> Int): IntList {
    val it = iterator()
    if (!it.hasNext()) {
        return intListOf()
    }
    val element = it.next()
    if (!it.hasNext()) {
        return intListOf(transform(element))
    }

    val dst = mutableIntListOf()
    dst.add(transform(element))
    while (it.hasNext()) dst.add(transform(it.next()))
    return dst
}

fun intIntMapKvOf(vararg kevValue: Int): MutableIntIntMap {
    if (kevValue.size % 2 != 0) {
        error("kevValue 必须是成对的")
    }

    return MutableIntIntMap(kevValue.size / 2).apply {
        var i = 0
        while (i < kevValue.size) {
            set(kevValue[i], kevValue[i + 1])
            i += 2
        }
    }
}

fun longLongMapKvOf(vararg kevValue: Long): MutableLongLongMap {
    if (kevValue.size % 2 != 0) {
        error("kevValue 必须是成对的")
    }

    return MutableLongLongMap(kevValue.size / 2).apply {
        var i = 0
        while (i < kevValue.size) {
            set(kevValue[i], kevValue[i + 1])
            i += 2
        }
    }
}

fun <T> MutableList<T>.swap(
    a: Int,
    b: Int,
) {
    val tmp = get(a)
    set(a, get(b))
    set(b, tmp)
}
