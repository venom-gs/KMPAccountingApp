import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BarData(
    val label: String,
    val value: Float,
    val color: Color = Color(0xFF4CAF50)
)

@Composable
fun CustomBarChart(
    modifier: Modifier = Modifier,
    data: List<BarData>,
    maxValue: Float = data.maxOfOrNull { it.value } ?: 0f,
    horizontalLinesCount: Int = 5,
    barWidth: Float = 0.7f, // Width of bar as percentage of available space
    barCornerRadius: Float = 8f,
    gridLineColor: Color = Color.LightGray,
    backgroundColor: Color = Color.White,
    gridLineWidth: Float = 1f,
    gridLinePathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
) {
    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier.fillMaxWidth()) {
        // Chart title
        Text(
            text = "Custom Bar Chart",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Main chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 40.dp, end = 16.dp, top = 16.dp, bottom = 40.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val availableWidth = size.width
                val availableHeight = size.height
                val barSpace = availableWidth / data.size

                // Draw background
                drawRect(backgroundColor)

                // Draw horizontal grid lines
                drawHorizontalGridLines(
                    availableHeight = availableHeight,
                    horizontalLinesCount = horizontalLinesCount,
                    gridLineColor = gridLineColor,
                    gridLineWidth = gridLineWidth,
                    gridLinePathEffect = gridLinePathEffect,
                    maxValue = maxValue
                )

                // Draw bars
                data.forEachIndexed { index, barData ->
                    val barHeight = (barData.value / maxValue) * availableHeight
                    val left = index * barSpace + barSpace * (1 - barWidth) / 2
                    val top = availableHeight - barHeight

                    // Draw bar
                    drawRoundRect(
                        color = barData.color,
                        topLeft = Offset(left, top),
                        size = Size(barSpace * barWidth, barHeight),
                        cornerRadius = CornerRadius(barCornerRadius, barCornerRadius)
                    )

                    // Draw value on top of the bar - Multiplatform compatible
                    val valueText = barData.value.toInt().toString()
                    if (true){
                        val textLayoutResult = textMeasurer.measure(
                            text = valueText,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        )

                        drawText(
                            textLayoutResult = textLayoutResult,
                            topLeft = Offset(
                                left + (barSpace * barWidth / 2) - (textLayoutResult.size.width / 2),
                                top - textLayoutResult.size.height - 4f
                            )
                        )

                    }
                }
            }

            // Y-axis labels
            Column(
                modifier = Modifier
                    .height(300.dp - 40.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-35).dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in horizontalLinesCount downTo 0) {
                    val value = (maxValue / horizontalLinesCount) * i
                    Text(
                        text = value.toString(),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(30.dp)
                    )
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y= (-20).dp)
                .padding(start = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { barData ->
                Text(
                    text = barData.label,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(20.dp)
                )
            }
        }
    }
}

private fun DrawScope.drawHorizontalGridLines(
    availableHeight: Float,
    horizontalLinesCount: Int,
    gridLineColor: Color,
    gridLineWidth: Float,
    gridLinePathEffect: PathEffect,
    maxValue: Float
) {
    for (i in 0..horizontalLinesCount) {
        val y = availableHeight - (i * availableHeight / horizontalLinesCount)

        drawLine(
            color = gridLineColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = gridLineWidth,
            pathEffect = gridLinePathEffect
        )
    }
}

// Example usage
@Composable
fun BarChartExample() {
    val barData = listOf(
        BarData("1", 50f, Color(0xFF4CAF50)),
        BarData("2", 80f, Color(0xFF2196F3)),
        BarData("3", 30f, Color(0xFFFFC107)),
        BarData("4", 100f, Color(0xFFE91E63)),
        BarData("5", 65f, Color(0xFF9C27B0)),
        BarData("6", 50f, Color(0xFF4CAF50)),
        BarData("1", 80f, Color(0xFF2196F3)),
        BarData("2", 30f, Color(0xFFFFC107)),
        BarData("3", 100f, Color(0xFFE91E63)),
    BarData("4", 30f, Color(0xFFFFC107)),
    BarData("5", 100f, Color(0xFFE91E63)),
    BarData("6", 65f, Color(0xFF9C27B0))
    )

    CustomBarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp),
        data = barData
    )
}