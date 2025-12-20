package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.textfield.PasswordTextField
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.navigation.NavigationRoute
import com.rollinup.rollinup.screen.auth.model.login.LoginCallback
import com.rollinup.rollinup.screen.auth.model.login.LoginFormData
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate.LoginUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_login
import rollin_up.composeapp.generated.resources.msg_forgot_password
import rollin_up.composeapp.generated.resources.ph_password
import rollin_up.composeapp.generated.resources.ph_username_email

@Composable
fun LoginContentWide(
    uiState: LoginUiState,
    cb: LoginCallback,
    onNavigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .width(600.dp)
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginForm(
                    onUpdateValue = cb.onUpdateForm,
                    formData = uiState.loginFormData,
                    onSubmit = cb.onLogin,
                    onNavigateTo = onNavigateTo
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(color = theme.primary)
                ) {
                    Text(
                        text = "To Test Screen",
                        color = theme.bodyText,
                        style = Style.title,
                        modifier = Modifier.clickable() {
                            onNavigateTo(NavigationRoute.TestRoute.route)
                        }

                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.LoginForm(
    onUpdateValue: (LoginFormData) -> Unit,
    onNavigateTo: (String) -> Unit,
    formData: LoginFormData,
    onSubmit: (LoginFormData) -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.label_login),
            style = Style.header,
            color = theme.bodyText,
        )
        TextField(
            title = "",
            value = formData.email,
            onValueChange = { value ->
                onUpdateValue(
                    formData.copy(
                        email = value,
                        emailError = null
                    )
                )

            },
            placeholder = stringResource(Res.string.ph_username_email),
            isError = formData.emailError != null,
            errorMsg = formData.emailError,
        )
        PasswordTextField(
            value = formData.password,
            onValueChange = { value ->
                onUpdateValue(
                    formData.copy(
                        password = value,
                        passwordError = null
                    )
                )
            },
            placeholder =stringResource(Res.string.ph_password),
            modifier = Modifier.fillMaxWidth(),
            isError = formData.passwordError != null,
            errorMsg = formData.passwordError,
        )
        Button(
            text = stringResource(Res.string.label_login),
            type = ButtonType.FILLED,
            severity = Severity.PRIMARY,
            onClick = {
                onSubmit(formData)
            },
        )
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onNavigateTo(AuthNavigationRoute.ResetPassword.route)
                }
        ) {
            Text(
                text = stringResource(Res.string.msg_forgot_password),
                style = Style.body,
                color = theme.textPrimary
            )
        }
    }
}