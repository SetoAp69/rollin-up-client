package com.rollinup.rollinup.component.profile.profilepopup.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.profile.ProfileInfoField
import com.rollinup.rollinup.component.profile.profilepopup.uistate.ProfileDialogUiState
import com.rollinup.rollinup.component.profile.profilepopup.viemodel.ProfileDialogViewModel
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_calendar_fill_24
import rollin_up.composeapp.generated.resources.ic_female_line_24
import rollin_up.composeapp.generated.resources.ic_home_line_24
import rollin_up.composeapp.generated.resources.ic_id_card_line_24
import rollin_up.composeapp.generated.resources.ic_mail_user_line_24
import rollin_up.composeapp.generated.resources.ic_male_line_24
import rollin_up.composeapp.generated.resources.ic_phone_line_24
import rollin_up.composeapp.generated.resources.ic_user_board_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24

@Composable
fun ProfileDialog(
    id: String,
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    val viewModel: ProfileDialogViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val maxHeight = getScreenHeight() * 0.8f
    val maxWidth = getScreenWidth() * 0.3f
    Dialog(
        showDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        contentPadding = screenPaddingValues,
        modifier = Modifier.sizeIn(maxWidth = maxWidth, maxHeight = maxHeight)
    ) {
        DisposableEffect(Unit) {
            viewModel.init(id)
            onDispose {
                viewModel.reset()
            }
        }
        ProfileDialogContent(uiState)
    }
}

@Composable
fun ProfileDialogContent(
    uiState: ProfileDialogUiState,
) {
    if (uiState.isLoading) {
        ProfileLoading()
    } else {
        Column {
            ProfileDialogHeader(uiState.userDetail)
            Spacer(24.dp)
            ProfileInfoSection(uiState.userDetail)
        }
    }
}

@Composable
private fun ProfileDialogHeader(
    userDetail: UserDetailEntity,
) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            modifier = Modifier
                .background(color = theme.primary, shape = CircleShape)
                .size(44.dp),
            text = userDetail.fullName.take(1),
            style = Style.title,
            color = theme.textBtnPrimary
        )
        Spacer(itemGap8)
        Column {
            Text(
                text = userDetail.fullName,
                style = Style.title,
                color = theme.bodyText
            )
            Spacer(itemGap4)
            Text(
                text = userDetail.userName,
                style = Style.body,
                color = theme.bodyText
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(
    userDetail: UserDetailEntity,
) {
    val genderIcon = when (userDetail.gender) {
        Gender.MALE -> Res.drawable.ic_male_line_24
        Gender.FEMALE -> Res.drawable.ic_female_line_24
    }
    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        maxItemsInEachRow = 2
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Full Name",
                icon = Res.drawable.ic_user_line_24,
                value = userDetail.fullName
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Birth Day",
                icon = Res.drawable.ic_calendar_fill_24,
                value = userDetail.birthDay
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "ID",
                icon = Res.drawable.ic_id_card_line_24,
                value = userDetail.id
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Gender",
                icon = genderIcon,
                value = userDetail.gender.label
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Class",
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.classX?.name ?: "-"
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Phone",
                icon = Res.drawable.ic_phone_line_24,
                value = userDetail.phoneNumber
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Role",
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.role.name
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Address",
                icon = Res.drawable.ic_home_line_24,
                value = userDetail.address
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Email",
                icon = Res.drawable.ic_mail_user_line_24,
                value = userDetail.email
            )
        }
    }
}

@Composable
private fun ProfileLoading() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            ShimmerEffect(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp),
            )
            Spacer(itemGap8)
            Column {
                ShimmerEffect(120.dp)
                Spacer(itemGap4)
                ShimmerEffect(80.dp)
            }
        }
        Spacer(24.dp)
        FlowRow(
            itemVerticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(9) {
                ShimmerEffect(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}
