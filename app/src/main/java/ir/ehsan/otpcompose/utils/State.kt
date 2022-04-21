package ir.ehsan.otpcompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> State(value: T) = remember {
    mutableStateOf(value)
}