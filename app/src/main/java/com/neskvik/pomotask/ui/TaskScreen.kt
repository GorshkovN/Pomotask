package com.neskvik.pomotask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.neskvik.pomotask.task.SortType
import com.neskvik.pomotask.task.TaskEvent
import com.neskvik.pomotask.task.TaskState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TaskScreen(
    taskState: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(TaskEvent.ShowDialog)
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add contact")
            }
        },
        modifier = modifier.padding(16.dp)
    ) { paddingValues ->
        if (taskState.isAddingTask){
            AddTaskdialog(state = taskState, onEvent = onEvent)
        }


        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SortType.entries.forEach {
                            sortType ->
                        Row(
                            modifier = Modifier
                                .clickable{
                                    onEvent(TaskEvent.SortTasks(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = taskState.sortType == sortType,
                                onClick = {
                                    onEvent(TaskEvent.SortTasks(sortType))
                                }
                            )
                            Text(
                                text = sortType.name
                            )
                        }
                    }
                }
            }
            items(taskState.tasks){
                task ->
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = task.task.name,
                            fontSize = 20.sp
                        )
                        if (task.task.description != ""){
                            Text(
                                text = task.task.description ?: "нету"
                            )
                        }
                        if (task.category != null){
                        Text(
                            modifier = Modifier.background(Color(task.category.color.toColorInt())),
                            text = task.category?.name ?: "нету"
                        )}
                        if (task.task.deadline != null){
                            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

                            Text(
                                text = formatter.format(Date(task.task.deadline!!))
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    onEvent(TaskEvent.DeleteTask(task.task))
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }

    }
}