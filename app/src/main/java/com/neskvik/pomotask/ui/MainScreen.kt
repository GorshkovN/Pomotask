package com.neskvik.pomotask.ui

import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.neskvik.pomotask.BottomNavigationItem
import com.neskvik.pomotask.TaskDatabase
import com.neskvik.pomotask.setting.SettingsEvent
import com.neskvik.pomotask.setting.SettingsState
import com.neskvik.pomotask.task.TaskEvent
import com.neskvik.pomotask.task.TaskState
import com.neskvik.pomotask.task.TaskViewModel
import kotlin.getValue

@Composable
fun MainScreen(modifier: Modifier = Modifier,
               taskState: TaskState,
               settingsState: SettingsState,
               onEvent: (TaskEvent) -> Unit,
               onSettingsEvent: (SettingsEvent) -> Unit){

    val items = listOf(
        BottomNavigationItem(
            title = "Tasks",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Pomodoro",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {

                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex){
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }

                    )
                }
            }
        }
    ) { innerPadding ->
      ContentScreen(modifier = Modifier.padding(innerPadding), selectedItemIndex, taskState = taskState, settingsState, onEvent, onSettingsEvent)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier,selectedIndex: Int,taskState: TaskState, settingsState: SettingsState, onEvent: (TaskEvent) -> Unit, onSettingsEvent: (SettingsEvent) -> Unit) {
    when(selectedIndex){
        0 -> {
            TaskScreen(taskState = taskState,onEvent = onEvent, modifier = modifier)
        }
        // 1 -> Timer()
        2 -> SettingsScreen(modifier, settingsState, onEvent = onSettingsEvent)
    }
}