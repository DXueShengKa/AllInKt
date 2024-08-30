package androidx.navigation.react

class WeakReference<T : Any>(reference: T) {
    private var reference: T? = reference
    fun get(): T? = reference
    fun clear() {
        reference = null
    }
}