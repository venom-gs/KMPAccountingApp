package org.example.kmpaccountingapp.commonCompose

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlin.math.roundToInt



data class ProgressData(
    val label: String,
    val value: Float,
    val maxValue: Float,
    val color: Color,
    val details: String // Additional information to show in popup
)

@Composable
fun ProgressBarChartWithPopups() {
    val progressDataList = listOf(
        ProgressData(
            label = "Completed",
            value = 75f,
            maxValue = 100f,
            color = Color(0xFF4CAF50),
            details = "5 tasks completed today"
        )
    )
    progressDataList.forEach { progressData ->
        ProgressBarWithPopup(
            progressData = progressData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}




@Composable
fun ProgressBarWithPopup(
    progressData: ProgressData,
    modifier: Modifier = Modifier
) {
    var showPopup by remember { mutableStateOf(false) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    val progress = (progressData.value / progressData.maxValue).coerceIn(0f, 1f)

    Box(modifier = modifier.height(24.dp)) {

        Box(modifier = modifier) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                popupPosition = offset
                                showPopup = true
                            }
                        )
                    }
            ) {
                // Background
                drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    cornerRadius = CornerRadius(8f, 8f),
                    size = Size(size.width, size.height)
                )

                // Progress
                drawRoundRect(
                    color = progressData.color,
                    cornerRadius = CornerRadius(8f, 8f),
                    size = Size(size.width * progress, size.height)
                )
            }

            // Show popup when long press is detected
            if (showPopup) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(
                        popupPosition.x.roundToInt(),
                        popupPosition.y.roundToInt() -300 // Position above the touch point
                    ),
                    onDismissRequest = { showPopup = false }
                ) {
                    Surface(
                        color = Color.White,
                        border = BorderStroke(1.dp,Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = progressData.label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Value: ${progressData.value} / ${progressData.maxValue}",
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = progressData.details,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
