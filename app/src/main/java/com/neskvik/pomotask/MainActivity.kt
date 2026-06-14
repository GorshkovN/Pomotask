package com.neskvik.pomotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.neskvik.pomotask.setting.SettingsViewModel
import com.neskvik.pomotask.ui.TaskScreen
import com.neskvik.pomotask.task.TaskViewModel
import com.neskvik.pomotask.ui.MainScreen
import com.neskvik.pomotask.ui.theme.PomotaskTheme
import kotlin.getValue
import kotlin.setValue

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<TaskViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel((application as App).db.dao) as T
                }
            }
        }
    )

    private val settingsViewModel by viewModels<SettingsViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel((application as App).dataStoreManager) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val categories = listOf(
//            Category(1, "Task", "#fc0303"),
//            Category(2, "Work", "#5c84fa"),
//            Category(3, "Study", "#aff536"),
//        )
//
//        lifecycleScope.launch { categories.forEach { db.dao.insertCategory(it) } }

        enableEdgeToEdge()
        setContent {
            PomotaskTheme {
                val taskState by viewModel.state.collectAsState()
                val settingsState by settingsViewModel.state.collectAsState()
                MainScreen(taskState = taskState, settingsState = settingsState, onEvent = viewModel::onEvent, onSettingsEvent = settingsViewModel::onEvent)
            }
        }
    }
}