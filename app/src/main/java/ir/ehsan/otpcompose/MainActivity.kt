package ir.ehsan.otpcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsan.otpcompose.ui.theme.OtpComposeTheme

@Composable
fun <T> State(value: T) = remember {
    mutableStateOf(value)
}

val errorColor = Color(0xFFDF3838)
val successColor = Color(0xFF19D674)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtpComposeTheme {

//                val color = animateColorAsState(
//                    targetValue =
//                    if (error.value)
//                        errorColor
//                    else if (success.value)
//                        successColor
//                    else
//                        Color.White,
//                    animationSpec = tween(400)
//                )


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        var error = State(false)
                        var success = State(false)
                        val correctOtp = "12345"
                        Otp(
                            onOtpChange = { otp, finish ->
                                if (!finish) {
                                    success.value = false
                                    error.value = false
                                }
                            },
                            onFinish = {
                                success.value = it == correctOtp
                                error.value = it != correctOtp
                            },
                            success = success.value,
                            error = error.value
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Otp(
    count: Int = 5,
    onOtpChange: (String, Boolean) -> Unit = { _, _ -> },
    onFinish: (String) -> Unit = {},
    error: Boolean = false,
    success: Boolean = false
) {

    val otpState = remember {
        mutableStateListOf(
            *MutableList(count) {
                OtpValue(focused = it == 0)
            }.toTypedArray()
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        count.each {
            OtpBox(
                otpValue = otpState[it],
                onValueChange = { newValue ->
                    if (otpState[it].value != newValue) {
                        if (newValue.length <= 1) {
                            otpState[it] = otpState[it].copy(value = newValue)
                            if (newValue.length == 1) {
                                if (otpState.size - 1 > it) {
                                    otpState[it + 1].focusRequester?.requestFocus()
                                }
                            }
                        }
                        var otp = ""
                        otpState.forEach {
                            otp += it.value
                        }
                        if (otp.length >= count)
                            onFinish(otp)
                        onOtpChange(otp, otp.length >= count)
                    }

                },
                onFocusChanged = { focused ->
                    otpState[it] = otpState[it].copy(focused = focused)
                },
                onFocusSet = { focus ->
                    otpState[it] = otpState[it].copy(focusRequester = focus)
                },
                onBackSpace = {
                    if (otpState[it].value.isEmpty()) {
                        if (it != 0) {
                            otpState[it - 1].focusRequester?.requestFocus()
                        }
                    }
                },
                error = error,
                success = success
            )
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpBox(
    otpValue: OtpValue,
    onValueChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onBackSpace: () -> Unit,
    textSize: TextUnit = 25.sp,
    onFocusSet: (FocusRequester) -> Unit,
    error: Boolean,
    success: Boolean
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        onFocusSet(focusRequester)
    }
    val color by animateColorAsState(
        targetValue = if (error)
            errorColor
        else if (success)
            successColor
        else if (otpValue.focused)
            Color.Black
        else
            Color.Gray,
        animationSpec = tween(300)
    )
    val textColor by animateColorAsState(
        targetValue = if (otpValue.focused) Color.Black else Color.Gray,
        animationSpec = tween(300)
    )
    Box(
        modifier = Modifier
            .border(1.dp, color, RoundedCornerShape(8))
            .size(50.dp, 70.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.isFocused) }
                .onKeyEvent {
                    if (it.key.keyCode == Key.Backspace.keyCode) {
                        onBackSpace()
                    }
                    false
                },
            value = otpValue.value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                color = textColor,
                fontSize = textSize,
                textAlign = TextAlign.Center
            ),
        )
    }
}

data class OtpValue(
    val value: String = "",
    var focused: Boolean = false,
    val focusRequester: FocusRequester? = null,
)

@Composable
fun Int.each(block: @Composable (Int) -> Unit) {
    for (i in 0 until this) {
        block(i)
    }
}