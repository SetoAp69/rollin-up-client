package com.rollinup.rollinup.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

/**
 * Displays a segmented control style tab row.
 *
 * Features a single container with a background color, where the selected tab
 * is highlighted (e.g., White on Primary background). Useful for switching views
 * or filtering lists.
 *
 * @param tabList List of titles for the tabs.
 * @param currentTab The index of the currently selected tab.
 * @param onTabChange Callback triggered when a tab is selected.
 * @param modifier Modifier applied to the row container.
 */
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


/**
 * Displays a list of individual, pill-shaped tabs.
 *
 * Unlike the segmented style, these tabs are separate elements separated by a gap.
 * On mobile, they stretch to fill width; otherwise, they wrap content.
 *
 * @param currentIndex The index of the currently selected tab.
 * @param tabList List of titles for the tabs.
 * @param padding Padding around the tab row.
 * @param onTabChange Callback triggered when a tab is selected.
 */
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

/**
 * Individual pill-shaped tab composable.
 */
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
                .clickable {
                    onClick()
                }
                .background(color = color.containerColor)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = label,
                color = color.contentColor,
                style = Style.body.copy(fontWeight = FontWeight.W500),
            )
        }
    }
}


/**
 * Helper class to determine tab colors based on selection state.
 */
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