package com.neskvik.pomotask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.neskvik.pomotask.TaskDatabase
import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.task.TaskScreen
import com.neskvik.pomotask.task.TaskViewModel
import com.neskvik.pomotask.ui.theme.PomotaskTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private  val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task.db"
        ).build()
    }




    private val viewModel by viewModels<TaskViewModel>(
        factoryProducer = {
            object  : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(db.dao) as T
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
                val state by viewModel.state.collectAsState()
                TaskScreen(state = state,onEvent = viewModel::onEvent)

            }
        }
    }
}