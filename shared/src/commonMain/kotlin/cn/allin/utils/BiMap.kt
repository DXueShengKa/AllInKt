package cn.allin.utils

class BiMap<K, V>(
    private val kMap: MutableMap<K, V>,
    private val vMap: MutableMap<V, K>,
) : MutableMap<K, V> {
    constructor() : this(HashMap(), HashMap())

    override val keys: MutableSet<K>
        get() = kMap.keys

    override val values: MutableCollection<V>
        get() = vMap.keys

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = kMap.entries

    override fun put(
        key: K,
        value: V,
    ): V {
        if (containsKey(key)) {
            val v = kMap[key]
            if (v == value) return v

            kMap[key] = value
            // 删除原来的value再存v k map
            vMap.remove(v)
            vMap[value] = key
            return value
        }
        kMap[key] = value
        vMap[value] = key
        return value
    }

    override fun remove(key: K): V? {
        val v = kMap.remove(key)
        vMap.remove(v ?: return null)
        return v
    }

    fun removeKeyByValue(value: V?): K? {
        val k = vMap.remove(value ?: return null)
        kMap.remove(k ?: return null)
        return k
    }

    override fun putAll(from: Map<out K, V>) {
        kMap.putAll(from)
        from.forEach {
            vMap[it.value] = it.key
        }
    }

    override fun clear() {
        kMap.clear()
        vMap.clear()
    }

    override val size: Int
        get() = kMap.size

    override fun isEmpty(): Boolean = kMap.isEmpty() && vMap.isEmpty()

    override fun containsKey(key: K): Boolean = kMap.containsKey(key)

    override fun containsValue(value: V): Boolean = vMap.containsKey(value)

    override fun get(key: K): V? = kMap[key]

    fun getKevByValue(value: V): K? = vMap[value]
}
