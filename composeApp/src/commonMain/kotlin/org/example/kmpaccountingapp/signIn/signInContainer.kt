package com.example.accountingappwithcompose.android.screens.signIn

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.accountingappwithcompose.android.commonCompose.CommonButton
import com.example.accountingappwithcompose.android.commonCompose.CommonDivider

@Composable
fun AllScreens(successfulLogin: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){

        composable(route = "login") {
            SignInContainer({
                navController.navigate("otp")
            })
        }

        composable(route = "otp") {
            OtpScreenContainer({
                successfulLogin()
                navController.navigate("login")
            })
        }
    }
}


@Composable
fun OtpScreenContainer(otpVerified: () -> Unit) {
    // Number of OTP fields
    val otpLength = 6

    // State for storing OTP digits
    val otpValues = remember { List(otpLength) { mutableStateOf("") } }
    var otpError by remember { mutableStateOf(false) }
    var otpMessage by remember { mutableStateOf("") }

    // Create focus requesters for each field
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    // Combine OTP values into a single string
    val otpCode = otpValues.joinToString("") { it.value }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verification Code",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please enter the verification code sent to your phone",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // OTP fields row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until otpLength) {
                OtpTextField(
                    value = otpValues[i].value,
                    isError = otpError,
                    onValueChange = { newValue ->
                        // Only accept a single digit
                        if (newValue.length <= 1 && (newValue.isEmpty() || newValue.all { it.isDigit() })) {
                            otpValues[i].value = newValue

                            // If a digit is entered and not the last field, move focus to next field
                            if (newValue.isNotEmpty() && i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    focusRequester = focusRequesters[i],
                    // Allow backspace to move to previous field
                    onBackspace = {
                        if (otpValues[i].value.isEmpty() && i > 0) {
                            focusRequesters[i - 1].requestFocus()
                        }
                    }
                )
            }
        }

        if (otpError) {
            Text(
                text = otpMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        CommonButton(
            label = "Verify",
            onButtonClick = {
                // Handle verification logic here
                println("Verifying OTP: $otpCode")
                if(otpCode.length != 6){
                    otpError = true
                    otpMessage = "Please enter all 6 digits"
                }
                else if(otpCode != "111111"){
                    otpMessage = "Invalid verification code"
                    otpError = true
                }
                else
                    otpVerified()
            },
            isEnabled = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Resend code option
        TextButton(onClick = { /* Resend code logic */ }) {
            Text(text = "Resend Code")
        }
    }

    // Set initial focus to the first field
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError:Boolean,
    focusRequester: FocusRequester,
    onBackspace: () -> Unit
) {
    val isFilled = value.isNotEmpty()

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .size(60.dp)
            .onKeyEvent {
                if (it.toString().contains("KEYCODE_DEL") && value.isEmpty()) {
                onBackspace()
                true
            } else {
                false
            }
            }
            .focusRequester(focusRequester)
            .background(
                if (isError) Color(0xFFFFEBEE)
                else if (isFilled) Color(0xFFE3F2FD)
                else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (isError) Color.Red
                else if (isFilled) MaterialTheme.colorScheme.primary
                else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            ),
        textStyle = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = if (isFilled) Color.LightGray.copy(alpha = 0.1f) else Color.Transparent,
            unfocusedContainerColor = if (isFilled) Color.LightGray.copy(alpha = 0.1f) else Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun SignInContainer(signIn : () -> Unit = {}) {
    var country by remember { mutableStateOf("+91") }
    var mobileNo by remember { mutableStateOf("") }
    Column() {
        Box(modifier = Modifier.weight(0.6f).fillMaxSize()
            .clip(shape = RoundedCornerShape(0,0,5,5)).background(MaterialTheme.colorScheme.primaryContainer)) {
        }
        Box(modifier = Modifier) {
            Column  {
                Text("Hey there!")
                Text("Let’s get yoooou in!")
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.weight(1f).padding(top = 10.dp)) {
            Box(modifier = Modifier) {

                Row(horizontalArrangement = Arrangement.Center) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(80.dp)
                            .padding(end = 10.dp)
                            .border(1.dp, Color.Red)
                    )
                    TextField(
                        leadingIcon = {
                            Text(country ,
                                fontWeight = FontWeight.Bold)
                        },
                        value = mobileNo,
                        placeholder = {
                            Text("Enter your mobile number")
                        },
                        onValueChange = {
                            mobileNo = it
                        },
                        modifier = Modifier.height(50.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray,
                            unfocusedContainerColor = Color.LightGray,
                            disabledContainerColor = Color.LightGray,
                            focusedIndicatorColor = Color.LightGray, //hide the indicator
                            unfocusedIndicatorColor = Color.LightGray,
                        )
                    )
                }
            }

            CommonButton(label = "Continue", onButtonClick = { signIn() }, true,Modifier.padding(top = 10.dp))
            CommonDivider("OR")
//            GoogleSignInButton()
        }
    }
}

//@Composable
//fun GoogleSignInButton(){
//
//    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
//    val launcher = rememberFirebaseAuthLauncher(
//        onAuthComplete = { result ->
//            user = result.user
//        },
//        onAuthError = {
//            user = null
//        }
//    )
//    val token = stringResource(R. default_web_client_id)
//    val context = LocalContext.current
//
//    CommonButton(
//        label = "Sign in via Google", onButtonClick = {
//            val gso =
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(token)
//                    .requestEmail()
//                    .build()
//            val googleSignInClient = GoogleSignIn.getClient(context, gso)
//            launcher.launch(googleSignInClient.signInIntent)
//        }, true, Modifier
//    )
//}
//
//
//@Composable
//fun rememberFirebaseAuthLauncher(
//    onAuthComplete: (AuthResult) -> Unit,
//    onAuthError: (ApiException) -> Unit
//): ManagedActivityResultLauncher<Intent, ActivityResult> {
//    val scope = rememberCoroutineScope()
//    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//            scope.launch {
//                val authResult = Firebase.auth.signInWithCredential(credential).await()
//                onAuthComplete(authResult)
//            }
//        } catch (e: ApiException) {
//            onAuthError(e)
//        }
//    }
//}