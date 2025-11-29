package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.profile.ProfileInfoField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
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
fun AttendanceByStudentProfileMobile(
    user: UserDetailEntity,
    isLoading: Boolean,
) {
    if (isLoading) {
        AttendanceByStudentProfileHeaderLoading()
    } else {
        AttendanceProfileHeaderContent(user)
    }
}

@Composable
fun AttendanceByStudentProfileDesktop(
    user: UserDetailEntity,
    isLoading: Boolean,
) {
    if (isLoading) {
        AttendanceByStudentProfileDesktopLoading()
    } else {
        AttendanceByStudentProfileDesktopContent(user)

    }
}

@Composable
fun AttendanceByStudentProfileDesktopContent(
    user: UserDetailEntity,
) {
    Column {
        AttendanceProfileHeaderContent(user)
        Spacer(36.dp)
        ProfileInfoSection(userDetail = user)
    }
}

@Composable
fun AttendanceByStudentProfileDesktopLoading() {
    Column {
        AttendanceByStudentProfileHeaderLoading()
        Spacer(36.dp)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

@Composable
private fun ProfileInfoSection(
    userDetail: UserDetailEntity,
) {
    val genderIcon = when (userDetail.gender) {
        Gender.MALE -> Res.drawable.ic_male_line_24
        Gender.FEMALE -> Res.drawable.ic_female_line_24
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileInfoField(
            title = "Full Name",
            icon = Res.drawable.ic_user_line_24,
            value = userDetail.fullName
        )
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "ID",
                icon = Res.drawable.ic_id_card_line_24,
                value = userDetail.id
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
                title = "Role",
                icon = Res.drawable.ic_user_board_line_24,
                value = userDetail.role.name
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Email",
                icon = Res.drawable.ic_mail_user_line_24,
                value = userDetail.email
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Birthday",
                icon = Res.drawable.ic_calendar_fill_24,
                value = userDetail.birthDay
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
                title = "Phone",
                icon = Res.drawable.ic_phone_line_24,
                value = userDetail.phoneNumber
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            ProfileInfoField(
                title = "Address",
                icon = Res.drawable.ic_home_line_24,
                value = userDetail.address
            )
        }
    }
}

@Composable
fun AttendanceProfileHeaderContent(
    user: UserDetailEntity,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(color = theme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.fullName.take(1),
                color = theme.textBtnPrimary,
                style = Style.header
            )
        }
        Spacer(itemGap8)
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            Text(
                text = user.fullName,
                color = theme.bodyText,
                style = Style.popupTitle
            )
            Text(
                text = user.classX?.name ?: "-",
                color = theme.bodyText,
                style = Style.body
            )
            Text(
                text = user.studentId.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        }
    }
}

@Composable
private fun AttendanceByStudentProfileHeaderLoading() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
        )
        Spacer(itemGap8)
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ShimmerEffect(160.dp)
            ShimmerEffect(60.dp)
            ShimmerEffect(60.dp)
        }
    }
}