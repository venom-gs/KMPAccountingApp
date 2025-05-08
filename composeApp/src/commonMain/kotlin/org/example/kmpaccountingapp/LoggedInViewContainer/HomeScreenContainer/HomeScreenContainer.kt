package org.example.kmpaccountingapp.LoggedInViewContainer.HomeScreenContainer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import org.example.kmpaccountingapp.commonCompose.PieChartExample
import org.example.kmpaccountingapp.commonCompose.ProgressBarChartWithPopups

@Composable
fun HomeScreenContainer(){
LazyColumn (modifier = Modifier.fillMaxHeight()){
    item {
        TotalAmountContainerCommon("Total Receivable")
    }
    item {
        TotalAmountContainerCommon("Total Payables")
    }
    item {
        PieChartExample()
    }
}
}

@Composable
fun TotalAmountContainerCommon(header : String){

    Card(
        modifier = Modifier.padding(16.dp).background(color = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    ) {
        Column() {
            Box(modifier = Modifier.border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp,8.dp,0.dp,0.dp)).fillMaxWidth(),) {
                Text(text = header, fontSize = 25.sp, modifier = Modifier.padding(9.dp), fontWeight = FontWeight.Medium)
            }
            Box(modifier = Modifier.border(1.dp, color = Color.LightGray)) {
                Column(Modifier.padding(9.dp)) {
                    if (header == "Total Receivable")
                        Text("Total Unpaid Invoices")
                    else
                        Text("Total Unpaid Bills")
                    ProgressBarChartWithPopups()
                }
            }
            Box() {
                Row(Modifier.fillMaxWidth().border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(0.dp,0.dp,8.dp,8.dp))) {
                    Box(Modifier.weight(0.5f).padding(9.dp)) {
                        Column {
                            Text("Current")
                            Text("621837")
                        }
                    }
                    Box(Modifier.weight(0.5f).padding(9.dp)) {
                        Column {
                            Text("OverDue")
                            Text("621837")
                        }
                    }
                }
            }
        }
    }
}


