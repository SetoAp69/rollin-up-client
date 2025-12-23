package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel.AttendanceByStudentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceByStudentDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    id: String,
) {
    val viewModel: AttendanceByStudentViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()
    val height = getScreenHeight() * 0.8f
    val width = getScreenWidth() * 0.8f
    val isMobile = getPlatform().isMobile()

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxHeight = height, maxWidth = width)
            .padding(screenPadding * 2)
    ) { showSnackBar ->
        DisposableEffect(showDialog) {
            if (showDialog) viewModel.init(id, isMobile)
            onDispose { viewModel.reset() }
        }
        AttendanceByStudentDialogContent(
            uiState = uiState,
            cb = cb,
            onShowSnackBar = showSnackBar
        )
    }
}