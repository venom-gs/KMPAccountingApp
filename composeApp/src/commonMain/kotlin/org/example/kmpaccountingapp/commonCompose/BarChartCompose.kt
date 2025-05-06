import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Data class for bar chart items
 */
data class BarData(
    val value: Float,
    val label: String,
    val color: Color = Color(0xFF2196F3)
)

/**
 * Interactive Bar Chart with selection highlighting
 */
@Composable
fun InteractiveBarChart(
    barData: List<BarData>,
    modifier: Modifier = Modifier,
    title: String = "Bar Chart",
    xAxisTitle: String = "X Axis",
    yAxisTitle: String = "Y Axis",
    gridColor: Color = Color(0xFFEEEEEE),
    textColor: Color = Color(0xFF424242),
    backgroundColor: Color = Color.White,
    showGridLines: Boolean = true,
    animationDuration: Int = 1000,
    barCornerRadius: Float = 8f,
    barPadding: Float = 0.2f // Percentage of bar width used as padding (0-1)
) {
    if (barData.isEmpty()) return

    // State for selected bar
    var selectedBarIndex by remember { mutableStateOf(-1) }

    // Calculate max Y value with padding
    val maxY = remember(barData) {
        (barData.maxOf { it.value } * 1.2f).roundToInt().toFloat()
    }

    // Animation progress
    var animationProgress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "barChartAnimation"
    )

    // Animation states for each bar
    val barAnimations = remember(barData) {
        barData.indices.map { index ->
            Animatable(initialValue = 1f)
        }
    }

    LaunchedEffect(selectedBarIndex) {
        // Create coroutines for each animation to run concurrently
        val animationJobs = barAnimations.mapIndexed { index, animatable ->
            launch {
                if (index == selectedBarIndex) {
                    animatable.animateTo(
                        targetValue = 1.15f, // Scale factor for selected bar
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                } else {
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100)
                    )
                }
            }
        }

        // Optionally wait for all animations to complete
        animationJobs.forEach { it.join() }
    }
    // Start the animation when the component is first composed
    LaunchedEffect(barData) {
        animationProgress = 0f
        delay(300) // Small delay for better UX
        animationProgress = 1f
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Chart title
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Y-axis title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(
                text = yAxisTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterStart),
                textAlign = TextAlign.Start
            )
        }

        // Chart area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 40.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            // Create Y-axis labels
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(40.dp)
                    .offset(x = (-40).dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 5 downTo 0) {
                    Text(
                        text = (maxY * i / 5).toInt().toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor,
                        fontSize = 10.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            val textMeasurer = rememberTextMeasurer()

            // Main chart canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(barData) {
                        detectTapGestures { offset ->
                            // Calculate which bar was tapped
                            val barWidth = size.width / barData.size
                            val tappedBarIndex = (offset.x / barWidth).toInt()
                                .coerceIn(0, barData.size - 1)

                            // Toggle selection (select if different, deselect if same)
                            selectedBarIndex = if (selectedBarIndex == tappedBarIndex) -1 else tappedBarIndex
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val barWidth = width / barData.size
                val effectiveBarWidth = barWidth * (1 - barPadding)

                // Draw grid lines
                if (showGridLines) {
                    // Horizontal grid lines
                    for (i in 0..5) {
                        val y = height * (1 - i / 5f)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                        )
                    }
                }

                // Draw bars
                barData.forEachIndexed { index, bar ->
                    val normalizedValue = bar.value / maxY
                    val barHeight = height * normalizedValue * animatedProgress
                    val isSelected = index == selectedBarIndex

                    // Calculate position
                    val barScale = barAnimations[index].value
                    val scaledBarWidth = effectiveBarWidth * barScale
                    val barLeft = index * barWidth + (barWidth - scaledBarWidth) / 2
                    val barRight = barLeft + scaledBarWidth

                    // Adjust Y-position for scaled bar (keep bottom anchored)
                    val barBottom = height
                    val scaledBarHeight = barHeight * barScale
                    val barTop = barBottom - scaledBarHeight

                    // Draw bar with rounded corners (only top corners)
                    val path = Path().apply {
                        // Start at bottom-left
                        moveTo(barLeft, barBottom)
                        // Line to top-left with rounded corner
                        lineTo(barLeft, barTop + barCornerRadius)
                        arcTo(
                            rect = Rect(
                                left = barLeft,
                                top = barTop,
                                right = barLeft + barCornerRadius * 2,
                                bottom = barTop + barCornerRadius * 2
                            ),
                            startAngleDegrees = 180f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        // Line to top-right with rounded corner
                        lineTo(barRight - barCornerRadius, barTop)
                        arcTo(
                            rect = Rect(
                                left = barRight - barCornerRadius * 2,
                                top = barTop,
                                right = barRight,
                                bottom = barTop + barCornerRadius * 2
                            ),
                            startAngleDegrees = 270f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        // Line to bottom-right and close
                        lineTo(barRight, barBottom)
                        close()
                    }

                    // Draw bar with gradient
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                bar.color,
                                bar.color.copy(alpha = 0.7f)
                            ),
                            startY = barTop,
                            endY = barBottom
                        )
                    )

                    // Draw selection indicator/border if selected
                    if (isSelected) {
                        drawPath(
                            path = path,
                            color = Color.White.copy(alpha = 0.5f),
//                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Draw highlight on top of bar
                        drawLine(
                            color = Color.White,
                            start = Offset(barLeft + barCornerRadius, barTop + 2.dp.toPx()),
                            end = Offset(barRight - barCornerRadius, barTop + 2.dp.toPx()),
                            strokeWidth = 3.dp.toPx()
                        )

                        // Draw value label above the bar
                        val valueText = bar.value.toInt().toString()
                        val textLayoutResult = textMeasurer.measure(
                            text = AnnotatedString(valueText),
                            style = TextStyle(
                                color = bar.color.darken(0.2f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        val textX = barLeft + (scaledBarWidth - textLayoutResult.size.width) / 2
                        val textY = barTop - textLayoutResult.size.height - 4.dp.toPx()

                        // Draw text with a small shadow
                        translate(left = 1f, top = 1f) {
                            drawText(
                                textLayoutResult,
                                color = Color.Black.copy(alpha = 0.2f),
                                topLeft = Offset(textX, textY)
                            )
                        }

                        drawText(
                            textLayoutResult,
                            topLeft = Offset(textX, textY)
                        )
                    }
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            barData.forEach { bar ->
                Text(
                    text = bar.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // X-axis title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = xAxisTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Helper function to darken a color
 */
fun Color.darken(factor: Float = 0.1f): Color {
    return copy(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor)
    )
}

/**
 * Sample usage of the InteractiveBarChart
 */
@Composable
fun BarChartSample() {
    val data = listOf(
        BarData(42f, "Jan", Color(0xFF4CAF50)),
        BarData(28f, "Feb", Color(0xFF2196F3)),
        BarData(65f, "Mar", Color(0xFFFFC107)),
        BarData(35f, "Apr", Color(0xFF9C27B0)),
        BarData(53f, "May", Color(0xFFE91E63)),
        BarData(47f, "Jun", Color(0xFF3F51B5)),
        BarData(72f, "Jul", Color(0xFF00BCD4))
    )

    InteractiveBarChart(
        barData = data,
        title = "Monthly Performance",
        xAxisTitle = "Month",
        yAxisTitle = "Value",
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}