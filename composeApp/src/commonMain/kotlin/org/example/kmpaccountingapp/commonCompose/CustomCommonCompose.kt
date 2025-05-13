package com.example.accountingappwithcompose.android.commonCompose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(modifier = Modifier
            .height(50.dp)
            .padding(10.dp), text = dividerText)
        Divider(modifier = Modifier.weight(1f))
    }
}

//@Composable
//fun CommonTextField(label: String, text: String = "",onTextChange: (String) -> Unit ,isEnabled :Boolean = true,fieldType :String = "randomBullShit"){
//
//    var text by remember { mutableStateOf(text) }
//    TextField(
//        value = text,
//        enabled = isEnabled,
//        onValueChange = {
//            when(fieldType){
//                "randomBullShit" -> text = it
//                "number" -> {
//                    if (it.matches("^[0-9]*$".toRegex())){
//                        text = it
//                    }
//                }
//                "alphabet" ->{
//                    if (it.matches("^[a-zA-Z]*$".toRegex())){
//                        text = it
//                    }
//                }
//                "textOnly" -> {
//                    if (!it.matches("^[0-9]*$".toRegex())){
//                        text = it
//                    }
//                }
//            }
//
//
//            onTextChange(text)},
//        label = { Text(label) },
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color.LightGray,
//            unfocusedContainerColor = Color.LightGray,
//            disabledContainerColor = Color.LightGray,
//            focusedIndicatorColor = Color.LightGray,
//            unfocusedIndicatorColor = Color.LightGray,
//        ))
//}


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
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next Month")
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



@Composable
fun CommonTextField(
    text: String,
    label: String,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    modifier: Modifier = Modifier
    ,onTextChange: (String) -> Unit ,fieldType :String = "randomBullShit") {
    var text by remember { mutableStateOf(text) }
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                when(fieldType){
                    "randomBullShit" -> {text = it}
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
            label = { Text(text = label) },
            placeholder = { Text(text = placeholder) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = Color.White,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = Color.Gray,
                cursorColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview()
@Composable
fun TextFieldsPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Standard Text Field
            var text1 by remember { mutableStateOf("") }
            CommonTextField(
                text = text1,
                label = "Username",
                placeholder = "Enter your username",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ) ,onTextChange = {}
            )

            // Password Field
            var text2 by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            CommonTextField(
                text = text2,
                label = "Password",
                placeholder = "Enter your password",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Email,

                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ) ,onTextChange = {}
            )

            // Error State Text Field
            var text3 by remember { mutableStateOf("invalid@email") }
            CommonTextField(
                text = text3,
                label = "Email",
                placeholder = "Enter your email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                isError = false,
                errorMessage = "Please enter a valid email address",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ) ,onTextChange = {}
            )

            // Multi-line Text Field
            var text4 by remember { mutableStateOf("") }
            CommonTextField(
                text = text4,
                label = "Bio",
                placeholder = "Tell us about yourself",
                singleLine = false,
                maxLines = 4,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ) ,onTextChange = {}
            )
        }
    }
}

@Composable
fun StylishSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumbContent: @Composable (() -> Unit)? = null
) {
    // Use the Material 3 Switch component with custom colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            checkedBorderColor = MaterialTheme.colorScheme.primary,
            checkedIconColor = MaterialTheme.colorScheme.onPrimary,
            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            uncheckedBorderColor = MaterialTheme.colorScheme.outline,
            uncheckedIconColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledCheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledCheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledCheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledCheckedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledUncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledUncheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledUncheckedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    )
}

/**
 * A labeled switch component that includes text and a styled switch.
 */
@Composable
fun LabeledSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        StylishSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

/**
 * Preview various states of the switches
 */
@Composable
fun SwitchesPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic switches using Material 3 Switch
            Text(
                text = "Material 3 Switches",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                var checked1 by remember { mutableStateOf(false) }
                StylishSwitch(
                    checked = checked1,
                    onCheckedChange = { checked1 = it }
                )

                var checked2 by remember { mutableStateOf(true) }
                StylishSwitch(
                    checked = checked2,
                    onCheckedChange = { checked2 = it }
                )

                StylishSwitch(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false
                )

                StylishSwitch(
                    checked = false,
                    onCheckedChange = {},
                    enabled = false
                )
            }

            Divider()

            // Labeled switches
            Text(
                text = "Labeled Switches",
                style = MaterialTheme.typography.titleMedium
            )

            var darkMode by remember { mutableStateOf(false) }
            LabeledSwitch(
                checked = darkMode,
                onCheckedChange = { darkMode = it },
                label = "Dark Mode"
            )

            var notifications by remember { mutableStateOf(true) }
            LabeledSwitch(
                checked = notifications,
                onCheckedChange = { notifications = it },
                label = "Enable Notifications"
            )

            var airplane by remember { mutableStateOf(false) }
            LabeledSwitch(
                checked = airplane,
                onCheckedChange = { airplane = it },
                label = "Airplane Mode",
                enabled = false
            )
        }
    }
}

@Composable
fun StylishDropdown(
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        Box {
            // Create a custom field using Surface instead of OutlinedTextField
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (enabled) {
                        if (expanded) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (expanded) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedIndex >= 0 && selectedIndex < items.size) items[selectedIndex] else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (enabled) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = icon,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onSelectionChanged(index)
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StylishSearchableDropdown(
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    // Update search text when selection changes
    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < items.size) {
            searchText = items[selectedIndex]
        }
    }

    // Filter items based on search text
    val filteredItems = remember(searchText, items) {
        if (searchText.isEmpty()) {
            items
        } else {
            items.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    Column(modifier = modifier) {
        Box {
            // Custom searchable field using TextField
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (enabled) {
                        if (expanded) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (expanded) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                expanded = true
                            },
                            enabled = enabled,
                            modifier = Modifier.weight(1f),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = if (enabled) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        )

                        Icon(
                            imageVector = icon,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            modifier = Modifier.clickable(enabled = enabled) {
                                expanded = !expanded
                            }
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                filteredItems.forEachIndexed { index, item ->
                    val originalIndex = items.indexOf(item)
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onSelectionChanged(originalIndex)
                            searchText = item
                            expanded = false
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                if (filteredItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching items",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview of the dropdown components
 */
@Composable
fun DropdownsPreview() {
    val countries = listOf(
        "United States", "Canada", "United Kingdom", "Australia",
        "Germany", "France", "Italy", "Spain", "Japan", "China",
        "Brazil", "India", "Russia", "South Africa", "Mexico"
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Standard Dropdown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            var selectedCountryIndex by remember { mutableStateOf(0) }
            StylishDropdown(
                label = "Select Country",
                items = countries,
                selectedIndex = selectedCountryIndex,
                onSelectionChanged = { selectedCountryIndex = it }
            )

            Divider()

            Text(
                text = "Searchable Dropdown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            var selectedSearchableCountryIndex by remember { mutableStateOf(0) }
            StylishSearchableDropdown(
                label = "Search Country",
                items = countries,
                selectedIndex = selectedSearchableCountryIndex,
                onSelectionChanged = { selectedSearchableCountryIndex = it }
            )

            Divider()

            Text(
                text = "Disabled Dropdown",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            StylishDropdown(
                label = "Disabled Dropdown",
                items = countries,
                selectedIndex = 0,
                onSelectionChanged = { },
                enabled = false
            )
        }
    }
}