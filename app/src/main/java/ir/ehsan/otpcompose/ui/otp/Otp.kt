package ir.ehsan.otpcompose.ui.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import ir.ehsan.otpcompose.utils.extensions.each

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
