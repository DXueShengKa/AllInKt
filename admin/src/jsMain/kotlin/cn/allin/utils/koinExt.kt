package cn.allin.utils

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.stopKoin
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatformTools
import react.ChildrenBuilder
import react.ReactDsl
import react.createContext
import react.use
import react.useEffectWithCleanup

fun useKoinApplication(koinApplication: KoinApplication): Koin {
    val koin = useRefInit { koinApplication.koin }

    useEffectWithCleanup(koinApplication) {
        onCleanup {
            stopKoin()
        }
    }

    return koin
}

fun useKoinScope(scope: Scope): Scope {
    useEffectWithCleanup(scope) {
        onCleanup {
            if (!scope.isRoot && !scope.closed)
                scope.close()
        }
    }
    return scope
}

inline fun <reified T : Any> useInject(): T {
    return useRefInit { getKoin().get<T>() }
}


val KoinContext = createContext<Koin>()

val KoinScopeContext = createContext<Scope>()


fun getKoin(): Koin {
    val koin = use(KoinContext)
    return koin ?: KoinPlatformTools.defaultContext().get()
}


@OptIn(KoinInternalApi::class)
fun currentKoinScope(): Scope {
    val koinScope = use(KoinScopeContext)
    return koinScope ?: KoinPlatformTools.defaultContext().get().let {
        it.logger.info("没找到当前KoinScope")
        it.scopeRegistry.rootScope
    }
}


fun ChildrenBuilder.KoinFC(koin: Koin, scope: Scope, f: @ReactDsl ChildrenBuilder.() -> Unit) {
    KoinContext.Provider {
        value = koin
        KoinScopeContext.Provider {
            value = scope
            f()
        }
    }
}

@OptIn(KoinInternalApi::class)
fun ChildrenBuilder.KoinFC(koin: Koin, f:@ReactDsl ChildrenBuilder.() -> Unit) {
    KoinFC(koin, koin.scopeRegistry.rootScope, f)
}
