package com.neskvik.pomotask.task

import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task

sealed interface TaskEvent {

    object SaveTask: TaskEvent
    data class SetName(val name: String): TaskEvent
    data class SetDescription(val description: String): TaskEvent
    data class SetCategory(val category: Category): TaskEvent
    data class SetDeadline(val deadline: String): TaskEvent
    data class SetUsePomodoro(val usePomodoro: Boolean): TaskEvent
    data class SetIsCompleted(val isCompleted: Boolean): TaskEvent

    object ShowDialog: TaskEvent
    object HideDialog: TaskEvent

    object ShowDatePicker: TaskEvent
    object HideDatePicker: TaskEvent

    data class SortTasks(val sortType: SortType): TaskEvent
    data class FilterTasks(val taskFilter: TaskFilter): TaskEvent
    data class SetSearchQuery(val query: String): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
    data class ToggleTaskCompleted(val task: Task): TaskEvent

}