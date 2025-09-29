package com.compose.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.allin.utils.getDate
import cn.allin.utils.isLeapYear
import cn.allin.utils.length
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlin.time.Clock


//@Preview
@Composable
fun WheelP() {
    DateWheel(
        remember {
            DateWheelState(
                Clock.System.now().getDate().minus(10, DateTimeUnit.YEAR),
                Clock.System.now().getDate()
            )
        },
        Modifier.size(300.dp),
        selectedBackgroundColor = MaterialTheme.colorScheme.error
    )
}

/**
 * 日期选择器
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateWheel(
    dateWheel: DateWheelState,
    modifier: Modifier,
    itemHeight: Dp = 34.dp,
    selectedItemHeight: Dp = 60.dp,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = 0.4f),
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    LaunchedEffect(Unit) {
        dateWheel.coroutineScope = this
        dateWheel.listening(this)
    }

    Row(modifier) {

        val wheelModifier = Modifier
            .fillMaxHeight()
            .weight(1f)

        val selectedContent: @Composable (Dp, String) -> Unit = { height, str ->
            Text(
                str,
                Modifier
                    .height(height)
                    .wrapContentHeight(),
                color = selectedContentColor,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        val unselectedContent: @Composable (Dp, String) -> Unit = { height, str ->
            Text(
                str,
                Modifier
                    .height(height)
                    .wrapContentHeight(),
                color = unselectedContentColor,
            )
        }

        Wheel(
            dateWheel.yearArrayUI,
            dateWheel.yearSwipe,
            wheelModifier,
            itemHeight,
            selectedItemHeight,
            selectedContent = selectedContent,
            unselectedContent = unselectedContent,
        ) { top, bottom ->
            val w = bottom - top
            val path = Path().apply {
                arcTo(
                    Rect(0f, top, w, bottom),
                    90f,
                    180f,
                    false
                )
                val x = size.width
                lineTo(x, top)
                lineTo(x, bottom)
                close()
            }
            drawPath(path, selectedBackgroundColor)
        }

        Wheel(
            dateWheel.monthArrayUI,
            dateWheel.monthSwipe,
            wheelModifier,
            itemHeight,
            selectedItemHeight,
            selectedContent = selectedContent,
            unselectedContent = unselectedContent,
        ) { top, bottom ->
            val width = size.width
            val bgHeight = bottom - top
            drawRect(selectedBackgroundColor, Offset(0f, top), Size(width, bgHeight))
            val lineLength = bgHeight / 2
            val y = (size.height - bgHeight / 2) / 2
            drawLine(Color.Black, Offset(1f, y), Offset(1f, y + lineLength))
            drawLine(Color.Black, Offset(width - 1, y), Offset(width - 1, y + lineLength))
        }

        if (dateWheel.showDay) Wheel(
            dateWheel.dayArrayUI,
            dateWheel.daySwipe,
            wheelModifier,
            itemHeight,
            selectedItemHeight,
            selectedContent = selectedContent,
            unselectedContent = unselectedContent,
        ) { top, bottom ->
            val w = bottom - top
            val path = Path().apply {
                moveTo(0f, top)
                val width = size.width
                lineTo(width - (w / 2), top)
                arcTo(
                    Rect(width - w, top, width, bottom),
                    270f,
                    180f,
                    false
                )
                lineTo(0f, bottom)
                close()
            }
            drawPath(path, selectedBackgroundColor)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Stable
class DateWheelState(
    private val start: LocalDate,
    private val end: LocalDate,
    //是否显示“日”选项
    val showDay: Boolean = true,
    default: LocalDate = start
) {
    companion object {
        private const val YEAR = 0
        private const val MONTH = 1
        private const val DAY = 2
    }

    internal var coroutineScope: CoroutineScope? = null

    private val currentDateArray = arrayOf(default.year, default.month.number, default.day)

    var currentDate: LocalDate
        get() = LocalDate(currentYear, currentMonth, currentDay)
        set(value) {
            currentDateArray[YEAR] = value.year
            currentDateArray[MONTH] = value.month.number
            currentDateArray[DAY] = value.day

            coroutineScope.let {
                it ?: throw NullPointerException("请在ui渲染后再设置当前选择的时间")
            }.launch {
                yearSwipe.animateTo(yearArray.indexOf(currentDateArray[YEAR]))

                delay(200)
                monthSwipe.animateTo(monthArray.indexOf(value.month.number))

                delay(200)
                daySwipe.animateTo(dayArray.indexOf(value.day))
            }
        }

    val currentYear: Int get() = currentDateArray[YEAR]
    val currentMonth: Int get() = currentDateArray[MONTH]
    val currentDay: Int get() = currentDateArray[DAY]


    private val yearArray = start.run {
        var y = year
        IntArray(end.year - year + 1) { y++ }
    }
    internal val yearArrayUI = Array(yearArray.size) { "${yearArray[it]}" }


    private var monthArray = IntArray(0)
        set(value) {
            monthArrayUI = Array(value.size) { "${value[it]}" }
            field = value
        }
    internal var monthArrayUI by mutableStateOf(Array(0) { "" })

    private fun createMonthArray(year: Int): IntArray {
        if (start.year == end.year) {
            var m = start.month.number
            return IntArray(end.month.number - start.month.number + 1) { m++ }
        }
        var m = if (year == start.year) start.month.number else 1
        val size = if (year == end.year) end.month.number else 12

        return IntArray(size - m + 1) { m++ }
    }


    private var dayArray = IntArray(0)
        set(value) {
            dayArrayUI = Array(value.size) { "${value[it]}" }
            field = value
        }
    internal var dayArrayUI by mutableStateOf(Array(0) { "" })

    private fun createDayArray(year: Int, month: Month): IntArray {
        val size = if (end.year == year && end.month == month) end.day else month.length(isLeapYear(year))
        var day = if (start.year == year && start.month == month) start.day else 1
        return IntArray(size - if (day == 1) 0 else day - 1) { day++ }
    }

    init {
        monthArray = createMonthArray(start.year)
        dayArray = createDayArray(start.year, start.month)
    }

    internal val yearSwipe = createAnchoredDraggable(yearArray.indexOf(currentDateArray[YEAR]))
    internal val monthSwipe = createAnchoredDraggable(monthArray.indexOf(currentDateArray[MONTH]))
    internal val daySwipe = createAnchoredDraggable(dayArray.indexOf(currentDateArray[DAY]))


    internal suspend fun listening(scope: CoroutineScope) {

        //监听年的变化
        var yearIndex = 0
        scope.launch {
            snapshotFlow {
                yearSwipe.currentValue
            }.collect { y ->
                yearIndex = y

                monthArray = createMonthArray(yearArray[y])
                dayArray = createDayArray(yearArray[y], Month(monthArray[0]))
                monthSwipe.snapTo(0)

                currentDateArray[YEAR] = yearArray[y]
                currentDateArray[MONTH] = monthArray[0]
                currentDateArray[DAY] = dayArray[0]
            }
        }

        //监听日的变化
        if (showDay) scope.launch {
            snapshotFlow {
                daySwipe.currentValue
            }.collect { d ->
                currentDateArray[DAY] = dayArray[d]
            }
        }

        //监听月的变化
        snapshotFlow {
            monthSwipe.currentValue
        }.collect { m ->
            if (showDay) {
                dayArray = createDayArray(yearArray[yearIndex], Month(monthArray[m]))
                daySwipe.snapTo(0)
                currentDateArray[DAY] = dayArray[0]
            }
            currentDateArray[MONTH] = monthArray[m]
        }
    }

}


/**
 * @see AnchoredDraggableState
 */
@ExperimentalFoundationApi
fun <T> createAnchoredDraggable(
    initialValue: T,
    positionalThreshold: (totalDistance: Float) -> Float = { distance: Float -> distance * 0.5f },
    velocityThreshold: () -> Float = { 0f },
    animationSpec: AnimationSpec<Float> = spring(),
    confirmValueChange: (newValue: T) -> Boolean = { true }
): AnchoredDraggableState<T> = AnchoredDraggableState(
    initialValue,
    positionalThreshold,
    velocityThreshold,
    animationSpec,
    exponentialDecay(),
    confirmValueChange
)

/**
 * @see AnchoredDraggableState
 */
@ExperimentalFoundationApi
fun <T> createAnchoredDraggable(
    initialValue: T,
    anchors: DraggableAnchors<T>,
    positionalThreshold: (totalDistance: Float) -> Float = { distance: Float -> distance * 0.5f },
    velocityThreshold: () -> Float = { 0f },
    animationSpec: AnimationSpec<Float> = spring(),
    confirmValueChange: (newValue: T) -> Boolean = { true }
): AnchoredDraggableState<T> = AnchoredDraggableState(
    initialValue,
    anchors,
    positionalThreshold,
    velocityThreshold,
    animationSpec,
    exponentialDecay(),
    confirmValueChange
)

/**
 * 滚轮选择器
 */
@ExperimentalFoundationApi
@Composable
fun Wheel(
    items: Array<String>,
    swipeableState: AnchoredDraggableState<Int>,
    modifier: Modifier,
    itemHeight: Dp = 40.dp,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor,
    maxLines: Int = Int.MAX_VALUE
) = Wheel(
    items = items,
    modifier = modifier,
    itemHeight = itemHeight,
    selectedItemHeight = itemHeight,
    swipeableState = swipeableState,
    selectedContent = { height, str ->
        Text(
            str,
            Modifier
                .height(height)
                .wrapContentHeight(),
            color = selectedContentColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines
        )
    },
    unselectedContent = { height, str ->
        Text(
            str,
            Modifier
                .height(height)
                .wrapContentHeight(),
            color = unselectedContentColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines
        )
    }
) { top, bottom ->
    drawLine(selectedContentColor, Offset(0f, top), Offset(size.width, top))
    drawLine(selectedContentColor, Offset(0f, bottom), Offset(size.width, bottom))
}

object NoFlingBehavior : FlingBehavior {
    override suspend fun ScrollScope.performFling(initialVelocity: Float) = 0f
}

/**
 *
 * [selectedContent] 被选中的组件，Dp：这个组件的高度， String：内容文本
 * [unselectedContent] 未选中的组件，Dp：这个组件的高度， String：内容文本
 * [drawSelected] 被选择区的背景绘制函数，第一个Float：y轴顶部位置，第二个个Float：y轴底部位置
 */
@ExperimentalFoundationApi
@Composable
fun Wheel(
    items: Array<String>,
    swipeableState: AnchoredDraggableState<Int>,
    modifier: Modifier,
    itemHeight: Dp = 40.dp,
    selectedItemHeight: Dp = 60.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    reverse: Boolean = true,
    selectedContent: @Composable (Dp, String) -> Unit,
    unselectedContent: @Composable (Dp, String) -> Unit,
    drawSelected: DrawScope.(Float, Float) -> Unit
) {
    val density = LocalDensity.current
    val wheelState = remember(swipeableState) {
        WheelState(itemHeight, selectedItemHeight, swipeableState, density)
    }
    wheelState.items = items

    LazyColumn(
        modifier
            .drawWithCache {
                val top = (size.height - wheelState.selectedItemHeightPx) / 2
                wheelState.spacerHeight = top.toDp()
                val bottom = top + wheelState.selectedItemHeightPx
                onDrawBehind {
                    drawSelected(top, bottom)
                }
            }
            .anchoredDraggable(
                state = swipeableState,
                orientation = Orientation.Vertical,
                reverseDirection = reverse
            ),
        wheelState.lazy,
        userScrollEnabled = false,
        reverseLayout = !reverse,
        flingBehavior = NoFlingBehavior,
        horizontalAlignment = horizontalAlignment
    ) {
        item(contentType = "Spacer") {
            Spacer(Modifier.height(wheelState.spacerHeight))
        }
        items(
            items.size,
            contentType = { wheelState }
        ) {
            val str = items[it]
            if (it == swipeableState.targetValue)
                selectedContent(selectedItemHeight, str)
            else
                unselectedContent(itemHeight, str)
        }
        item(contentType = "Spacer") {
            Spacer(Modifier.height(wheelState.spacerHeight))
        }
    }

    LaunchedEffect(wheelState) {

        delay(100)
        wheelState.lazy.animateScrollToItem(0)

        snapshotFlow {
            swipeableState.offset
        }.collect {
            wheelState.scroll(it)
        }
    }
}


@ExperimentalFoundationApi
@Stable
private class WheelState(
    val itemHeight: Dp,
    val selectedItemHeight: Dp,
    val anchoredDraggableState: AnchoredDraggableState<Int>,
    density: Density
) {
    val lazy = LazyListState()

    val itemHeightPx = with(density) { itemHeight.roundToPx() }
    val selectedItemHeightPx = with(density) { selectedItemHeight.roundToPx() }

    var spacerHeight by mutableStateOf(Dp.Hairline)


    var items: Array<String> = emptyArray()
        set(value) {
            if (value === field) return
            field = value

            val anchors = DraggableAnchors {
                repeat(value.size) {
                    it at itemHeightPx * it.toFloat()
                }
            }
            anchoredDraggableState.updateAnchors(anchors)
        }


    private var offset = 0f

    suspend fun scroll(swipe: Float) {
        if (swipe == 0f) {
            lazy.animateScrollToItem(0)
            offset = 0f
        } else {
            offset += lazy.scrollBy(swipe - offset)
        }
    }
}
