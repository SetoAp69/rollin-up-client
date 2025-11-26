package com.rollinup.rollinup.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun TabList(
    tabList: List<String>,
    currentIndex: Int,
    onTabChange: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = currentIndex,
//        modifier =,
        containerColor = Color.Transparent,
//        contentColor =,
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
private fun Tab(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    val color = TabListColor.getColor(isSelected)
    Tab(
        selected = isSelected,
        onClick = onClick,
        enabled = true,
        text = {
            Text(
                text = label,
                color = color.contentColor,
                style = Style.body.copy(fontWeight = FontWeight.W500),
                modifier = Modifier
                    .background(shape = RoundedCornerShape(50), color = color.containerColor)
                    .padding(vertical = 8.dp, horizontal = 12.dp)

            )
        },
    )
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