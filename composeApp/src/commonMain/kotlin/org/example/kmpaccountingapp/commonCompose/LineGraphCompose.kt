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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Data model for line chart
 */
data class DataPoint(
    val value: Float,
    val label: String
)

/**
 * A beautiful and animated line chart with tooltips, grid lines, and proper axis labeling
 */
@Composable
fun AnimatedLineChart(
    dataPoints: List<DataPoint>,
    modifier: Modifier = Modifier,
    title: String = "Line Chart",
    xAxisTitle: String = "X Axis",
    yAxisTitle: String = "Y Axis",
    lineColor: Color = Color(0xFF2196F3),
    gridColor: Color = Color(0xFFEEEEEE),
    textColor: Color = Color(0xFF424242),
    backgroundColor: Color = Color.White,
    showAreaGradient: Boolean = true,
    showGridLines: Boolean = true,
    animationDuration: Int = 1000
) {
    if (dataPoints.isEmpty()) return

    val scope = rememberCoroutineScope()

    // Calculate max Y value with a little padding
    val maxY = remember(dataPoints) {
        (dataPoints.maxOf { it.value } * 1.2f).roundToInt().toFloat()
    }

    // Animation progress
    var animationProgress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "lineChartAnimation"
    )

    // Tooltip state
    var showTooltip by remember { mutableStateOf(false) }
    var tooltipIndex by remember { mutableStateOf(0) }
    var tooltipPosition by remember { mutableStateOf(Offset.Zero) }

    // Start the animation when the component is first composed
    LaunchedEffect(dataPoints) {
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
                    .pointerInput(dataPoints) {
                        detectTapGestures(
                            onPress = { offset ->
                                // Find closest point
                                val canvasWidth = size.width
                                val pointWidth = canvasWidth / (dataPoints.size - 1)
                                val index = (offset.x / pointWidth).toInt().coerceIn(0, dataPoints.size - 1)
                                tooltipIndex = index
                                tooltipPosition = Offset(
                                    x = (index * pointWidth).toFloat(),
                                    y = size.height * (1f - dataPoints[index].value / maxY)
                                )
                                showTooltip = true
                            },
                            onTap = { showTooltip = !showTooltip }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                val pointWidth = width / (dataPoints.size - 1)

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

                    // Vertical grid lines
                    for (i in dataPoints.indices) {
                        if (i % max(1, dataPoints.size / 6) == 0) {
                            val x = i * pointWidth
                            drawLine(
                                color = gridColor,
                                start = Offset(x, 0f),
                                end = Offset(x, height),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                            )
                        }
                    }
                }

                // Create path for line
                val linePath = Path().apply {
                    dataPoints.forEachIndexed { index, dataPoint ->
                        val progress = (index.toFloat() / (dataPoints.size - 1).coerceAtLeast(1))
                        if (progress <= animatedProgress) {
                            val currentAnimProgress = if (index < dataPoints.size - 1) 1f else
                                (animatedProgress - progress) / (1f / (dataPoints.size - 1).coerceAtLeast(1))

                            val x = index * pointWidth
                            val normalizedY = dataPoint.value / maxY
                            val y = height * (1 - normalizedY * currentAnimProgress.coerceIn(0f, 1f))

                            if (index == 0) {
                                moveTo(x, y)
                            } else {
                                lineTo(x, y)
                            }
                        }
                    }
                }

                // Create path for the area under the line
                if (showAreaGradient) {
                    val areaPath = Path().apply {
                        // Start at the bottom-left corner
                        moveTo(0f, height)

                        // Add all the line points
                        dataPoints.forEachIndexed { index, dataPoint ->
                            val progress = (index.toFloat() / (dataPoints.size - 1).coerceAtLeast(1))
                            if (progress <= animatedProgress) {
                                val currentAnimProgress = if (index < dataPoints.size - 1) 1f else
                                    (animatedProgress - progress) / (1f / (dataPoints.size - 1).coerceAtLeast(1))

                                val x = index * pointWidth
                                val normalizedY = dataPoint.value / maxY
                                val y = height * (1 - normalizedY * currentAnimProgress.coerceIn(0f, 1f))

                                lineTo(x, y)
                            }
                        }

                        // Close the path by drawing lines to the bottom-right and bottom-left corners
                        val lastVisiblePointIndex = (dataPoints.size * animatedProgress).toInt().coerceAtMost(dataPoints.size - 1)
                        lineTo(lastVisiblePointIndex * pointWidth, height)
                        lineTo(0f, height)
                        close()
                    }

                    // Draw area with gradient
                    drawPath(
                        path = areaPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.4f),
                                lineColor.copy(alpha = 0.1f),
                                lineColor.copy(alpha = 0.05f)
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )
                }

                // Draw line
                drawPath(
                    path = linePath,
                    color = lineColor,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Draw data points with animation
                dataPoints.forEachIndexed { index, dataPoint ->
                    val progress = (index.toFloat() / (dataPoints.size - 1).coerceAtLeast(1))
                    if (progress <= animatedProgress) {
                        val currentAnimProgress = if (index < dataPoints.size - 1) 1f else
                            (animatedProgress - progress) / (1f / (dataPoints.size - 1).coerceAtLeast(1))

                        val x = index * pointWidth
                        val normalizedY = dataPoint.value / maxY
                        val y = height * (1 - normalizedY * currentAnimProgress.coerceIn(0f, 1f))

                        // Draw point
                        drawCircle(
                            color = lineColor,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )

                        // Draw white inner circle
                        drawCircle(
                            color = Color.White,
                            radius = 2.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                // Draw tooltip if needed
                if (showTooltip && tooltipIndex in dataPoints.indices) {
                    val dataPoint = dataPoints[tooltipIndex]
                    val x = tooltipIndex * pointWidth
                    val normalizedY = dataPoint.value / maxY
                    val y = height * (1 - normalizedY)

                    // Draw vertical indicator line
                    drawLine(
                        color = lineColor.copy(alpha = 0.5f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )

                    // Draw tooltip background
                    val tooltipWidth = 80.dp.toPx()
                    val tooltipHeight = 40.dp.toPx()
                    val tooltipX = (x - tooltipWidth / 2).coerceIn(0f, width - tooltipWidth)
                    val tooltipY = (y - tooltipHeight - 10.dp.toPx()).coerceIn(0f, height - tooltipHeight)

                    drawRoundRect(
                        color = Color(0xDD000000),
                        topLeft = Offset(tooltipX, tooltipY),
                        size = Size(tooltipWidth, tooltipHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )

                    // Draw tooltip text using the proper TextMeasurer approach

                    val labelLayout = textMeasurer.measure(
                        text = AnnotatedString(dataPoint.label),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    val valueLayout = textMeasurer.measure(
                        text = AnnotatedString(dataPoint.value.toString()),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    drawText(
                        labelLayout,
                        topLeft = Offset(
                            x = tooltipX + (tooltipWidth - labelLayout.size.width) / 2,
                            y = tooltipY + 8.dp.toPx()
                        )
                    )

                    drawText(
                        valueLayout,
                        topLeft = Offset(
                            x = tooltipX + (tooltipWidth - valueLayout.size.width) / 2,
                            y = tooltipY + 8.dp.toPx() + labelLayout.size.height
                        )
                    )
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
            // Show a subset of labels to avoid overcrowding
            val step = max(1, dataPoints.size / 6)
            dataPoints.forEachIndexed { index, dataPoint ->
                if (index % step == 0 || index == dataPoints.lastIndex) {
                    Text(
                        text = dataPoint.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
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

@Composable
fun LineChartSample() {
    val data = listOf(
        DataPoint(18f, "Jan"),
        DataPoint(24f, "Feb"),
        DataPoint(15f, "Mar"),
        DataPoint(32f, "Apr"),
        DataPoint(27f, "May"),
        DataPoint(43f, "Jun"),
        DataPoint(38f, "Jul"),
        DataPoint(52f, "Aug"),
        DataPoint(47f, "Sep"),
        DataPoint(35f, "Oct"),
        DataPoint(42f, "Nov"),
        DataPoint(58f, "Dec")
    )

    AnimatedLineChart(
        dataPoints = data,
        title = "Monthly Sales Performance",
        xAxisTitle = "Month",
        yAxisTitle = "Sales (K)",
        lineColor = Color(0xFF00BCD4),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}