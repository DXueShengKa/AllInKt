package com.logic.navigation

import kotlinx.coroutines.channels.Channel

class ResultEventBus<Key> {
    private val channelMap: MutableMap<Key?, Channel<Any?>> = hashMapOf()

    fun getResultChannel(resultKey: Key) = channelMap[resultKey]

    fun sendResult(
        resultKey: Key?,
        result: Any?,
    ) {
        val channel =
            channelMap[resultKey] ?: Channel<Any?>(Channel.CONFLATED).also {
                channelMap[resultKey] = it
            }
        channel.trySend(result)
    }

    fun removeResult(resultKey: Key) {
        channelMap.remove(resultKey)?.close()
    }
}
