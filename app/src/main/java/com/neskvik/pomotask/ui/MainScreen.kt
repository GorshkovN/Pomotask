package com.neskvik.pomotask.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.neskvik.pomotask.BottomNavigationItem
import com.neskvik.pomotask.R
import com.neskvik.pomotask.auth.AuthViewModel
import com.neskvik.pomotask.pomodoro.PomodoroViewModel
import com.neskvik.pomotask.setting.SettingsEvent
import com.neskvik.pomotask.setting.SettingsState
import com.neskvik.pomotask.task.TaskEvent
import com.neskvik.pomotask.task.TaskState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    taskState: TaskState,
    settingsState: SettingsState,
    pomodoroViewModel: PomodoroViewModel,
    authViewModel: AuthViewModel,
    onEvent: (TaskEvent) -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit
) {
    val authState by authViewModel.state.collectAsState()
    var showAuthScreen by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        BottomNavigationItem("Tasks", ImageVector.vectorResource(id = R.drawable.tasks_icon), ImageVector.vectorResource(id = R.drawable.tasks_icon)),
        BottomNavigationItem("Pomodoro", ImageVector.vectorResource(id = R.drawable.pomodoro_icon), ImageVector.vectorResource(id = R.drawable.pomodoro_icon)),
        BottomNavigationItem("Settings", ImageVector.vectorResource(id = R.drawable.settings_icon), ImageVector.vectorResource(id = R.drawable.settings_icon))
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = { selectedItemIndex = index },
                            icon = {
                                BadgedBox(badge = {}) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) item.selectedIcon
                                                      else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (selectedItemIndex) {
                0 -> TaskScreen(
                    taskState = taskState,
                    onEvent = onEvent,
                    modifier = Modifier.padding(innerPadding)
                )
                1 -> PomodoroTimerScreen(
                    viewModel = pomodoroViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                2 -> SettingsScreen(
                    modifier = Modifier.padding(innerPadding),
                    settingsState = settingsState,
                    authState = authState,
                    onEvent = onSettingsEvent,
                    onLoginClick = { showAuthScreen = true },
                    onLogout = authViewModel::logout
                )
            }
        }

        if (showAuthScreen) {
            AuthScreen(
                viewModel = authViewModel,
                onDismiss = { showAuthScreen = false }
            )
        }
    }
}
