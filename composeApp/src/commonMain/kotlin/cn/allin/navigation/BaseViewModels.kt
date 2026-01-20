package com.logic.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.request.ErrorResult
import com.logic.navigation.NavRoute
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<R : NavRoute<*, *>>(
    private val composeBaseState: ComposeBaseState,
) : ViewModel() {
    companion object : ViewModel()

    protected lateinit var route: R
        private set

    @Suppress("UNCHECKED_CAST")
    internal fun setRoute(any: Any) {
        route = any as R
    }

    private val errorHandler = ErrorHandler(composeBaseState)

    /**
     * 带有加载弹窗的启动协程
     * @see launch
     */
    fun launchLoading(
        showError: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        context: CoroutineContext? = null,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        errorHandler.showError = showError
        errorHandler.onError = onError
        composeBaseState.showLoading()
        return viewModelScope.launch(context?.also { it + errorHandler } ?: errorHandler) {
            block()
            composeBaseState.dismissLoading()
        }
    }

    /**
     * vm默认行为的协程
     *
     * @param showError 是否把错误信息显示出来
     * @param onError 错误信息处理，在[block]中抛出的异常在此处理
     * @param context 协程上下文
     */
    fun launch(
        showError: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        context: CoroutineContext? = null,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        errorHandler.showError = showError
        errorHandler.onError = onError
        return viewModelScope.launch(
            context?.also { it + errorHandler } ?: errorHandler,
            block = block,
        )
    }

    /**
     * 附带默认处理的CoroutineScope,
     * @see launch
     */
    fun viewModelScopeWithError(
        showError: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
    ): CoroutineScope {
        errorHandler.showError = showError
        errorHandler.onError = onError
        return viewModelScope + errorHandler
    }

    private class ErrorHandler(
        private val composeState: ComposeBaseState,
    ) : AbstractCoroutineContextElement(CoroutineExceptionHandler.Key),
        CoroutineExceptionHandler {
        var showError = true
        var onError: ((Throwable) -> Unit)? = null

        override fun handleException(
            context: CoroutineContext,
            exception: Throwable,
        ) {
//            onError?.also {
//                try {
//                    it.invoke(exception)
//                    composeState.dismissLoading()
//                    return
//                } catch (_: Throwable) {
//                }
//            }
//            val errorResult = ErrorResult()
//            when (exception) {
//
//                is ApiException -> {
//                    errorResult.code = exception.code
//                    errorResult.msg = exception.msg
//                }
//
// //todo                is HttpException -> {
// //                    errorResult.code = exception.code()
// //                }
// //
//                is GrpcException -> {
//                    errorResult.msg = exception.message
//                }
//
//                else -> {
//                }
//            }
//            errorResult.showError = showError
//            composeState.dismissLoading()
//            composeState.errorData(errorResult)
        }
    }

    fun requestError(
        exception: Throwable,
        showError: Boolean = true,
    ) {
    }
}
