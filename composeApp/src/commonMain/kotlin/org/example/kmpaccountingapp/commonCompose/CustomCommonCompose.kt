package com.example.accountingappwithcompose.android.commonCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
fun CommonButton(label: String, onButtonClick: () -> Unit, isEnabled: Boolean, modifier: Modifier = Modifier){

    Button(modifier = modifier, colors =ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(4.dp), onClick = {onButtonClick()},enabled=isEnabled
    ){
        Text(text=label, color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CommonDivider(dividerText : String){
    Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center,
        modifier = Modifier.height(40.dp)
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(modifier = Modifier
            .height(50.dp)
            .padding(10.dp), text = dividerText)
        Divider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CommonTextField(label: String, text: String,onTextChange: (String) -> Unit ,isEnabled :Boolean,fieldType :String = "randomBullShit"){

    var text by remember { mutableStateOf(text) }
    TextField(
        value = text,
        enabled = isEnabled,
        onValueChange = {
            when(fieldType){
                "randomBullShit" -> text = it
                "number" -> {
                    if (it.matches("^[0-9]*$".toRegex())){
                        text = it
                    }
                }
                "alphabet" ->{
                    if (it.matches("^[a-zA-Z]*$".toRegex())){
                        text = it
                    }
                }
                "textOnly" -> {
                    if (!it.matches("^[0-9]*$".toRegex())){
                        text = it
                    }
                }
            }


            onTextChange(text)},
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            disabledContainerColor = Color.LightGray,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
        ))
}


//var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
//SimpleDatePicker(
//selectedDate = selectedDate,
//onDateSelected = { selectedDate = it },
//modifier = Modifier.fillMaxWidth(),
//label = "Select Date"
//)
@Composable
fun SimpleDatePicker(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Select Date"
) {
    var showPicker by remember { mutableStateOf(false) }

    // Date field
    OutlinedTextField(
        value = selectedDate?.let { "${it.monthNumber}/${it.dayOfMonth}/${it.year}" } ?: "",
        onValueChange = { },
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Open Date Picker",
                modifier = Modifier.clickable { showPicker = true }
            )
        },
        modifier = modifier.clickable { showPicker = true }
    )

    // Date picker dialog
    if (showPicker) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val initialDate = selectedDate ?: today

        DatePickerDialog(
            initialDate = initialDate,
            onDateSelected = {
                onDateSelected(it)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
    }
}

@Composable
fun CommonNavigationButton(label: String, onButtonClick: () -> Unit, isEnabled: Boolean = true, modifier: Modifier = Modifier){
    ElevatedButton(modifier = modifier.padding(3.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 4.dp,
            focusedElevation = 4.dp
        ), colors =ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background), shape = RoundedCornerShape(20.dp), onClick = {onButtonClick()},enabled=isEnabled
    ){
        Text(text=label, color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentDate by remember { mutableStateOf(initialDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date") },
        text = {
            Column {
                // Month and year selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentDate = if (currentDate.month == Month.JANUARY) {
                            LocalDate(currentDate.year - 1, Month.DECEMBER, 1)
                        } else {
                            LocalDate(currentDate.year, currentDate.month.prev(), 1)
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, "Previous Month")
                    }

                    Text("${currentDate.month.name.lowercase().capitalize()} ${currentDate.year}")

                    IconButton(onClick = {
                        currentDate = if (currentDate.month == Month.DECEMBER) {
                            LocalDate(currentDate.year + 1, Month.JANUARY, 1)
                        } else {
                            LocalDate(currentDate.year, currentDate.month.next(), 1)
                        }
                    }) {
                        Icon(Icons.Default.ArrowForward, "Next Month")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Days of week header
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayName in listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")) {
                        Text(
                            text = dayName,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar
                val firstOfMonth = LocalDate(currentDate.year, currentDate.month, 1)
                val startDayOfWeek = firstOfMonth.dayOfWeek.ordinal
                val daysInMonth = getDaysInMonth(currentDate.year, currentDate.month)

                // Create calendar grid
                Column {
                    var dayCounter = 1
                    for (week in 0 until 6) {
                        if (dayCounter > daysInMonth) break

                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (day in 0 until 7) {
                                if ((week == 0 && day < startDayOfWeek) || dayCounter > daysInMonth) {
                                    // Empty space
                                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                                } else {
                                    // Day button
                                    val currentDay = dayCounter
                                    val date = LocalDate(currentDate.year, currentDate.month, currentDay)
                                    val isSelected = date.year == initialDate.year &&
                                            date.month == initialDate.month &&
                                            date.dayOfMonth == initialDate.dayOfMonth

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.surface
                                            )
                                            .clickable {
                                                onDateSelected(date)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = currentDay.toString(),
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    dayCounter++
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDateSelected(currentDate) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Helper functions
private fun getDaysInMonth(year: Int, month: Month): Int {
    return when (month) {
        Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

private fun Month.next(): Month {
    val nextOrdinal = (ordinal + 1) % 12
    return Month.entries[nextOrdinal]
}

private fun Month.prev(): Month {
    val prevOrdinal = (ordinal + 11) % 12
    return Month.entries[prevOrdinal]
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}