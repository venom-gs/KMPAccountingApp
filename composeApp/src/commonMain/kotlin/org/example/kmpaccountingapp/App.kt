package org.example.kmpaccountingapp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect.Companion.dashPathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accountingappwithcompose.android.commonCompose.TextFieldsPreview
import com.example.accountingappwithcompose.android.screens.signIn.AllScreens
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.autoScaleXRange
import io.github.koalaplot.core.xygraph.autoScaleYRange
import io.github.koalaplot.core.xygraph.rememberFloatLinearAxisModel
import io.github.koalaplot.core.xygraph.rememberIntLinearAxisModel
import org.example.kmpaccountingapp.LoggedInViewContainer.LoggedInViewContainer
import org.example.kmpaccountingapp.signIn.CompanyRegistration


data class BarData(
    val label: String,
    val value: List<Double>,
    val color: Color = Color.Blue
)


@Composable
@Preview
fun App() {
    AppTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "loginPage",
                enterTransition = {
                    // you can change whatever you want transition
                    EnterTransition.None
                },
                exitTransition = {
                    // you can change whatever you want transition
                    ExitTransition.None
                }) {
                composable(route = "loginPage") {
//                    AllScreens({
//                        navController.navigate("dashBoard")
//                    })
//                    CompanyRegistration()
                    TextFieldsPreview()
//                    LoggedInViewContainer()
//                    LineGraphExample()
                }
                composable(route = "dashBoard") {
                    LoggedInViewContainer()
                }
            }
//            val xData = listOf(1,2,3,4,5,6)
//            val yData = listOf(1.0f,3.0f,4.0f,5.0f,6.0f,7.0f)
//            DailyMoodChart(Modifier,xData,yData)
//            LineChart()
//            BarChartExample()

}

    }
}


@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun LineChart(){
    val data = buildList {
        for (i in 1..10) {
            add(DefaultPoint(i.toFloat(), i * i.toFloat()))
        }
    }
    XYGraph(
        rememberFloatLinearAxisModel(data.autoScaleXRange()),
        rememberFloatLinearAxisModel(data.autoScaleYRange())
    ) {
        LinePlot(
            data,
            lineStyle = LineStyle(SolidColor(Color.Blue))
        )
        LinePlot(
            data,
            lineStyle = LineStyle(
                SolidColor(Color(0, 200, 0)),
                2.dp,
                dashPathEffect(floatArrayOf(20f, 10f))
            )
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun DailyMoodChart(
    modifier: Modifier = Modifier,
    xData: List<Int>,
    yData: List<Float>
) {
    ChartLayout(
        modifier = modifier.padding(16.dp),
    ) {
        var yDataMax = yData.max() + yData.max()/10
        val xAxisModel = rememberIntLinearAxisModel(1..xData.size)
        val yAxisModel = rememberFloatLinearAxisModel(0f..yDataMax)
        xData.size
        var showTextArray = remember { mutableListOf(false) }
        for (i in xData){
            showTextArray.add(false)
        }
        XYGraph(
            xAxisModel = xAxisModel,
            yAxisModel = yAxisModel,
            yAxisLabels = { value -> value.toString() }
        ) {
            VerticalBarPlot(
                modifier = Modifier.padding(25.dp,0.dp,25.dp,0.dp),

                xData = xData,
                yData = yData,
                barWidth = 0.6f,
                bar = {
                    val color = when (yData[it]) {
                        in 0f..1.65f -> Color(0xFFF2554A)
                        in 1.66f..3.3f -> Color(0xFFFFB31A)
                        else -> Color(0xFF8FBF41)
                    }

                    Column {
                        var showText = remember { mutableStateOf(false) }
                        if (showText.value) {
                            Text(yData[it].toString())
                        }
                        DefaultVerticalBar(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.type == PointerEventType.Press) {
                                            event.changes.forEach {
                                                showText.value = true
                                            }
                                        }
                                        if (event.type == PointerEventType.Release) {
                                            event.changes.forEach {
                                                showText.value = false
                                            }
                                        }
                                    }
                                }
                            }
                            ,
                            border = BorderStroke(1.dp, color = Color.Black),
                            brush = SolidColor(color)
                        )
                    }
                }
            )
        }
    }
}