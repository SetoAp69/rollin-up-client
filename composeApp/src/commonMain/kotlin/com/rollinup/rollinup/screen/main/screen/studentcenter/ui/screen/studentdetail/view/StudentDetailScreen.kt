package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentdetail.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.profile.profilepopup.view.screen.ProfileScreen
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.topbar.TopBar
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_student_profile

@Composable
fun StudentProfileScreen(
    id: String,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    Scaffold(
        topBar = {
            TopBar(
                onClickMenu = {},
                menu = emptyList(),
                onNavigateUp = onNavigateUp,
                title = stringResource(Res.string.label_student_profile)
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(
                id = id,
                showEdit = false,
                onShowSnackBar = onShowSnackBar
            )
        }
    }

}