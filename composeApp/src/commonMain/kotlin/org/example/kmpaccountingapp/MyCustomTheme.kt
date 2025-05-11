package org.example.kmpaccountingapp


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyCustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF11512F),
            primaryContainer = Color(0xFF33EB89),
            secondary = Color(0xFF33EB89),
            tertiary = Color(0xFF3700B3)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFB9FD66),
            primaryContainer = Color(0xFF223D22),
            secondary = Color(0xFFB9FD66),
            tertiary = Color(0xFF3700B3)
        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MyCustomTheme(
        content = content
    )
}



// Define light theme colors
private val LightColorPalette = lightColorScheme(
    primary = Color(0xFFB9FD66),
    primaryContainer = Color(0xFF223D22),
    secondary = Color(0xFFB9FD66),
    tertiary = Color(0xFF3700B3),
    background = androidx.compose.ui.graphics.Color(0xFFFCFFFF),
    surface = androidx.compose.ui.graphics.Color(0xFF97A5A6),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF000000),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF000000),
    onBackground = androidx.compose.ui.graphics.Color(0xFF000000),
    onSurface = androidx.compose.ui.graphics.Color(0xFF000000)
)

//// Define light theme colors
//private val LightColorPalette = lightColorScheme(
//    primary = Color(0xFFB9FD66),
//    primaryContainer = Color(0xFF223D22),
//    secondary = Color(0xFFB9FD66),
//    tertiary = Color(0xFF3700B3),
//    background = androidx.compose.ui.graphics.Color(0xEDFBFC),
//    surface = androidx.compose.ui.graphics.Color(0xc4c4c4),
//    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
//    onSecondary = androidx.compose.ui.graphics.Color(0xFF000000),
//    onBackground = androidx.compose.ui.graphics.Color(0x97A5A6),
//    onSurface = androidx.compose.ui.graphics.Color(0xFF000000)
//)

// Define dark theme colors
private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF11512F),
    primaryContainer = Color(0xFF33EB89),
    secondary = Color(0xFF33EB89),
    tertiary = Color(0xFF3700B3),
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    surface = androidx.compose.ui.graphics.Color(0x383838),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF000000),
    onBackground = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
)


@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    var darkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        content = content
    )
}

