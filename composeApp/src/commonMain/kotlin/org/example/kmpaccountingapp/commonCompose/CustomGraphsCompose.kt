package org.example.kmpaccountingapp.commonCompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp


data class ChartData(
    val label: String,
    val value: Float
)

@Composable
fun MultiplatformBarChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val chartHeight = size.height
        val chartWidth = size.width
        val maxValue = data.maxOfOrNull { it.value } ?: 0f
        val barWidth = chartWidth / (data.size * 2)

        data.forEachIndexed { index, item ->
            val barHeight = (item.value / maxValue) * chartHeight
            val left = index * barWidth * 2

            // Draw bar
            drawRect(
                color = Color.Blue,
                topLeft = Offset(left, chartHeight - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

//            // Optional: Draw label
//            drawContext.canvas.nativeCanvas.drawText(
//                item.label,
//                left,
//                chartHeight + 20,
//                android.graphics.Paint().apply {
//                    color = android.graphics.Color.BLACK
//                    textSize = 30f
//                }
//            )
        }
    }
}

@Composable
fun MultiplatformLineChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val chartHeight = size.height
        val chartWidth = size.width
        val maxValue = data.maxOfOrNull { it.value } ?: 0f

        // Create path for line chart
        val path = Path()
        val points = data.mapIndexed { index, item ->
            val x = (index.toFloat() / (data.size - 1)) * chartWidth
            val y = chartHeight - (item.value / maxValue) * chartHeight
            Offset(x, y)
        }

        // Draw line
        path.moveTo(points.first().x, points.first().y)
        points.drop(1).forEach { point ->
            path.lineTo(point.x, point.y)
        }

        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 2f)
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color.Red,
                radius = 5f,
                center = point
            )
        }
    }
}
