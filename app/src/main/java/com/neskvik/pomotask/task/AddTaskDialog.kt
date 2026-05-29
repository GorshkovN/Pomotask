package com.neskvik.pomotask.task

import android.R.attr.checked
import android.graphics.Color.parseColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun AddTaskdialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideDialog)
        },
        title = { Text(text = "Add task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(TaskEvent.SetName(it))
                    },
                    placeholder = {
                        Text(
                            text = "Name"
                        )
                    }
                )
                TextField(
                        value = state.description,
                    onValueChange = {
                        onEvent(TaskEvent.SetDescription(it))
                    },
                    placeholder = {
                        Text(
                            text = "Description"
                        )
                    }
                )
                Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
                ){
                state.categories.forEach {
                        category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected =  state.category?.cid == category.cid,
                            onClick = {
                                onEvent(TaskEvent.SetCategory(category))
                            }
                        )
                        Text(
                            text = category.name,
                            color = Color(category.color.toColorInt())
                        )
                    }
                }
            }
                TextField(
                    value = state.deadline,
                    onValueChange = {
                        onEvent(TaskEvent.SetDeadline(it))
                    },
                    placeholder = {
                        Text(
                            text = "Deadline"
                        )
                    }
                )
//                if (state.isSettingDate){   ну как нибудь потом
//                val datePickerState = rememberDatePickerState()
//
//                DatePickerDialog(
//                    onDismissRequest = {
//                        onEvent(TaskEvent.HideDatePicker)
//                    },
//                    confirmButton = {
//                        Button(onClick = {
//                            val millis = datePickerState.selectedDateMillis
//                            onEvent(TaskEvent.SetDeadline(millis ?: 0L))
//                        }) { Text("OK") }
//                    }
//                ) {
//                    DatePicker(state = datePickerState)
//                }}
                Text(
                    text = "Использовать помодоро"
                )
                Switch(
                    checked = state.usePomodoro,
                    onCheckedChange = {
                        onEvent(TaskEvent.SetUsePomodoro(it))
                    }

                )
                Text(
                    text = "Выполнен"
                )
                Switch(
                    checked = state.isCompleted,
                    onCheckedChange = {
                        onEvent(TaskEvent.SetIsCompleted(it))
                    }

                )
            }
        },
        confirmButton =  {

                Button(onClick = {
                    onEvent(TaskEvent.SaveTask)
                }){
                    Text(
                        text = "Save"
                    )
                }

        }
    )
}