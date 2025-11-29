package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.paging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun StudentCenterPagingItem(
    item: UserEntity,
    onAction: (UserEntity) -> Unit,
) {
    Card(
        showAction = true,
        onClickAction = {onAction(item)}
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(color = theme.primary, shape = CircleShape)
                    .size(40.dp),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = item.fullName.take(1),
                    color = theme.textBtnPrimary,
                    style = Style.title
                )
            }
            Spacer(itemGap8)
            Column {
                Text(
                    text = item.fullName,
                    style = Style.title,
                    color = theme.bodyText
                )
                Text(
                    text = item.studentId.ifBlank {  "-"},
                    style = Style.body,
                    color = theme.bodyText
                )
            }
        }
    }
}


@Composable
fun StudentCenterPagingItemLoading() {
    Card {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ShimmerEffect(
                modifier = Modifier
                    .background(color = theme.primary, shape = CircleShape)
                    .size(40.dp),

            )
            Spacer(itemGap8)
            Column {
                ShimmerEffect(150.dp)
                Spacer(itemGap4)
                ShimmerEffect(150.dp)

            }
        }
    }
}