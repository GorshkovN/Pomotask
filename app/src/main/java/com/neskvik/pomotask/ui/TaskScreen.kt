package com.neskvik.pomotask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.neskvik.pomotask.R
import com.neskvik.pomotask.task.SortType
import com.neskvik.pomotask.task.TaskEvent
import com.neskvik.pomotask.task.TaskFilter
import com.neskvik.pomotask.task.TaskState
import com.neskvik.pomotask.entities.relations.TaskWithCategory
import com.neskvik.pomotask.ui.theme.Blue
import com.neskvik.pomotask.ui.theme.BlockColor
import com.neskvik.pomotask.ui.theme.Bounded
import com.neskvik.pomotask.ui.theme.Montserrat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime

private val SearchBg = Color(0xFF1F252E)
private val TextDim  = Color.White.copy(alpha = 0.38f)

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    taskState: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier
) {
    var showSortSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskWithCategory?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSortSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSortSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 32.dp)) {
                Text(
                    text = "Сортировка",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontFamily = Montserrat,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                listOf(
                    SortType.Name     to "По названию",
                    SortType.Category to "По категории",
                    SortType.Deadline to "По дедлайну"
                ).forEach { (sort, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(TaskEvent.SortTasks(sort))
                                showSortSheet = false
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = taskState.sortType == sort,
                            onClick = {
                                onEvent(TaskEvent.SortTasks(sort))
                                showSortSheet = false
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(label, fontSize = 15.sp, fontFamily = Montserrat)
                    }
                }
            }
        }
    }

    selectedTask?.let { twc ->
        ModalBottomSheet(
            onDismissRequest = { selectedTask = null },
            containerColor = Color(0xFF1A1F28)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 40.dp)
            ) {
                twc.category?.let { cat ->
                    val bgColor = Color(cat.color.toColorInt())
                    val textColor = if (bgColor.luminance() > 0.5f) Color.Black else Color.White
                    Text(
                        text = cat.name,
                        fontSize = 11.sp,
                        color = textColor,
                        fontFamily = Montserrat,
                        modifier = Modifier
                            .clip(RoundedCornerShape(200.dp))
                            .background(bgColor.copy(alpha = 0.7f))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                    Spacer(Modifier.height(14.dp))
                }

                Text(
                    text = twc.task.name,
                    fontSize = 20.sp,
                    fontFamily = Bounded,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    lineHeight = 24.sp
                )

                Spacer(Modifier.height(20.dp))

                twc.task.deadline?.let { dl ->
                    val fmt = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    TaskDetailRow(label = "Дедлайн", value = fmt.format(Date(dl)))
                    Spacer(Modifier.height(14.dp))
                }

                if (!twc.task.description.isNullOrBlank()) {
                    TaskDetailRow(label = "Описание", value = twc.task.description ?: "")
                    Spacer(Modifier.height(14.dp))
                }

                if (twc.task.usePomodoro) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pomodoro_icon),
                            contentDescription = null,
                            tint = Blue,
                            modifier = Modifier.size(14.dp)
                        )
                        Text("Помодоро", fontSize = 12.sp, color = Blue, fontFamily = Montserrat)
                    }
                    Spacer(Modifier.height(14.dp))
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        onEvent(TaskEvent.ToggleTaskCompleted(twc.task))
                        selectedTask = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (twc.task.isCompleted) Color(0xFF2D3340) else Color(0xFF1A3A2A)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (twc.task.isCompleted) "Снять отметку" else "Выполнено",
                        color = if (twc.task.isCompleted) TextDim else Color(0xFF4CAF50),
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        onEvent(TaskEvent.DeleteTask(twc.task))
                        selectedTask = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D1B1B)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Удалить задачу", color = Color(0xFFFF5252), fontFamily = Montserrat)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->

        if (taskState.isAddingTask) {
            AddTaskdialog(state = taskState, onEvent = onEvent)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    text = "Мои задачи",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Bounded
                )


                IconButton(
                    onClick = { onEvent(TaskEvent.ShowDialog)},
                    modifier = Modifier.size(45.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                listOf(
                    TaskFilter.TODAY to "Сегодня",
                    TaskFilter.WEEK  to "На неделе",
                    TaskFilter.ALL   to "Все"
                ).forEach { (filter, label) ->
                    val active = taskState.taskFilter == filter
                    Text(
                        text = label,
                        color = if (active) Color.White else TextDim,
                        fontSize = 16.sp,
                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = Bounded,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onEvent(TaskEvent.FilterTasks(filter)) }
                           .padding(horizontal = 4.dp, vertical = 1.dp)

                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BasicTextField(
                    value = taskState.searchQuery,
                    onValueChange = { onEvent(TaskEvent.SetSearchQuery(it)) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp, fontFamily = Montserrat),
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(SearchBg)
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = TextDim,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box {
                                if (taskState.searchQuery.isEmpty()) {
                                    Text("Поиск", color = TextDim, fontSize = 12.sp, fontWeight = FontWeight.Medium, fontFamily = Montserrat)
                                }
                                inner()
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(SearchBg)
                        .clickable { showSortSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.sort_icon), contentDescription = "")
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(taskState.tasks) { twc ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(BlockColor)
                            .clickable { selectedTask = twc }
                            .padding(14.dp)
                    ) {
                        Text(
                            text = twc.task.name,
                            fontSize = 13.sp,
                            fontFamily = Bounded,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            maxLines = 2,
                            lineHeight = 13.sp
                        )
                        if (twc.category != null || twc.task.deadline != null || twc.task.isCompleted) {
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    twc.task.deadline?.let { dl ->
                                        val fmt = SimpleDateFormat("dd MMMM", Locale("ru"))
                                        Text(
                                            text = fmt.format(Date(dl)),
                                            fontSize = 11.sp,
                                            color = TextDim
                                        )
                                    }
                                    twc.category?.let { cat ->
                                        val backgroundColor = Color(cat.color.toColorInt())
                                        val textColor = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White
                                        Text(
                                            text = cat.name,
                                            fontSize = 11.sp,
                                            color = textColor,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(200.dp))
                                                .background(backgroundColor.copy(alpha = 0.70f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                if (twc.task.isCompleted) {
                                    Text(
                                        text = "Выполнено",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFFFFFF),
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun TaskDetailRow(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = TextDim, fontFamily = Montserrat)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 14.sp, color = Color.White, fontFamily = Montserrat, lineHeight = 20.sp)
    }
}
