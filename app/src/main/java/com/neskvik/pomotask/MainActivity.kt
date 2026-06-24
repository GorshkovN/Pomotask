package com.neskvik.pomotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.neskvik.pomotask.auth.AuthViewModel
import com.neskvik.pomotask.entities.Task
import com.neskvik.pomotask.pomodoro.PomodoroViewModel
import com.neskvik.pomotask.setting.SettingsViewModel
import com.neskvik.pomotask.task.TaskViewModel
import com.neskvik.pomotask.ui.MainScreen
import com.neskvik.pomotask.ui.theme.PomotaskTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val taskViewModel by viewModels<TaskViewModel>(
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
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel((application as App).dataStoreManager) as T
                }
            }
        }
    )

    private val pomodoroViewModel by viewModels<PomodoroViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PomodoroViewModel((application as App).dataStoreManager) as T
                }
            }
        }
    )

    private val authViewModel by viewModels<AuthViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel((application as App).dataStoreManager) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PomotaskTheme {
                val taskState by taskViewModel.state.collectAsState()
                val settingsState by settingsViewModel.state.collectAsState()
                MainScreen(
                    taskState = taskState,
                    settingsState = settingsState,
                    pomodoroViewModel = pomodoroViewModel,
                    authViewModel = authViewModel,
                    onEvent = taskViewModel::onEvent,
                    onSettingsEvent = settingsViewModel::onEvent
                )
            }
        }
    }
}
