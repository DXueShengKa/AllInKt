@file:OptIn(ExperimentalMaterial3Api::class)

package cn.allin.ui.components


import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import com.compose.components.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun TopBar(
    onBack: () -> Unit,
    title: @Composable RowScope.() -> Unit
) {
    TopAppBar(
        title = { Row(content = title) },
        navigationIcon = {
            BackIcon(onBack = onBack)
        }
    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
){
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

val IconBack: ImageVector = ImageVector.Builder(
    name = "IcBack", defaultWidth = 28.0.dp, defaultHeight = 28.0.dp, viewportWidth = 28.0f, viewportHeight = 28.0f
).apply {
    path(
        fill = SolidColor(Color(0x65F4F4F4)),
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 4.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(0.0f, 12.96f)
        curveTo(0.0f, 8.3285f, 0.0f, 6.0128f, 0.9299f, 4.2566f)
        curveTo(1.6803f, 2.8393f, 2.8393f, 1.6803f, 4.2566f, 0.9299f)
        curveTo(6.0128f, 0.0f, 8.3285f, 0.0f, 12.96f, 0.0f)
        horizontalLineTo(15.04f)
        curveTo(19.6715f, 0.0f, 21.9872f, 0.0f, 23.7434f, 0.9299f)
        curveTo(25.1607f, 1.6803f, 26.3197f, 2.8393f, 27.0701f, 4.2566f)
        curveTo(28.0f, 6.0128f, 28.0f, 8.3285f, 28.0f, 12.96f)
        verticalLineTo(15.04f)
        curveTo(28.0f, 19.6715f, 28.0f, 21.9872f, 27.0701f, 23.7434f)
        curveTo(26.3197f, 25.1607f, 25.1607f, 26.3197f, 23.7434f, 27.0701f)
        curveTo(21.9872f, 28.0f, 19.6715f, 28.0f, 15.04f, 28.0f)
        horizontalLineTo(12.96f)
        curveTo(8.3285f, 28.0f, 6.0128f, 28.0f, 4.2566f, 27.0701f)
        curveTo(2.8393f, 26.3197f, 1.6803f, 25.1607f, 0.9299f, 23.7434f)
        curveTo(0.0f, 21.9872f, 0.0f, 19.6715f, 0.0f, 15.04f)
        verticalLineTo(12.96f)
        close()
    }
    path(
        stroke = SolidColor(Color(0xFF2C2C2C)),
        strokeLineWidth = 2.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 4.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(17.0f, 20.0f)
        lineTo(11.0f, 14.0f)
        lineTo(17.0f, 8.0f)
    }
}.build()


@Composable
fun BackIcon(
    modifier: Modifier = Modifier.padding(start = 16.dp),
    onBack: () -> Unit
) {
    Image(IconBack, "返回", modifier.clickable(onClick = onBack))
}




@Composable
fun FillScreenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalPadding: Dp = 16.dp,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp
) {
    Button(
        onClick,
        modifier
            .padding(
                start = horizontalPadding,
                end = horizontalPadding,
                top = topPadding,
                bottom = bottomPadding
            )
            .fillMaxWidth(),
        enabled,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun WeScrollableTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    modifier: Modifier = Modifier,
    edgePadding: Dp = 16.dp,
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    unselectedBackgroundColor: Color = selectedBackgroundColor.copy(0.4f),
    onSelect: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier.height(40.dp),
        listState,
        contentPadding = PaddingValues(horizontal = edgePadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tabs.size) { index ->
            val selected = selectedTabIndex == index
            Text(
                tabs[index],
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        onSelect(index)
                        coroutineScope.launch {
                            val layoutInfo = listState.layoutInfo
                            val itemSize = layoutInfo.visibleItemsInfo.first { it.index == index }.size
                            listState.animateScrollToItem(index, (layoutInfo.viewportSize.width - itemSize) / -2)
                        }
                    }
                    .background(if (selected) selectedBackgroundColor else unselectedBackgroundColor)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


private const val IdIndicator: Byte = 1
private const val IdTabs: Byte = 2

private data class IndicatorPosition(
    val x: Int,
    val width: Int
)


@Composable
fun WeTabRow(
    selectedTabIndex: Int,
    tabText: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    spacing: Dp = 10.dp,
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    unselectedBackgroundColor: Color = selectedBackgroundColor.copy(0.4f),
    onSelect: (Int) -> Unit
) {
    val tabItems: @Composable @UiComposable () -> Unit = {
        tabText.forEachIndexed { index, text ->
            Text(
                text,
                Modifier.clickable { onSelect(index) },
                if (selectedTabIndex == index) selectedBackgroundColor else unselectedBackgroundColor
            )
        }
    }

    val indicator: @Composable @UiComposable (tabPositions: List<IndicatorPosition>) -> Unit = { tabPositions ->
        val currentTabPosition = tabPositions[selectedTabIndex]
        val density = LocalDensity.current
        val (converter, height) = remember {
            TwoWayConverter<IndicatorPosition, AnimationVector2D>(
                convertToVector = { AnimationVector(it.x.toFloat(), it.width.toFloat()) },
                convertFromVector = { IndicatorPosition(it.v1.toInt(), it.v2.toInt()) }
            ) to density.run { 2.dp.roundToPx() }
        }
        val pos by animateValueAsState(
            targetValue = currentTabPosition,
            typeConverter = converter
        )
        Box(
            Modifier
                .wrapContentSize(Alignment.BottomStart)
                .offset {
                    IntOffset(x = pos.x, y = 0)
                }
                .size(pos.width, height)
                .background(selectedBackgroundColor)
        )
    }


    val direction = LocalLayoutDirection.current
    SubcomposeLayout(modifier) { constraints ->
        val tabs = subcompose(IdTabs, tabItems).fastMap {
            it.measure(constraints)
        }

        val edgePaddingStart = contentPadding.calculateStartPadding(direction).roundToPx()
        val edgePaddingTop = contentPadding.calculateTopPadding().roundToPx()
        val edgePaddingBottom = contentPadding.calculateBottomPadding().roundToPx()
        val spacingInt = spacing.roundToPx()

        val layoutWidth = constraints.maxWidth
        val layoutHeight = tabs[0].height + edgePaddingTop + edgePaddingBottom

        layout(layoutWidth, layoutHeight) {
            val positions = ArrayList<IndicatorPosition>(tabs.size)

            var tabX = edgePaddingStart
            tabs.fastForEach { placeable ->
                placeable.place(tabX, edgePaddingTop)
                positions.add(IndicatorPosition(tabX, placeable.width))
                tabX += placeable.width + spacingInt
            }

            subcompose(IdIndicator) {
                indicator(positions)
            }.fastForEach {
                it.measure(Constraints.fixed(layoutWidth, layoutHeight))
                    .place(0, 0)
            }
        }
    }

}


//fun CoroutineScope.getOneFormatString(formatId: StringResource, stringId: StringResource): String {
//    return runBlocking(coroutineContext) {
//        val format = getString(stringId)
//        getString(formatId, format)
//    }
//}
//
//fun getOneFormatString(formatId: StringResource, stringId: StringResource): String {
//    return runBlocking {
//        val format = getString(stringId)
//        getString(formatId, format)
//    }
//}
//
//suspend fun getOneFormatStringSuspend(formatId: StringResource, stringId: StringResource): String {
//    val format = getString(stringId)
//    return getString(formatId, format)
//}
//
//@Composable
//fun oneFormatStringResource(formatId: StringResource, stringId: StringResource): String {
//    val environment = rememberResourceEnvironment()
//    return remember {
//        runBlocking {
//            val s = getString(environment, stringId)
//            getString(environment, formatId, s)
//        }
//    }
//}

/**
 * 弹窗预览媒体文件
 */
@Composable
expect fun PreviewMediaDialog(
    path: Any,
    isVideo: Boolean,
    onDismissRequest: () -> Unit,
)


@get:NonRestartableComposable
@get:Composable
expect val WindowInsetsIsImeVisible: Boolean


@Composable
expect fun onBackPressed(): () -> Unit
