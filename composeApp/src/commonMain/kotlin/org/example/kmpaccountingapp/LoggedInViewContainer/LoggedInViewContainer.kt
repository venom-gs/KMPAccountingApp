package org.example.kmpaccountingapp.LoggedInViewContainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.kmpaccountingapp.LoggedInViewContainer.HomeScreenContainer.HomeScreenContainer
import org.example.kmpaccountingapp.LoggedInViewContainer.ProfileScreenContainer.ProfileScreenContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoggedInViewContainer(){

    Box(modifier = Modifier.fillMaxSize()) {
        val navControllerWholeScreen = rememberNavController()
        NavHost(navController = navControllerWholeScreen, startDestination = "Home") {
            composable(route = "Home") {
                AllViewContainer(navControllerWholeScreen)
        }
            composable(route = "Profile") {
                ProfileScreenContainer()
            }
        }

    }
}
@Composable
fun AllViewContainer(navControllerWholeScreen :NavController){
    Column(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        Box(modifier = Modifier.weight(0.07f).fillMaxSize().background(color = MaterialTheme.colorScheme.surface)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    navControllerWholeScreen.navigate("Profile")
                }) {
                    Text("Profile")
                }
            }
        }
        Box(modifier = Modifier.weight(0.93f).fillMaxSize()) {
            NavHost(navController = navController, startDestination = "Home") {
                composable(route = "Home") {
                    HomeScreenContainer()
                }
                composable(route = "Sales") {

                }
                composable(route = "Purchase") {

                }
                composable(route = "Accounting") {

                }
            }
        }
        Box(modifier = Modifier.weight(0.07f).fillMaxSize().background(color = MaterialTheme.colorScheme.surface)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    navController.navigate("Home")
                }) {
                    Text("Home")
                }
                Button(onClick = {
                    navController.navigate("Sales")
                }) {
                    Text("Sales")
                }
                Button(onClick = {
                    navController.navigate("Purchase")
                }) {
                    Text("Purc")
                }
                Button(onClick = {
                    navController.navigate("Accounting")
                }) {
                    Text("Acc")
                }
            }
        }
    }
}