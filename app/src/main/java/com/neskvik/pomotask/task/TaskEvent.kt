package com.neskvik.pomotask.task

import com.neskvik.pomotask.category.Category
import java.time.LocalDate

sealed interface TaskEvent {

    object SaveTask: TaskEvent
    data class SetName(val name: String): TaskEvent
    data class SetDescription(val description: String): TaskEvent
    data class SetCategory(val category: Category): TaskEvent
    data class SetDeadline(val deadline: LocalDate): TaskEvent
    data class SetUsePomodoro(val usePomodoro: Boolean): TaskEvent
    data class SetIsCompleted(val isCompleted: Boolean): TaskEvent

    object ShowDialog: TaskEvent
    object HideDialog: TaskEvent

    data class SortTasks(val sortType: SortType): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent



}