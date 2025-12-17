package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateEditUserStateHandler(
    uiState: CreateEditUserUiState,
    onResetMessageState:()->Unit,
    onResetForm:()->Unit,
    onShowSnackBar: OnShowSnackBar,
    onDismissRequest: (Boolean) -> Unit,
    onSuccess:()->Unit,
) {
    val successMessage =
        if(uiState.isEdit){
            "Success, User data successfully edited"
        }else{
            "Success, User data successfully created"
        }

    val errorMessage =
        if(uiState.isEdit){
            "Error, failed to edit user data"
        }else{
            "Error, failed to create user data"
        }

    val scope = rememberCoroutineScope()

    HandleState(
        state = uiState.submitState,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onDispose = onResetMessageState,
        onSuccess ={
            scope.launch{
                onSuccess()
                if (!uiState.isStay) {
                    delay(1000)
                    onDismissRequest(false)
                }
                onResetForm()
            }
        } ,
        onShowSnackBar = onShowSnackBar
    )
}