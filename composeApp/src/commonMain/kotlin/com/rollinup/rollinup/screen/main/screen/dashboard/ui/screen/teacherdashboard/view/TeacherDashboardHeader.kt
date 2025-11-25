package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun TeacherDashboardHeader(
    isLoading: Boolean,
    userLoginEntity: LoginEntity,
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = theme.primary,
                spotColor = theme.primary
            )
            .background(
                color = theme.popUpBg,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = theme.lineStroke,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(screenPaddingValues)
            .fillMaxWidth()
    ) {
        if (isLoading) {
            TeacherDashboardHeaderLoading()
        } else {
            TeacherDashboardHeaderContent(
                user = userLoginEntity
            )
        }
    }
}

@Composable
fun TeacherDashboardHeaderContent(
    user: LoginEntity,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(shape = CircleShape, color = theme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.userName.take(1),
                style = Style.header,
                color = theme.textBtnPrimary
            )
        }
        Spacer(itemGap8)
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            Text(
                text = "Hello",
                style = Style.body,
                color = theme.bodyText
            )
            Text(
                text = user.fullName,
                style = Style.popupTitle,
                color = theme.bodyText
            )
            Text(
                text = user.classX ?: "-",
                style = Style.body,
                color = theme.bodyText
            )
        }
    }
}


@Composable
fun TeacherDashboardHeaderLoading() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        ShimmerEffect(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(52.dp)
        )
        Spacer(itemGap8)
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            ShimmerEffect(60.dp)
            ShimmerEffect(150.dp)
            ShimmerEffect(80.dp)
        }
    }
}