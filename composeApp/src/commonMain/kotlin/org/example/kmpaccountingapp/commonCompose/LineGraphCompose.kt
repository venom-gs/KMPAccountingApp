package org.example.kmpaccountingapp.commonCompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.key.Key.Companion.K
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlin.math.abs
import kotlin.math.roundToInt


data class DataPoint(
    val x: Float,
    val y: Float,
    val label: String,
    val value: Float
)


// Example usage
@Composable
fun LineGraphExample() {
    val monthlyData = listOf(
        DataPoint(1f, 20f, "Jan", 20f),
        DataPoint(2f, 35f, "Feb", 35f),
        DataPoint(3f, 25f, "Mar", 25f),
        DataPoint(4f, 40f, "Apr", 40f),
        DataPoint(5f, 35f, "May", 35f),
        DataPoint(6f, 60f, "Jun", 60f),
        DataPoint(7f, 45f, "Jul", 45f),
        DataPoint(8f, 50f, "Aug", 50f)
    )

    LineGraphWithPopup(
        title = "Monthly Revenue (in $K)",
        dataPoints = monthlyData,
        lineColor = Color(0xFF2196F3),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LineGraphWithPopup(
    title: String,
    dataPoints: List<DataPoint>,
    lineColor: Color = Color(0xFF4CAF50),
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<DataPoint?>(null) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    var showPopup by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val canvasModifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { offset ->
                                // Find the nearest data point to the long press
                                val points = dataPoints.mapIndexed { index, point ->
                                    val pointX = size.width * (point.x / dataPoints.maxOf { it.x })
                                    val pointY = size.height * (1 - point.y / dataPoints.maxOf { it.y })
                                    Triple(point, Offset(pointX, pointY), index)
                                }

                                val nearest = points.minByOrNull { abs(it.second.x - offset.x) + abs(it.second.y - offset.y) }
                                nearest?.let {
                                    selectedPoint = it.first
                                    popupPosition = it.second
                                    showPopup = true
                                }
                            },
                            onTap = {
                                showPopup = false
                            }
                        )
                    }

                Canvas(modifier = canvasModifier) {
                    val maxX = dataPoints.maxOf { it.x }
                    val maxY = dataPoints.maxOf { it.y }

                    // Draw axes
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 2f
                    )

                    // Draw grid lines (horizontal)
                    for (i in 0..4) {
                        val y = size.height * (1 - i / 4f)
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                        )
                    }

                    // Draw data points and connect them with lines
                    for (i in 0 until dataPoints.size - 1) {
                        val startX = size.width * (dataPoints[i].x / maxX)
                        val startY = size.height * (1 - dataPoints[i].y / maxY)
                        val endX = size.width * (dataPoints[i + 1].x / maxX)
                        val endY = size.height * (1 - dataPoints[i + 1].y / maxY)

                        // Draw line between points
                        drawLine(
                            color = lineColor,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 3f
                        )

                        // Draw point
                        drawCircle(
                            color = lineColor,
                            radius = 6f,
                            center = Offset(startX, startY)
                        )

                        // Draw the last point
                        if (i == dataPoints.size - 2) {
                            drawCircle(
                                color = lineColor,
                                radius = 6f,
                                center = Offset(endX, endY)
                            )
                        }
                    }
                }

                // Show popup when a point is selected
                if (showPopup && selectedPoint != null) {
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(
                            popupPosition.x.roundToInt(),
                            popupPosition.y.roundToInt() - 100 // Offset above the point
                        )
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = selectedPoint!!.label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Value: ${selectedPoint!!.value}",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
