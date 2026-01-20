package com.logic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import kotlin.jvm.JvmStatic

/**
 * 页面嵌套显示
 * p页面下切换子页面1234

```mermaid
graph TD
subgraph route
p(parent) --> 1
p --> 2
p --> 3
p --> 4
end
```
 *
 */
class SubrouteSceneStrategy<T : Any> : SceneStrategy<T> {
    @Suppress("UNCHECKED_CAST")
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val last = entries.lastOrNull() ?: return null
        val parent = last.metadata[PARENT] ?: return null
        val i = entries.indexOfLast { parent == it.contentKey }

        return SubrouteScene(
            key = entries[i].contentKey as T,
            entries = entries,
            previousEntries = entries.subList(0, i),
        )
    }

    companion object {
        private const val PARENT = "parent"

        @JvmStatic
        fun parent(parent: Any): Map<String, Any> = mapOf(PARENT to parent)
    }
}

val LocalSubrouteEntry = compositionLocalOf<NavEntry<*>> { error("null SubrouteEntry") }

internal class SubrouteScene<T : Any>(
    override val key: T,
    override val entries: List<NavEntry<T>>,
    override val previousEntries: List<NavEntry<T>>,
) : Scene<T> {
    override val content: @Composable (() -> Unit) = {
        CompositionLocalProvider(
            LocalSubrouteEntry provides entries[entries.lastIndex],
        ) {
            entries[0].Content()
        }
    }
}
