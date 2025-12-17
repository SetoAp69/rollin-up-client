package com.rollinup.rollinup.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform

@Composable
fun TabRow(
    tabList: List<String>,
    currentIndex: Int,
    onTabChange: (Int) -> Unit,
) {
    val modifier =
        if (getPlatform().isMobile()) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.width(200.dp)
        }
    TabRow(
        selectedTabIndex = currentIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
//        indicator =,
//        divider =,
        tabs = {
            tabList.forEachIndexed { index, title ->
                Tab(
                    isSelected = index == currentIndex,
                    label = title,
                    onClick = {
                        onTabChange(index)
                    }
                )
            }
        }
    )
}

@Composable
fun TabRow(
    tabList: List<String>,
    currentTab: Int,
    onTabChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = theme.secondary,
                shape = RoundedCornerShape(50)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabList.forEachIndexed { index, title ->
            val isSelected = index == currentTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(
                        color = if (isSelected) theme.primary else Color.Transparent
                    )
                    .clickable { onTabChange(index) }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (isSelected) {
                        Color.White
                    } else {
                        theme.textPrimary
                    },
                    style = Style.title
                )
            }
        }
    }
}


@Composable
fun TabRow(
    currentIndex: Int,
    tabList: List<String>,
    padding: PaddingValues = PaddingValues(0.dp),
    onTabChange: (Int) -> Unit,
) {
    val modifier =
        if (getPlatform().isMobile()) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
        }

    Row(
        modifier = modifier
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabList.fastForEachIndexed { index, title ->
            Tab(
                isSelected = index == currentIndex,
                label = title,
                onClick = {
                    onTabChange(index)
                }
            )
            Spacer(itemGap4)
        }
    }
}

@Composable
private fun Tab(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    val color = TabListColor.getColor(isSelected)
    CustomRipple(theme.primary) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable{
                    onClick()
                }
                .background(color = color.containerColor)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ){
            Text(
                text = label,
                color = color.contentColor,
                style = Style.body.copy(fontWeight = FontWeight.W500),
            )
        }
    }
}


data class TabListColor(
    val contentColor: Color,
    val containerColor: Color,
) {
    companion object {
        @Composable
        fun getColor(isSelected: Boolean): TabListColor {
            return TabListColor(
                contentColor = if (isSelected) theme.textBtnPrimary else theme.primary,
                containerColor = if (isSelected) theme.primary else theme.secondary
            )
        }
    }
}