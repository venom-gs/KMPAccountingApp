package org.example.kmpaccountingapp.commonCompose
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

// Data class to represent a pie chart segment
data class PieChartData(
    val name: String,
    val value: Float,
    val color: Color
)

@Composable
fun InteractivePieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    outerRadius: Float = 120f,    // Controls the outer radius of the donut
    innerRadiusRatio: Float = 0.6f, // Inner radius as a ratio of outer radius (0-1)
    chartSize: Dp = 350.dp,      // Controls the overall canvas size
    containerHeight: Dp = 400.dp, // Controls the height of the container
    animDuration: Int = 1000
) {
    // Calculate the total sum of all data points
    val totalSum = data.sumOf { it.value.toDouble() }.toFloat()

    // Selected segment state
    var selectedSegment by remember { mutableStateOf<Int?>(null) }

    // Popup state
    var showPopup by remember { mutableStateOf(false) }
    var popupData by remember { mutableStateOf<PieChartData?>(null) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }

    // Animation progress for initial appearance
    val animatedProgress = animateFloatAsState(
        targetValue = 1f,
        label = "pieChartAnimation"
    )

    // Calculate angle for each segment for better click detection
    val segmentAngles = remember(data, totalSum) {
        var currentAngle = 0f
        data.map { pieData ->
            val sweepAngle = (pieData.value / totalSum) * 360f
            val startAngle = currentAngle
            currentAngle += sweepAngle
            Pair(startAngle, sweepAngle)
        }
    }

    // Create animated scales for each segment
    val animatedScales = List(data.size) { index ->
        animateFloatAsState(
            targetValue = if (index == selectedSegment) 1.1f else 1.0f,
            animationSpec = tween(300),
            label = "segmentScale$index"
        )
    }

    // Background color for the center of the donut
    var backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(chartSize)
                .pointerInput(data) {
                    detectTapGestures { offset ->
                        val center = Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
                        val distance = sqrt(
                            (offset.x - center.x).pow(2) + (offset.y - center.y).pow(2)
                        )

                        // Check if the click is within the donut (between inner and outer radius)
                        val innerRadius = outerRadius * innerRadiusRatio
                        if (distance in innerRadius..outerRadius) {
                            // Calculate angle of touch point (0-360 degrees, 0 at top, clockwise)
                            var touchAngle = (atan2(
                                offset.y - center.y,
                                offset.x - center.x
                            ) * (180 / PI).toFloat())

                            // Convert to 0-360 range starting from top (negative Y) going clockwise
                            touchAngle = (touchAngle + 360) % 360
                            // Adjust to match our chart starting point at -90 degrees
                            touchAngle = (touchAngle + 90) % 360

                            // Find which segment was clicked
                            for (i in segmentAngles.indices) {
                                val (startAngle, sweepAngle) = segmentAngles[i]
                                if (isAngleInSegment(touchAngle, startAngle, sweepAngle)) {
                                    popupData = data[i]
                                    popupPosition = offset
                                    selectedSegment = if (selectedSegment == i) null else i
                                    showPopup = selectedSegment != null
                                    break
                                }
                            }
                        }
                    }
                }
        ) {
            // Draw the pie chart
            val width = size.width
            val height = size.height
            val center = Offset(width / 2, height / 2)

            var startAngle = -90f // Start from the top (translated to -90 in canvas coordinates)

            data.forEachIndexed { index, pieData ->
                val sweepAngle = (pieData.value / totalSum) * 360f * animatedProgress.value
                val scaleFactor = animatedScales[index].value
                val segmentOuterRadius = outerRadius * scaleFactor
                val segmentInnerRadius = (outerRadius * innerRadiusRatio)

                // Draw donut segment (we'll need to use a path for this)
                val path = Path().apply {
                    // Outer arc
                    addArc(
                        Rect(
                            center.x - segmentOuterRadius,
                            center.y - segmentOuterRadius,
                            center.x + segmentOuterRadius,
                            center.y + segmentOuterRadius
                        ),
                        startAngle,
                        sweepAngle
                    )

                    // Line to inner arc
                    val innerStartRadians = (startAngle + sweepAngle) * (PI / 180f).toFloat()
                    val innerStartX = center.x + segmentInnerRadius * cos(innerStartRadians)
                    val innerStartY = center.y + segmentInnerRadius * sin(innerStartRadians)
                    lineTo(innerStartX, innerStartY)

                    // Inner arc (going counter-clockwise)
                    arcTo(
                        Rect(
                            center.x - segmentInnerRadius,
                            center.y - segmentInnerRadius,
                            center.x + segmentInnerRadius,
                            center.y + segmentInnerRadius
                        ),
                        startAngle + sweepAngle,
                        -sweepAngle,
                        false
                    )

                    // Close the path
                    close()
                }

                // Draw the donut segment
                drawPath(
                    path = path,
                    color = pieData.color
                )

                startAngle += sweepAngle
            }

            // Draw the inner circle (background) to complete the donut effect
            drawCircle(
                color = backgroundColor,
                radius = outerRadius * innerRadiusRatio * 0.98f,  // Slightly smaller to avoid edge artifacts
                center = center
            )
        }

        // Draw labels in the center
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total")
            Text(
                text = totalSum.toString())
        }

        // Show popup for selected segment
        if (showPopup && popupData != null) {
            val density = LocalDensity.current.density
            val popupX = popupPosition.x / density
            val popupY = popupPosition.y / density

            Card(
                modifier = Modifier
                    .offset(popupX.dp - 155.dp, popupY.dp - 150.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(8.dp)) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = popupData!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = popupData!!.value.toString(),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${((popupData!!.value / totalSum) * 100).toFloat().round(1)}%",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
// Helper function to determine if an angle is within a segment
private fun isAngleInSegment(angle: Float, startAngle: Float, sweepAngle: Float): Boolean {
    // Handle segments that cross the 0/360 boundary
    return if (startAngle + sweepAngle > 360f) {
        angle >= startAngle || angle <= (startAngle + sweepAngle - 360f)
    } else {
        angle >= startAngle && angle <= startAngle + sweepAngle
    }
}

// Extension function to round a float to a specified number of decimal places
fun Float.round(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

// Example usage
@Composable
fun PieChartExample() {
    val pieChartData = listOf(
        PieChartData("Category A", 30f, Color(0xFF6200EE)),
        PieChartData("Category B", 20f, Color(0xFF03DAC5)),
        PieChartData("Category C", 15f, Color(0xFFFF6859)),
        PieChartData("Category D", 35f, Color(0xFF2196F3)),
        PieChartData("Category A", 30f, Color(0xFF6200EE)),
        PieChartData("Category B", 20f, Color(0xFF03DAC5)),
        PieChartData("Category C", 15f, Color(0xFFFF6859)),
        PieChartData("Category D", 35f, Color(0xFF2196F3))
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Interactive Pie Chart",
                textAlign = TextAlign.Center
            )
            InteractivePieChart(
                data = pieChartData,
                modifier = Modifier.fillMaxWidth(),
                outerRadius = 500f,           // Increase the radius
                chartSize = 450.dp,      // Increase the canvas size
                containerHeight = 330.dp // Increase the container height
            )
//Row(horizontalArrangement= Arrangement.SpaceBetween) {
//    // Example of how to customize the pie chart size
//    Box(modifier = Modifier.weight(0.5f)){
//    }
//    // Legend
//    Column(
//        modifier = Modifier.fillMaxWidth().weight(0.5f),
//        horizontalAlignment = Alignment.Start
//    ) {
//
//        pieChartData.forEach { data ->
//            Row(
//                modifier = Modifier,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(16.dp)
//                        .background(data.color)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = data.name
//                )
//                Text(
//                    text = "(${((data.value / pieChartData.sumOf { it.value.toDouble() }) * 100).toFloat().round(1)}%)")
//            }
//        }
//    }
//}

        }
    }
}