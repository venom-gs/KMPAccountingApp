package org.example.kmpaccountingapp.LoggedInViewContainer

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kmpaccountingapp.composeapp.generated.resources.Res
import kmpaccountingapp.composeapp.generated.resources.compose_multiplatform
import org.example.kmpaccountingapp.LoggedInViewContainer.HomeScreenContainer.HomeScreenContainer
import org.example.kmpaccountingapp.LoggedInViewContainer.ProfileScreenContainer.ProfileScreenContainer
import org.example.kmpaccountingapp.commonCompose.bottomBorder
import org.example.kmpaccountingapp.commonCompose.topBorder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoggedInViewContainer(){

    Box(modifier = Modifier.fillMaxSize()) {
        val navControllerWholeScreen = rememberNavController()
        NavHost(navController = navControllerWholeScreen, startDestination = "Home",
            enterTransition = {
                // you can change whatever you want transition
                EnterTransition.None
            },
            exitTransition = {
                // you can change whatever you want transition
                ExitTransition.None
            })  {
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
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),modifier = Modifier.weight(0.07f).fillMaxSize(),
                shape = RoundedCornerShape(0.dp),elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(){
                    Image(modifier = Modifier.align(Alignment.Center).clickable(
                        onClick = {
                            navControllerWholeScreen.navigate("Profile")
                        }
                    ),
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "Content description for visually impaired",
                )
                }
            }
        }
        Box(modifier = Modifier.weight(0.86f).fillMaxSize()) {
            NavHost(navController = navController, startDestination = "Home",
                enterTransition = {
                    // you can change whatever you want transition
                    EnterTransition.None
                },
                exitTransition = {
                    // you can change whatever you want transition
                    ExitTransition.None
                }) {
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
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),modifier = Modifier.weight(0.07f).fillMaxSize().topBorder(color = Color.Black, height = 1f),
            shape = RoundedCornerShape(0.dp),elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )) {
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