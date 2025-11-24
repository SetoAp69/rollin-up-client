package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.auth.model.login.LoginCallback
import com.rollinup.rollinup.screen.auth.model.login.LoginFormData
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate.LoginUiState


@Composable
fun LoginContentCompact(
    onNavigateTo: (String) -> Unit,
    uiState: LoginUiState,
    cb: LoginCallback,
) {
    Column {
        LoginHeader()
        Spacer(24.dp)
        LoginForm(
            formData = uiState.loginFormData,
            onUpdateForm = cb.onUpdateForm,
            onSubmit = cb.onLogin,
        )
        Spacer(36.dp)
        Box(
            modifier = Modifier
                .clickable() {
                    onNavigateTo(AuthNavigationRoute.ResetPassword.route)
                }
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "I forgot my password",
                style = Style.body,
                color = theme.textPrimary
            )
        }
        Box(
            modifier = Modifier
                .clickable() {
                    onNavigateTo(com.rollinup.rollinup.navigation.NavigationRoute.TestRoute.route)
                }
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Go to Test Screen",
                style = Style.body,
                color = theme.textPrimary
            )
        }
    }
}

@Composable
private fun LoginForm(
    formData: LoginFormData,
    onUpdateForm: (LoginFormData) -> Unit,
    onSubmit: (LoginFormData) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(screenPaddingValues)
    ) {
        TextField(
            placeholder = "Enter your email/username",
            value = formData.email,
            maxChar = 30,
            onValueChange = { value ->
                onUpdateForm(
                    formData.copy(
                        email = value,
                        emailError = null
                    )
                )
            },
            isError = formData.emailError != null,
            errorMsg = formData.emailError
        )
        Spacer(itemGap8)
        PasswordTextField(
            value = formData.password,
            onValueChange = { value ->
                onUpdateForm(
                    formData.copy(
                        password = value,
                        passwordError = null
                    )
                )
            },
            placeholder = "Enter your password",
            modifier = Modifier.fillMaxWidth(),
            isError = formData.passwordError != null,
            errorMsg = formData.passwordError
        )
        Spacer(26.dp)
        Button(
            type = ButtonType.FILLED,
            severity = Severity.PRIMARY,
            onClick = {
                onSubmit(formData)
            },
            text = "Login",
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun LoginHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
//            .background(
//                shape = RoundedCornerShape(bottomEndPercent = 50, bottomStartPercent = 50),
//                color = theme.primary
//            )
        ,
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds,
//            colorFilter = ColorFilter.tint(color = theme.primary, blendMode = BlendMode.Color),
//            painter = painterResource(Res.drawable.wavy_header_background),
//            contentDescription = null,
//        )
        LoginBackgroundShape()
    }
}
