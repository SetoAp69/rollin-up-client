package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.uistate.CreateEditUserUiState

@Composable
fun CreateEditUserStateHandler(
    uiState: CreateEditUserUiState,
    onResetMessageState:()->Unit,
    onResetForm:()->Unit,
    onShowSnackBar: OnShowSnackBar,
    onDismissRequest: (Boolean) -> Unit,
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

    HandleState(
        state = uiState.submitState,
        successMsg = successMessage,
        errorMsg = errorMessage,
        onDispose = onResetMessageState,
        onSuccess ={
            if(!uiState.isStay){
                onDismissRequest(false)
            }else{
                onResetForm()
            }
        } ,
        onShowSnackBar = onShowSnackBar
    )
}