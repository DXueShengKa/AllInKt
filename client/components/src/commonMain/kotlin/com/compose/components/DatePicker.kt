package com.compose.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cn.allin.utils.ChineseDate
import cn.allin.utils.TimeTextStyle
import cn.allin.utils.getDisplayName
import cn.allin.utils.lastDayOfMonth
import cn.allin.utils.withDayOfMonth
import cn.allin.utils.withMonth
import cn.allin.utils.withYear
import com.compose.components.DatePickerDayType.Companion.DayTypeDefault
import com.compose.components.DatePickerDayType.Companion.DayTypeDisable
import com.compose.components.DatePickerDayType.Companion.DayTypeNull
import com.compose.components.DatePickerDayType.Companion.DayTypePre
import com.compose.components.DatePickerState.Companion.PickerUiDefault
import com.compose.components.DatePickerState.Companion.PickerUiMonth
import com.compose.components.DatePickerState.Companion.PickerUiYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.jvm.JvmInline
import kotlin.time.Clock

/**
 * [onDate] 参数顺序 年、月、日
 */
@Composable
fun DatePickerDialog(
    onCancel: () -> Unit,
    pickerState: DatePickerState = remember { DatePickerState() },
    colors: DatePickerColors = DatePickerColors(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.primary.copy(0.3f),
        MaterialTheme.colorScheme.secondary
    ),
    onDate: (LocalDate) -> Unit
) {
    Dialog(onCancel, DialogProperties()) {

        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp)
        ) {

            DatePicker(pickerState, colors = colors)

            Row(
                Modifier
                    .align(Alignment.End)
                    .padding(bottom = 10.dp, end = 10.dp)
            ) {
                val enabled = pickerState.showUiType == PickerUiDefault && pickerState.selectDay.isNotEmpty()
                TextButton(
                    onCancel,
                    enabled = enabled
                ) {
                    Text("取消")
                }
                Spacer(Modifier.width(20.dp))
                TextButton(
                    onClick = {
                        onCancel()
                        onDate(pickerState.currentDate)
                    },
                    enabled = enabled
                ) {
                    Text("确定")
                }
            }
        }
    }
}

//@Preview
@Composable
fun DatePicker() {
    MaterialTheme {
        DatePicker(
            remember { DatePickerState() }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerColors(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.onSecondary
    )
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Box(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val yearModifier = Modifier.clickable {
                    state.showUiType = PickerUiYear
                }
                Text(
//                    stringResource(R.string.year_format, state.now.year),
                    "${state.now.year}年",
                    yearModifier,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                androidx.compose.material3.Icon(
                    Icons.Rounded.KeyboardArrowDown, null,
                    yearModifier
                        .padding(end = 4.dp)
                        .size(30.dp)
                        .padding(5.dp)
                )
                val monthModifier = Modifier.clickable {
                    state.showUiType = PickerUiMonth
                }
                Text(
                    state.nowMonth,
                    monthModifier.padding(start = 10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                androidx.compose.material3.Icon(
                    Icons.Rounded.KeyboardArrowDown, null,
                    monthModifier
                        .padding(end = 4.dp)
                        .size(30.dp)
                        .padding(5.dp)
                )
            }

            val color = if (state.enabledBackToToday) colors.selectColor else colors.disableColor

            Text(
//                stringResource(R.string.back_to_today),
                "回到今天",
                Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(state.enabledBackToToday) {
                        state.showUiType = PickerUiDefault
                        state.currentDate = state.today
                    }
                    .border(1.dp, color, MaterialTheme.shapes.medium)
                    .padding(8.dp, 6.dp),
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        when (state.showUiType) {
            PickerUiDefault -> DatePickerLayout(state, colors)
            PickerUiMonth -> PickerWheel(
                state.monthArray,
                state.monthSwipe,
                state.pickerHeight,
                colors.disableColor,
                state::clickMonth
            )

            PickerUiYear -> PickerWheel(
                state.yearArray,
                state.yearSwipe,
                state.pickerHeight,
                colors.disableColor,
                state::clickYear
            )
        }
    }

}

@Immutable
class DatePickerColors(
    val selectColor: Color,
    val onSelectColor: Color,
    val preColor: Color,
    val disableColor: Color
)


@Composable
private fun DatePickerLayout(
    state: DatePickerState,
    colors: DatePickerColors
) {

    val language = Locale.current.language
    val dayOfWeek = remember {
        val style = if (language.lowercase() == "china")
            TimeTextStyle.NARROW else TimeTextStyle.SHORT

        val weeks = DayOfWeek.entries
        Array(weeks.size) {
            weeks[it].getDisplayName(style)
        }
    }

    AnimatedContent(
        state.dayArray,
        Modifier.pointerInput(state, state.selectPointerInput),
        transitionSpec = {
            val target = targetState.date
            val init = initialState.date

            if (init.year == target.year && init.month == target.month) {
                EnterTransition.None togetherWith ExitTransition.None
            } else if (init < target) {
                //下一个月动画：从右边往左
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            } else {
                //上一个月动画：从左边往右
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
        },
        label = "DatePicker"
    ) { da ->

        Layout(
            content = {
                val weekModifier = Modifier
                    .size(BoxSize)
                    .wrapContentSize()

                dayOfWeek.forEach {
                    key(it) {
                        Text(it, weekModifier, colors.disableColor)
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 6.dp))

                val typography = MaterialTheme.typography

                CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.merge(textAlign = TextAlign.Center)) {
                    da.array.forEach {
                        key(it) {
                            val day = remember(it.day, it.chineseDay) {
                                buildAnnotatedString {
                                    append(it.day)
                                    append('\n')
                                    withStyle(typography.labelSmall.toSpanStyle()) {
                                        append(it.chineseDay)
                                    }
                                }
                            }
                            if (it.day.isNotEmpty() && it.day == state.selectDay) Text(
                                day,
                                Modifier
                                    .size(BoxSize)
                                    .background(colors.selectColor, CircleShape)
                                    .wrapContentSize(),
                                colors.onSelectColor
                            ) else when (it.dayType) {
                                DayTypeDefault -> {
                                    Text(
                                        day,
                                        Modifier
                                            .size(BoxSize)
                                            .wrapContentSize()
                                    )
                                }

                                DayTypePre -> {
                                    Text(
                                        day,
                                        Modifier
                                            .size(BoxSize)
                                            .background(colors.preColor, CircleShape)
                                            .wrapContentSize()
                                    )
                                }

                                DayTypeDisable -> {
                                    Text(
                                        day,
                                        Modifier
                                            .size(BoxSize)
                                            .wrapContentSize(),
                                        colors.disableColor
                                    )
                                }

                                else -> Spacer(Modifier.size(BoxSize))
                            }
                        }
                    }
                }
            },
            measurePolicy = state.measurePolicy
        )
    }

}

@ExperimentalFoundationApi
@Composable
private fun PickerWheel(
    items: Array<String>,
    swipeableState: AnchoredDraggableState<Int>,
    pickerHeight: Dp,
    disableColor: Color,
    onClick: () -> Unit
) {
    val scope = remember { floatArrayOf(0f, 0f) }
    val contentColor = LocalContentColor.current
    Wheel(
        items,
        swipeableState,
        Modifier
            .fillMaxWidth()
            .height(pickerHeight)
            .padding(vertical = 40.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    if (it.y in scope[0]..scope[1]) {
                        onClick()
                    }
                }
            },
        40.dp,
        50.dp,
        selectedContent = { height, str ->
            Text(
                str,
                Modifier
                    .height(height)
                    .wrapContentHeight(),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        unselectedContent = { height, str ->
            Text(
                str,
                Modifier
                    .height(height)
                    .wrapContentHeight(),
                disableColor
            )
        }
    ) { top, bottom ->
        scope[0] = top
        scope[1] = bottom

        val xStart = size.width / 3
        val yTop = top + 16
        val yBottom = bottom - 16
        drawLine(
            contentColor,
            Offset(xStart, yTop),
            Offset(xStart, yBottom),
        )
        val xEnd = size.width * 2 / 3
        drawLine(
            contentColor,
            Offset(xEnd, yTop),
            Offset(xEnd, yBottom),
        )

    }
}

/**
 * 单个格子的大小
 */
private val BoxSize = 44.dp

/**
 * 日期之间的垂直间隔
 */
private val BoxSpace = 8.dp

/**
 * 日期的格子数量，7列（对应星期）6行
 */
private const val DayBoxCount = 42


/**
 * 指定某一天的状态
 */
fun interface DatePickerDayType {

    fun type(year: Int, month: Int, dayOfMonth: Int): Type

    @JvmInline
    value class Type(val type: Byte)

    companion object {
        internal val DayTypeNull = Type(-1)

        /**
         * 默认
         */
        val DayTypeDefault = Type(0)

        /**
         * 不可选
         */
        val DayTypeDisable = Type(1)

        /**
         * 预选中
         */
        val DayTypePre = Type(2)
    }
}

/**
 * 日期选择器,[startDate] - [endDate]是可选择的日期范围
 * @param onSelect 某天被选中之后的回调
 */
@OptIn(ExperimentalFoundationApi::class)
@Stable
class DatePickerState(
    private val startDate: LocalDate = LocalDate(1900, 1, 1),
    private val endDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    private val onSelect: ((LocalDate) -> Unit)? = null,
) {
    companion object {
        const val PickerUiDefault: Byte = 0

        const val PickerUiYear: Byte = 1

        const val PickerUiMonth: Byte = 2
    }

    internal val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    internal var pickerHeight = 240.dp


    internal val measurePolicy = MeasurePolicy { measurables, layoutConstraints ->

        val placeables = measurables.map { it.measure(layoutConstraints) }

        val itemWidth = layoutConstraints.maxWidth / 7
        val oneHeight = placeables[0].height
        //分割线下标
        val dividerIndex = 7
        val boxSize = BoxSize.roundToPx()

        val space = BoxSpace.roundToPx()

        //组件高度为7个box加一个分割线
        val layoutHeight = boxSize * 7 + space * 4 + placeables[dividerIndex].height

        pickerHeight = layoutHeight.toDp()

        layout(layoutConstraints.maxWidth, layoutHeight) {

            //第一行，星期
            repeat(7) {
                val placeable = placeables[it]
                placeable.placeRelative(it * itemWidth + (itemWidth - placeable.width) / 2, 0)
            }

            //分割线
            var placeable = placeables[dividerIndex]
            placeable.placeRelative(0, oneHeight)

            val topHeight = oneHeight + placeable.height

            val rectSize = Size(boxSize.toFloat(), boxSize.toFloat())
            //剩余的日期
            for (xIndex in 0..<7)
                for (yIndex in 0..<6) {
                    placeable = placeables[xIndex + yIndex * 7 + 8]

                    val x = xIndex * itemWidth + (itemWidth - placeable.width) / 2
                    val y = yIndex * placeable.height + topHeight + space * yIndex

                    dayRect[xIndex + yIndex * 7] = Rect(Offset(x.toFloat(), y.toFloat()), rectSize)
                    placeable.placeRelative(x, y)
                }
        }
    }

    //点击事件，获取点到的那个日期
    internal val selectPointerInput: suspend PointerInputScope.() -> Unit = {
        val coroutineScope = CoroutineScope(currentCoroutineContext())

        coroutineScope.launch {
            syncDateSwipe()
        }
        coroutineScope.launch {
            var drag = 0f
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (drag > 100f) {
                        minusMonth()
                    } else if (drag < -100f) {
                        plusMonth()
                    }
                    drag = 0f
                }
            ) { _, dragAmount ->
                drag += dragAmount
            }
        }
        detectTapGestures { tapOffset ->
            val selectIndex = dayRect.indexOfFirst {
                it.contains(tapOffset)
            }

            if (selectIndex > -1
                && _dayArray[selectIndex].day.isNotEmpty()
                && _dayArray[selectIndex].dayType != DayTypeDisable
            ) {
                selectDay = _dayArray[selectIndex].day

                onSelect?.invoke(currentDate)
            }
        }
    }

    internal val yearSwipe =  createAnchoredDraggable(0)
    internal val monthSwipe =  createAnchoredDraggable(0)

    var showUiType: Byte? by mutableStateOf(PickerUiDefault)

    //包装一层用于动画api
    @Immutable
    internal class DayArray(
        val array: Array<Day>,
        //用于比较动画方向
        val date: LocalDate
    )

    @Immutable
    internal class Day(
        var day: String,
        var chineseDay: String,
        var dayType: DatePickerDayType.Type
    )

    /**
     * 日期数组，1日之前和当月最后一天之后不显示
     */
    private val _dayArray = Array(DayBoxCount) { Day("","", DayTypeNull) }

    /**
     * 日期位于ui上的矩形
     */
    private val dayRect = Array(DayBoxCount) { Rect.Zero }

    /**
     * 当前显示的时间
     */
    internal var now by mutableStateOf(endDate)

    /**
     * 年份列表
     */
    internal var yearArray = Array(endDate.year - startDate.year + 1) { (endDate.year - it).toString() }

    /**
     * 是否能点击回到今天
     */
    internal var enabledBackToToday = false


    /**
     * 被选中的那一天
     */
    internal var selectDay by callbackStateOf(endDate.day.toString()){ a, b ->
        enabledBackToToday = !(
                today.year == now.year
                && today.month == now.month
                && today.day.toString() == b
            )

        a == b
    }

    internal val nowMonth by derivedStateOf {
        upMonthArray(now)
        now.month.getDisplayName(TimeTextStyle.SHORT_STANDALONE)
    }

    internal val dayArray by derivedStateOf {
        createDayArray(now)
    }

    private fun plusMonth() {
        val next = now.plus(1,DateTimeUnit.MONTH)
        if (next > endDate) return
        selectDay = ""
        now = next
    }

    private fun minusMonth() {
        val prev = now.minus(1,DateTimeUnit.MONTH)

        if (prev < startDate) return
        selectDay = ""
        now = prev
    }

    /**
     * 创建显示天的数组
     */
    private fun createDayArray(date: LocalDate): DayArray {
        val startDayOfMonth = date.withDayOfMonth(1)
        val endDayOfMonth = date.lastDayOfMonth()

        var day = startDayOfMonth.day
        val year = date.year
        val month = date.month.number

        //该月1日星期下标
        val weekIndex = startDayOfMonth.dayOfWeek.ordinal

        for (i in _dayArray.indices) {
            //1日之前的星期和最后一天之后的位置为空不显示东西
            if (i < weekIndex || day > endDayOfMonth.day) {
                _dayArray[i].day = ""
                _dayArray[i].dayType = DayTypeNull
                continue
            }

            _dayArray[i].day = day.toString()
            _dayArray[i].chineseDay = ChineseDate(LocalDate(year, month, day)).run { getLunarFestivals()?:getChineseDay() }


            _dayArray[i].dayType = if (
                (startDate.year == year && startDate.month.number == month && day < startDate.day)
                ||
                (endDate.year == year && endDate.month.number == month && day > endDate.day)
            ) {
                DayTypeDisable
            } else {
                dayType.type(year, month, day)
            }

            day++
        }

        return DayArray(_dayArray, date)
    }

    /**
     * @see DatePickerDayType
     */
    var dayType: DatePickerDayType = DatePickerDayType { _, _, _ ->
        DayTypeDefault
    }

    var currentDate: LocalDate
        set(value) {
            now = value
            selectDay = value.day.toString()
        }
        get() = if (selectDay.isEmpty()) now else now.withDayOfMonth(selectDay.toInt())


    /**
     * 同步年和月的滚轮位置
     */
    private suspend fun syncDateSwipe() {
        val y = yearArray.indexOf(currentDate.year.toString())
        if (y > -1) {
            yearSwipe.snapTo(y)
        }

        val m = monthArray.indexOf(currentDate.month.number.toString())
        if (m > -1) {
            monthSwipe.snapTo(m)
        }
    }

    internal fun clickYear() {
        val year = yearArray[yearSwipe.currentValue].toInt()
        now = now.withYear(year)
        if (showUiType == PickerUiYear) {
            showUiType = PickerUiMonth
        }
    }

    internal fun clickMonth() {
        val month = monthArray[monthSwipe.currentValue].toInt()
        now = now.withMonth(month)
        if (showUiType == PickerUiMonth) {
            showUiType = PickerUiDefault
        }
    }

    internal var monthArray: Array<String> = emptyArray()


    private fun upMonthArray(now: LocalDate) {
        val start = if (now.year == startDate.year) {
            //小于开始时间的月份不要
            if (now.month.number < startDate.month.number)
                this.now = now.withMonth(startDate.month.number)

            startDate.month.ordinal
        } else {
            0
        }

        val end = if (now.year == endDate.year) {
            //大于结束时间的月份不要
            if (now.month.number > endDate.month.number)
                this.now = now.withMonth(endDate.month.number)

            endDate.month.ordinal
        } else {
            11
        }

        val months = Month.entries.slice(start..end)
        monthArray = Array(months.size) {
            months[months.size - it - 1].number.toString()
        }
    }
}
