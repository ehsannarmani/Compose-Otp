package ir.ehsan.otpcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.ehsan.otpcompose.ui.otp.Otp
import ir.ehsan.otpcompose.ui.theme.OtpComposeTheme
import ir.ehsan.otpcompose.utils.State


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


