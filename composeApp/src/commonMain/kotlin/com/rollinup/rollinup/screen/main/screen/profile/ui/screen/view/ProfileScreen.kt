package com.rollinup.rollinup.screen.main.screen.profile.ui.screen.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.profile.profilepopup.view.screen.ProfileScreen
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.localUser

@Composable
fun ProfileScreen(
    onShowSnackBar: OnShowSnackBar,
) {
    val id = localUser?.id ?: ""
    Scaffold {
        ProfileScreen(
            id = id,
            showEdit = true,
            onShowSnackBar = onShowSnackBar
        )
    }
}