package org.example.kmpaccountingapp.signIn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.accountingappwithcompose.android.commonCompose.CommonTextField

@Composable
fun CompanyRegistration(){
    Column(Modifier.padding(15.dp)) {
        CommonTextField(
            "",
            "Company Name",
            onTextChange = {}
        )
        CommonTextField(
            "",
            "Company Name",
            onTextChange = {}
        )
        Row() {
            CommonTextField(
                "",
                "Company Name",
                onTextChange = {},
                modifier = Modifier.weight(0.5f)
            )
            CommonTextField(
                "",
                "Company Name",
                onTextChange = {},
                modifier = Modifier.weight(0.5f)
            )
        }
        CommonTextField(
            "",
            "Company Name",
            onTextChange = {}
        )
    }
}