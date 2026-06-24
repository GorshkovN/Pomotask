package com.neskvik.pomotask.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neskvik.pomotask.TaskDao
import com.neskvik.pomotask.entities.Task
import com.neskvik.pomotask.entities.relations.TaskWithCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.Name)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.Name -> dao.getAllTasksWithCategorySortedByName()
                SortType.Category -> dao.getAllTasksWithCategorySortedByCategory()
                SortType.Deadline -> dao.getAllTasksWithCategorySortedByDeadline()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TaskState())
    private val _categories = dao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _tasks, _categories) { state, sortType, tasks, categories ->
        state.copy(
            tasks = applyFilters(tasks, state.taskFilter, state.searchQuery),
            categories = categories,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> viewModelScope.launch { dao.deleteTask(event.task) }
            is TaskEvent.ToggleTaskCompleted -> viewModelScope.launch {
                dao.updateTask(event.task.copy(isCompleted = !event.task.isCompleted))
            }

            TaskEvent.HideDialog -> _state.update { it.copy(isAddingTask = false) }

            TaskEvent.SaveTask -> {
                val name = state.value.name
                val description = state.value.description
                val deadline = state.value.deadline
                val category = state.value.category
                if (name.isBlank()) return

                val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val deadlineMillis = try { formatter.parse(deadline)?.time } catch (_: Exception) { null }

                val task = Task(
                    name = name,
                    description = description,
                    deadline = deadlineMillis,
                    isCompleted = false,
                    categoryId = category?.cid ?: return,
                    usePomodoro = state.value.usePomodoro
                )
                viewModelScope.launch { dao.insertTask(task) }
                _state.update { it.copy(isAddingTask = false, name = "", description = "", deadline = "", usePomodoro = false) }
            }

            is TaskEvent.SetDeadline -> _state.update { it.copy(deadline = event.deadline) }
            is TaskEvent.SetDescription -> _state.update { it.copy(description = event.description) }
            is TaskEvent.SetIsCompleted -> _state.update { it.copy(isCompleted = event.isCompleted) }
            is TaskEvent.SetName -> _state.update { it.copy(name = event.name) }
            is TaskEvent.SetUsePomodoro -> _state.update { it.copy(usePomodoro = event.usePomodoro) }
            is TaskEvent.SetCategory -> _state.update { it.copy(category = event.category) }
            is TaskEvent.SetSearchQuery -> _state.update { it.copy(searchQuery = event.query) }
            is TaskEvent.FilterTasks -> _state.update { it.copy(taskFilter = event.taskFilter) }

            TaskEvent.ShowDialog -> _state.update { it.copy(isAddingTask = true) }
            TaskEvent.ShowDatePicker -> _state.update { it.copy(isSettingDate = true) }
            TaskEvent.HideDatePicker -> _state.update { it.copy(isSettingDate = false) }
            is TaskEvent.SortTasks -> _sortType.value = event.sortType
        }
    }

    private fun applyFilters(
        tasks: List<TaskWithCategory>,
        filter: TaskFilter,
        query: String
    ): List<TaskWithCategory> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val startOfToday = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val startOfTomorrow = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 6)
        val endOfWeek = cal.timeInMillis

        return tasks
            .filter { twc ->
                when (filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.TODAY -> {
                        val d = twc.task.deadline ?: return@filter false
                        d in startOfToday until startOfTomorrow
                    }
                    TaskFilter.WEEK -> {
                        val d = twc.task.deadline ?: return@filter false
                        d in startOfToday until endOfWeek
                    }
                }
            }
            .filter { twc ->
                if (query.isBlank()) true
                else twc.task.name.contains(query, ignoreCase = true) ||
                        twc.task.description?.contains(query, ignoreCase = true) == true
            }
    }
}
