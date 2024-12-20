@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.compose.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentDialog
import androidx.activity.addCallback
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewRootForInspector
import androidx.compose.ui.semantics.dialog
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.compose.ui.window.isFlagSecureEnabled
import androidx.compose.ui.window.shouldApplySecureFlag
import androidx.core.view.WindowCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.util.UUID

/**
 * 底部弹窗
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    background: Color,
    shape: Shape,
    properties: DialogProperties,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val composition = rememberCompositionContext()
    val dialogId = rememberSaveable { UUID.randomUUID() }
    val dialog = remember(view, density) {
        DialogWrapper(
            onDismissRequest,
            properties,
            view,
            layoutDirection,
            density,
            dialogId
        ).apply {
            setContent(composition) {
                DialogLayout(
                    Modifier
                        .offset {
                            IntOffset(x = 0, y = swipeableState.offset.toInt())
                        }
                        .anchoredDraggable(swipeableState, Orientation.Vertical)
                        .nestedScroll(nestedScroll)
                        .background(background, shape)
                        .navigationBarsPadding()
                        .semantics { dialog() },
                    { height ->

                        if (swipeableState.anchors.size < 2) {
                            swipeableState.updateAnchors(DraggableAnchors {
                                Close at 0f
                                Open at height / 2f
                            })
                        }
                    },
                    content
                )
                if (swipeableState.currentValue == Open) {
                    onDismissRequest()
                }
            }
        }
    }

    DisposableEffect(dialog) {
        dialog.show()

        onDispose {
            dialog.dismiss()
            dialog.disposeComposition()
        }
    }

    SideEffect {
        dialog.updateParameters(
            onDismissRequest = onDismissRequest,
            properties = properties,
            layoutDirection = layoutDirection
        )
    }
}

private const val Close: Byte = 0
private const val Open: Byte = 1


@Suppress("ViewConstructor")
private class DialogLayout(
    context: Context,
    override val window: Window
) : AbstractComposeView(context), DialogWindowProvider {

    private var content: (@Composable () -> Unit)? = null

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    fun setContent(parent: CompositionContext, content: @Composable () -> Unit) {
        setParentCompositionContext(parent)
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
        createComposition()
    }

    @Composable
    override fun Content() {
        content?.invoke()
    }
}

@SuppressLint("PrivateResource")
@OptIn(ExperimentalFoundationApi::class)
private class DialogWrapper(
    private var onDismissRequest: () -> Unit,
    private var properties: DialogProperties,
    private val composeView: View,
    layoutDirection: LayoutDirection,
    density: Density,
    dialogId: UUID
) : ComponentDialog(
    /**
     * [Window.setClipToOutline] is only available from 22+, but the style attribute exists on 21.
     * So use a wrapped context that sets this attribute for compatibility back to 21.
     */
    ContextThemeWrapper(
        composeView.context,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || properties.decorFitsSystemWindows)
            androidx.compose.ui.R.style.DialogWindowTheme
        else
            androidx.compose.ui.R.style.FloatingDialogWindowTheme
    )
), ViewRootForInspector {

    private val dialogLayout: DialogLayout

    // On systems older than Android S, there is a bug in the surface insets matrix math used by
    // elevation, so high values of maxSupportedElevation break accessibility services: b/232788477.
    private val maxSupportedElevation = 8.dp

    override val subCompositionView: AbstractComposeView get() = dialogLayout

    private val defaultSoftInputMode: Int

    init {
        val window = window ?: error("Dialog has no window")
        defaultSoftInputMode =
            window.attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        WindowCompat.setDecorFitsSystemWindows(window, properties.decorFitsSystemWindows)


        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.BottomSheetAnimation)
        window.decorView.setPadding(0, 0, 0, 0)
        window.attributes = window.attributes.also {
            it.width = WindowManager.LayoutParams.MATCH_PARENT
            it.horizontalMargin = 0f
        }

        dialogLayout = DialogLayout(context, window).apply {
            // Set unique id for AbstractComposeView. This allows state restoration for the state
            // defined inside the Dialog via rememberSaveable()
            setTag(androidx.compose.ui.R.id.compose_view_saveable_id_tag, "Dialog:$dialogId")
            // Enable children to draw their shadow by not clipping them
            clipChildren = false
            // Allocate space for elevation
            with(density) { elevation = maxSupportedElevation.toPx() }
            // Simple outline to force window manager to allocate space for shadow.
            // Note that the outline affects clickable area for the dismiss listener. In case of
            // shapes like circle the area for dismiss might be to small (rectangular outline
            // consuming clicks outside of the circle).
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, result: Outline) {
                    result.setRect(0, 0, view.width, view.height)
                    // We set alpha to 0 to hide the view's shadow and let the composable to draw
                    // its own shadow. This still enables us to get the extra space needed in the
                    // surface.
                    result.alpha = 0f
                }
            }

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        /**
         * Disables clipping for [this] and all its descendant [ViewGroup]s until we reach a
         * [DialogLayout] (the [ViewGroup] containing the Compose hierarchy).
         */
        fun ViewGroup.disableClipping() {
            clipChildren = false
            if (this is DialogLayout) return
            for (i in 0 until childCount) {
                (getChildAt(i) as? ViewGroup)?.disableClipping()
            }
        }

        // Turn of all clipping so shadows can be drawn outside the window
        (window.decorView as? ViewGroup)?.disableClipping()
        setContentView(dialogLayout)
        dialogLayout.setViewTreeLifecycleOwner(composeView.findViewTreeLifecycleOwner())
        dialogLayout.setViewTreeViewModelStoreOwner(composeView.findViewTreeViewModelStoreOwner())
        dialogLayout.setViewTreeSavedStateRegistryOwner(
            composeView.findViewTreeSavedStateRegistryOwner()
        )

        // Initial setup
        updateParameters(onDismissRequest, properties, layoutDirection)

        // Due to how the onDismissRequest callback works
        // (it enforces a just-in-time decision on whether to update the state to hide the dialog)
        // we need to unconditionally add a callback here that is always enabled,
        // meaning we'll never get a system UI controlled predictive back animation
        // for these dialogs
        onBackPressedDispatcher.addCallback(this) {
            if (properties.dismissOnBackPress) {
                onDismissRequest()
            }
        }
    }

    private fun setLayoutDirection(layoutDirection: LayoutDirection) {
        dialogLayout.layoutDirection = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
    }

    fun setContent(parentComposition: CompositionContext, children: @Composable () -> Unit) {
        dialogLayout.setContent(parentComposition, children)
    }

    private fun setSecurePolicy(securePolicy: SecureFlagPolicy) {
        val secureFlagEnabled =
            securePolicy.shouldApplySecureFlag(composeView.isFlagSecureEnabled())
        window!!.setFlags(
            if (secureFlagEnabled) {
                WindowManager.LayoutParams.FLAG_SECURE
            } else {
                WindowManager.LayoutParams.FLAG_SECURE.inv()
            },
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun updateParameters(
        onDismissRequest: () -> Unit,
        properties: DialogProperties,
        layoutDirection: LayoutDirection
    ) {
        this.onDismissRequest = onDismissRequest
        this.properties = properties
        setSecurePolicy(properties.securePolicy)
        setLayoutDirection(layoutDirection)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (properties.decorFitsSystemWindows) {
                window?.setSoftInputMode(defaultSoftInputMode)
            } else {
                @Suppress("DEPRECATION")
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }
    }

    fun disposeComposition() {
        dialogLayout.disposeComposition()
    }


    val swipeableState: AnchoredDraggableState<Byte> = AnchoredDraggableState(
        initialValue = Close,
        anchors = DraggableAnchors {
            Close at 0f
        },
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { 20f },
        snapAnimationSpec = spring(),
        decayAnimationSpec = exponentialDecay()
    )

    val nestedScroll = ConsumeSwipeNestedScrollConnection(swipeableState, Orientation.Vertical)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        if (result && properties.dismissOnClickOutside) {
            onDismissRequest()
        }

        return result
    }

    override fun cancel() {
        // Prevents the dialog from dismissing itself
        return
    }
}

@Composable
private fun DialogLayout(
    modifier: Modifier = Modifier,
    onHeight: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.fastMap { it.measure(constraints) }
        val width = placeables.fastMaxBy { it.width }?.width ?: constraints.minWidth
        val height = placeables.fastMaxBy { it.height }?.height ?: constraints.minHeight
        onHeight(height)
        layout(width, height) {
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}