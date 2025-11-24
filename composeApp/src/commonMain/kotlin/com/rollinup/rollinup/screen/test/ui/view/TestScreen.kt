@file:OptIn(ExperimentalMaterial3Api::class)

package com.rollinup.rollinup.screen.test.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.camera.CameraView
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DatePickerField
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.filepicker.FilePicker
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.permitform.view.PermitForm
import com.rollinup.rollinup.component.pullrefresh.PullRefresh
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.selector.SelectorTest
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.generalSetting
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.time.SnapListTest
import com.rollinup.rollinup.component.time.TimeDurationTextFieldTest
import com.rollinup.rollinup.component.time.TimePickerBottomSheet
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.toAnnotatedString
import com.rollinup.rollinup.navigation.NavigationRoute
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.dashboard.ui.component.DashboardCalendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.YearMonth
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_camera_line_24
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import kotlin.time.ExperimentalTime

@Composable
fun TestScreen(
    onShowSnackBar: (String, Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    navController: NavController,
    onUpdateLoginData: (LoginEntity?) -> Unit,
) {
    var searchValue by remember { mutableStateOf("") }
    var isShowCamera by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPagingDialog by remember { mutableStateOf(false) }
    var showPermitForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TestTopAppBar(
                onSearch = { value ->
                    searchValue = value
                },
                onNavigateUp = onNavigateUp,
                onLogout = {
                    onUpdateLoginData(null)
                    navController.navigate(AuthNavigationRoute.Login.route) {
                        popUpTo(NavigationRoute.TestRoute.route) {
                            inclusive = true
                        }
                    }
                },
                onOpenCamera = { isShowCamera = true }
            )
        }
    ) {
        PagingDummyDialog(
            isShowDialog = showPagingDialog,
            onDismissRequest = {
                showPagingDialog = false
            }
        )



        CameraView(
            onDismissRequest = { isShowCamera = it },
            onCapture = {},
            onError = {},
            notification = {
                Chip(
                    text = "Make sure your face can be identified easily",
                    severity = Severity.SECONDARY,
                    textStyle = Style.body
                )
            },
            isShowCamera = isShowCamera,
            successMsg = "Photo captured",
            errorMsg = "Failed to take photo",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showShimmer by remember { mutableStateOf(false) }
            val user = localUser
            val scope = rememberCoroutineScope()
            var selected by remember { mutableStateOf(listOf<Int>()) }
            var isRefreshing by remember { mutableStateOf(false) }
            var showTimePicker by remember { mutableStateOf(false) }
            var selectedDate by remember { mutableStateOf(emptyList<Long>()) }

            LaunchedEffect(showShimmer) {
                delay(1000)
                if (showShimmer) showShimmer = false
            }
            FilePicker(
                value = null,
                fileName = null,
                showCameraOption = true ,
                onValueChange = {}
            )
//            CalendarContent()
//            Example4Page()
            DashboardCalendar(
                attendanceList = emptyList(),
                onClickDay = {
                    L.w { "$it" }
                },
                isLoading = false,
                title = "Calendar"
            )

            Button(
                text = "To Paging Demo",
                onClick = {
                    showPagingDialog = true
                }
            )

            TimeDurationTextFieldTest()
//            FilePicker()
            Button(text = "Show Permit Form") {
                showPermitForm = true
            }

            Spacer(itemGap8)
            ShimmerEffect(120.dp)
            Spacer(itemGap8)
            Text(
                text = user.toString(),
                style = Style.header,
                color = theme.bodyText
            )
            Spacer(itemGap8)
            Text(
                text = generalSetting.toString(),
                style = Style.body,
                color = theme.bodyText,
                modifier = Modifier.padding(screenPaddingValues)
            )
            if (isRefreshing) {
                Text(
                    text = "Refreshing",
                    style = Style.body,
                    color = theme.bodyText,
                    modifier = Modifier.padding(screenPaddingValues)
                )
            }
            Button(
                text = "Show Date Picker"
            ) {
                showDatePicker = true
            }
//            DatePickerBottomSheet(
//                isShowSheet = showDatePicker,
//                onDismissRequest = {
//                    showDatePicker = false
//                },
//                value = selectedDate,
//                maxSelection = 3,
//                onValueChange = { selectedDate = it }
//            )
//            DatePicker(
//                value = emptyList(),
//                isShowPicker = showDatePicker,
//                onDismissRequest = { showDatePicker = it },
//                onValueChange = {}
//            )
            SelectorTest()

            Column(modifier = Modifier.padding(screenPaddingValues)) {
                DatePickerField(
                    title = "Absent Duration",
                    placeholder = "Select duration",
//                errorText = ,
                    value = selectedDate,
                    maxSelection = 3,
//                isError = ,
//                enabled = false ,
                    platform = getPlatform(),
                    isDisablePastSelection = true
                ) { value ->
                    selectedDate = value
                }
            }
//            TimePicker()
            SnapListTest()
            Button(
                text = "Show Time Picker"
            ) {
                showTimePicker = true
            }
            TimePickerBottomSheet(
                showSheet = showTimePicker,
                onDismissRequest = { showTimePicker = it },
                onValueChange = {}
            )
            PermitForm(
                id = "nigga",
                showPermitForm = showPermitForm,
                onDismissRequest = {
                    showPermitForm = it
                },
            )
            PullRefresh(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(1000)
                        isRefreshing = false
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier.height(getScreenHeight() * 0.8f),
                    contentPadding = screenPaddingValues,
                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                ) {
                    repeat(10) {
                        item {
                            Card(
                                onClick = {
                                    if (selected.isNotEmpty()) {
                                        if (1 in selected) {
                                            selected = selected.filter { it != 1 }
                                        } else {
                                            selected = selected.plus(1)
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (selected.isEmpty()) {
                                        selected = selected.plus(1)
                                    }
                                },
                                backgroundColor = if (1 in selected) theme.popUpBgSelected else theme.popUpBg
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                                ) {
                                    Text(
                                        text = "Title",
                                        style = Style.title,
                                        color = theme.bodyText
                                    )
                                    Text(
                                        text = "Body",
                                        style = Style.body,
                                        color = theme.bodyText
                                    )
                                }
                            }
                        }
                        item {
                            Card(
                                onClick = {
                                    if (selected.isNotEmpty()) {
                                        if (2 in selected) {
                                            selected = selected.filter { it != 2 }
                                        } else {
                                            selected = selected.plus(2)
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (selected.isEmpty()) {
                                        selected = selected.plus(2)
                                    }
                                },
                                backgroundColor = if (2 in selected) theme.popUpBgSelected else theme.popUpBg,
                                showAction = selected.isEmpty(),
                                onClickAction = {}
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                                ) {
                                    Text(
                                        text = "Title",
                                        style = Style.title,
                                        color = theme.bodyText
                                    )
                                    Text(
                                        text = "Body",
                                        style = Style.body,
                                        color = theme.bodyText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TestTopAppBar(
    onSearch: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
    onOpenCamera: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }

    val listMenu = listOf(
        Menu.SEARCH,
        Menu.FILTER,
        Menu.PRINT,
    )

    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showBottomSheet = true
                }

                Menu.PRINT -> {
                    showBottomSheet2 = true
                }

                else -> {}
            }
        },
        menu = listMenu,
        onSearch = onSearch,
        onNavigateUp = onNavigateUp,
        title = "Test Screen",
    )

    BottomSheetWithButton(
        onClickConfirm = {},
        isShowSheet = showBottomSheet2,
        onDismissRequest = { showBottomSheet2 = it },
        onOpenCamera = onOpenCamera
    )
}

@Composable
fun BottomSheetWithButton(
    onClickConfirm: () -> Unit,
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onOpenCamera: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var showAnnotatedDialog by remember { mutableStateOf(false) }


    BottomSheet(
        onClickConfirm = {
            onClickConfirm()
        },
        onDismissRequest = onDismissRequest,
        isShowSheet = isShowSheet,
    ) {
        ActionButton(
            label = "Show Text Dialog",
            onClick = {
                showDialog = true
                onDismissRequest(false)
            }
        )
        ActionButton(
            label = "Show Annotated Text",
            onClick = {
                showAnnotatedDialog = true
                onDismissRequest(false)
            },
            icon = Res.drawable.ic_check_line_24,
            iconTint = theme.danger
        )
        ActionButton(
            label = "Open Camera",
            onClick = {
                onOpenCamera()
                onDismissRequest(false)
            },
            icon = Res.drawable.ic_camera_line_24,
            iconTint = theme.primary
        )
    }

    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = { showDialog = it },
        content = "My name is Yoshikage Kira. I'm 33 years old. My house is in the northeast section of Morioh, where all the villas are, and I am not married.",
        onClickConfirm = {},
        title = "Hello, This is Kira",
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.primary,
        onClickCancel = {}
    )

    AlertDialog(
        isShowDialog = showAnnotatedDialog,
        onDismissRequest = { showAnnotatedDialog = it },
        content = "**123456** **789** 012**34 3**567890 **1234567890**".toAnnotatedString(),
        isSingleButton = true,
        showCancelButton = true,
        onClickConfirm = {},
        onClickCancel = {}
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun CalendarContent() {
    val currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val startMonth = remember { currentMonth }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val selections = remember { mutableStateListOf<CalendarDay>() }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = currentMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )

    HorizontalCalendar(
        state = state,
        calendarScrollPaged = false,
        userScrollEnabled = true,
        reverseLayout = false,
        modifier = Modifier
//            .width(500.dp),
//        contentPadding = ,
//        contentHeightMode = ,
        ,
        dayContent = {
            Box() {
                Text(
                    text = it.date.day.toString(),
                    style = Style.title,
                    color = theme.bodyText
                )
            }
        },
        monthHeader = {
//            MonthHeader(it)
            MonthHeader(it)
        },
        monthBody = { _, content ->
            Box(
                Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            theme.primary,
                            theme.danger
                        ),
                    ),
                )
            ) {
                content()
            }
//            Column(modifier = Modifier
////                .fillMaxWidth()
//                .background(theme.primary)
//            ) {
//                content()
//            }
        },
        monthFooter = {},
        monthContainer = { _, content ->
//            LaunchedEffect(month){
//                L.w {
//                    "month changes from container: $month"
//                }
//            }
            Box(
                modifier = Modifier.width(500.dp)
            ) {
                content()
            }

//            Column(
//                modifier = Modifier
////                    .fillMaxWidth()
//                    .background(theme.warning)
//            ) {
//            }

        }
    )
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = theme.secondary50)
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = calendarMonth.yearMonth.month.name,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek.name.first().toString(),
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        HorizontalDivider(color = Color.Black)
    }
}

@Composable
private fun Day(day: CalendarDay) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (day.position == DayPosition.MonthDate) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = day.date.dayOfMonth.toString(),
                color = Color.Black,
                fontSize = 14.sp,
            )
        }
    }
}

