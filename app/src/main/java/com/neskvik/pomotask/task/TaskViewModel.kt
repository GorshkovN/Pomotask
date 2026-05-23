package com.neskvik.pomotask.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(
    private val dao: TaskDao
): ViewModel()  {

    private val _sortType = MutableStateFlow(SortType.Name)
    private val _tasks = _sortType
        .flatMapLatest {  sortType ->
            when(sortType){
                SortType.Name -> dao.getAllTasksSortedByName()
                SortType.Category -> dao.getAllTasksSortedByCategory()
                SortType.Deadline -> dao.getAllTasksSortedByDeadline()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TaskState())

    val state = combine(_state,_sortType,_tasks){ state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType,

        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())


    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                }
            }
            TaskEvent.HideDialog -> TODO()
            TaskEvent.SaveTask -> TODO()
            is TaskEvent.SetCategory -> TODO()
            is TaskEvent.SetDeadline -> TODO()
            is TaskEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.description
                ) }
            }
            is TaskEvent.SetIsCompleted -> TODO()
            is TaskEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is TaskEvent.SetUsePomodoro -> TODO()
            TaskEvent.ShowDialog -> TODO()
            is TaskEvent.SortTasks -> {
                _sortType.value = event.sortType
            }
        }
    }

}