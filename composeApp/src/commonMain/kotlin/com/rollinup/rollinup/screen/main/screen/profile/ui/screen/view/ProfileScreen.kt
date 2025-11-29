package com.rollinup.rollinup.screen.main.screen.profile.ui.screen.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.profile.profilepopup.view.screen.ProfileScreen
import com.rollinup.rollinup.component.theme.localUser

@Composable
fun ProfileScreen() {
    val id = localUser?.id ?: ""
    ProfileScreen(
        id = id,
        showEdit = true
    )
}