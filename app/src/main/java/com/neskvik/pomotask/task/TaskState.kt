package com.neskvik.pomotask.task

import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.relations.TaskWithCategory

data class TaskState(
    val tasks: List<TaskWithCategory> = emptyList(),
    val name: String = "",
    val description: String = "",
    val categories: List<Category> = emptyList(),
    val category: Category? = null,
    val deadline: String = "",
    val usePomodoro: Boolean = false,
    val isCompleted: Boolean = false,
    val sortType: SortType = SortType.Name,
    val taskFilter: TaskFilter = TaskFilter.ALL,
    val searchQuery: String = "",
    val isAddingTask: Boolean = false,
    val isSettingDate: Boolean = false
)
