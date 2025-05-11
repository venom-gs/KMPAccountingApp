package org.example.kmpaccountingapp.LoggedInViewContainer.HomeScreenContainer

import LineChartSample
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accountingappwithcompose.android.commonCompose.CommonNavigationButton
import org.example.kmpaccountingapp.commonCompose.PieChartExample
import org.example.kmpaccountingapp.commonCompose.ProgressBarChartWithPopups

@Composable
fun HomeScreenContainer(){
    val navController = rememberNavController()
LazyColumn {
    item{
        NavigationButtonContainer(navController)
    }
    item {
        NavHost(navController = navController, startDestination = "dashboard") {
            composable(route = "dashboard") {
                HomeDashBoard()
            }
            composable(route = "Sales") {

            }
            composable(route = "Purchase") {

            }
            composable(route = "Accounting") {

            }
        }
        }
}
}
@Composable
fun NavigationButtonContainer(navController : NavController){
    LazyRow (modifier = Modifier.fillMaxWidth()) {
        item {
            CommonNavigationButton("Receivable",{
                navController.navigate("dashboard")
            })
        }
        item {
            CommonNavigationButton("Receivable", {
                navController.navigate("Sales")})
        }
        item {
            CommonNavigationButton("Sales",{})
        }
        item {
            CommonNavigationButton("Sales", {})
        }
    }
}


