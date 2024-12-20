package com.compose.components

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Compose的全局浮动窗口
 * @param lifecycleOwner 为空的话需要手动控制[ComposeFloatWindow.lifecycleRegistry]生命周期
 */
class ComposeFloatWindow @JvmOverloads constructor(
    private val context: Context,
    lifecycleOwner: LifecycleOwner?,
    private val topPadding: Int = 0
) : ViewModelStoreOwner, SavedStateRegistryOwner {

    private val tag = "ComposeFloatWindow"

    private val defaultOffset = IntOffset(0,topPadding)

    private lateinit var windowManager: WindowManager

    private val wmParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        PixelFormat.TRANSPARENT
    ).also {
        it.gravity = Gravity.START or Gravity.TOP
    }

    private var view: View? = null

    var isShowing = false
        private set

    private var content: (@Composable (Modifier) -> Unit)? = null


    fun setContent(content: @Composable (Modifier) -> Unit) {
        this.content = content
    }

    private var coroutineScope: CoroutineScope? = null

    private val animatable = Animatable(defaultOffset, IntOffset.VectorConverter, label = tag)

    private var contentWidth = 0
    private var contentHeight = 0

    var windowWidth = 0
        private set

    var windowHeight = 0
        private set

    var enabled by mutableStateOf(true)

    fun show(){
        show(Settings.canDrawOverlays(context))
    }

    /**
     * @param canDrawOverlays 是否能够全局绘制
     */
    fun show(canDrawOverlays: Boolean) {

        if (view == null) {
            val metrics = context.resources.displayMetrics
            windowWidth = metrics.widthPixels
            windowHeight = metrics.heightPixels

            view = content?.let(::initView)
        }

        wmParams.type = if (canDrawOverlays)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else {
            WindowManager.LayoutParams.TYPE_APPLICATION
        }

        wmParams.x = 0
        wmParams.y = topPadding

        windowManager.addView(view, wmParams)

        isShowing = true
    }


    private fun initView(content: @Composable (Modifier) -> Unit) = ComposeView(context).also {
        setViewTree(it)
        it.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        it.setContent {
            LaunchedEffect(Unit) {
                coroutineScope = this
                snapshotFlow {
                    animatable.value
                }.collect { (x, y) ->
                    wmParams.x = x
                    wmParams.y = y
                    windowManager.updateViewLayout(view, wmParams)
                }
            }

            content(
                Modifier
                    .pointerInput(enabled) {
                        if (enabled) detectDragGestures(
                            onDragEnd = {
                                coroutineScope?.launch {
                                    animatable.snapTo(IntOffset(wmParams.x, wmParams.y))
                                    if (wmParams.x > (windowWidth - contentWidth) / 2) { //停靠右边
                                        animatable.animateTo(
                                            IntOffset(
                                                windowWidth - contentWidth,
                                                wmParams.y
                                            )
                                        )
                                    } else { //停靠左边
                                        animatable.animateTo(IntOffset(0, wmParams.y))
                                    }
                                }
                            }
                        ) { _, (x, y) ->
                            wmParams.x += x.toInt()
                            wmParams.y += y.toInt()
                            windowManager.updateViewLayout(view, wmParams)
                        }
                    }
                    .onSizeChanged { (width, height) ->
                        contentWidth = width
                        contentHeight = height

                        onSizeChanged?.invoke()
                    }
            )
        }
    }

    private var onSizeChanged: (() -> Unit)? = null

    fun toTop() {
        onSizeChanged = ::top
    }

    private fun top() {
        coroutineScope?.launch {
            animatable.animateTo(defaultOffset)
            onSizeChanged = null
        }
    }

    fun toCenter() {
        onSizeChanged = ::center
    }

    private fun center() {
        coroutineScope?.launch {

            animatable.animateTo(
                IntOffset(
                    x = windowWidth - contentWidth,
                    y = windowHeight - contentHeight
                ) / 2f
            )
            onSizeChanged = null
        }
    }

    fun hide() {
        windowManager.removeView(view)
        isShowing = false
    }

    private val _viewModelStore = ViewModelStore()

    override val viewModelStore = _viewModelStore

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    @JvmField
    val lifecycleRegistry = LifecycleRegistry(this)

    private val savedState: Bundle = Bundle()

    private val eventObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            windowManager = context.getSystemService(WindowManager::class.java)
            savedStateRegistryController.performRestore(savedState)
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            hide()
            viewModelStore.clear()
            savedStateRegistryController.performSave(savedState)
        }

        if (lifecycleOwner != null)
            lifecycleRegistry.handleLifecycleEvent(event)
    }

    init {
        if (lifecycleOwner != null) {
            lifecycleOwner.lifecycle.addObserver(eventObserver)
        } else {
            lifecycleRegistry.addObserver(eventObserver)
        }
    }

    override val savedStateRegistry: SavedStateRegistry = savedStateRegistryController.savedStateRegistry


    private fun setViewTree(composeView: View) {
        composeView.setViewTreeLifecycleOwner(this)
        composeView.setViewTreeViewModelStoreOwner(this)
        composeView.setViewTreeSavedStateRegistryOwner(this)
    }

    /**
     * 申请全局显示悬浮窗权限并且显示
     */
    fun showManageOverlay(registry: ActivityResultRegistry) {
        if (Settings.canDrawOverlays(context)) {
            show(true)
        } else registry.register(
            "ACTION_MANAGE_OVERLAY",
            ActivityResultContracts.StartActivityForResult()
        ) {
            show(Settings.canDrawOverlays(context))
        }.launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
        )
    }
}