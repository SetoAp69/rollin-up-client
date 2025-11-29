package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentdetail.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.profile.profilepopup.view.screen.ProfileScreen
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.topbar.TopBar

@Composable
fun StudentProfileScreen(
    id: String,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                onClickMenu = {},
                menu = emptyList(),
                onNavigateUp = onNavigateUp,
                title = "Student Profile"
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(
                id = id,
                showEdit =false
            )
        }
    }

}