package cn.allin.utils


inline infix fun <T, reified R> Array<T>.map(transform: (T) -> R) = Array(size) { transform(this[it]) }

inline infix fun <T, reified R> Array<T>.mapIndexed(transform: (index: Int, T) -> R) =
    Array(size) { transform(it, this[it]) }

inline infix fun <T, reified R> List<T>.mapToArray(transform: (T) -> R) = Array(size) { transform(this[it]) }

inline fun <T, reified R> List<T>.mapToArrayIndexed(transform: (index: Int, T) -> R) =
    Array(size) { transform(it, this[it]) }

@Suppress("UNCHECKED_CAST")
inline fun <T> Iterator<Any>.nextType(): T = next() as T