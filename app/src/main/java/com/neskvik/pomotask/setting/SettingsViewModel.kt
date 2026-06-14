package com.neskvik.pomotask.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neskvik.pomotask.entities.Settings
import com.neskvik.pomotask.task.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel (private val manager: DataStoreManager): ViewModel(){
    val _state = MutableStateFlow(SettingsState())

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            manager.getSettings().first().let{settings ->
                _state.update {
                    it.copy(
                        workSession = settings.workSession.toString(),
                        shortBreak = settings.shortBreak.toString(),
                        longBreak = settings.longBreak.toString()
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent){
        when(event){
            is SettingsEvent.SaveWorkSession ->{

                val value = (_state.value.workSession.toIntOrNull() ?: 25).coerceIn(1,60)
                _state.update { it.copy(workSession = value.toString()) }
                viewModelScope.launch {
                    manager.saveWorkSession(value)
                }
            }
            is SettingsEvent.SaveShortBreak ->{
                val value = (_state.value.shortBreak.toIntOrNull() ?: 5).coerceIn(1,60)
                _state.update { it.copy(shortBreak = value.toString()) }
                viewModelScope.launch {
                    manager.saveShortBreak(value)
                }
            }

            is SettingsEvent.SaveLongBreak -> {
                val value = (_state.value.longBreak.toIntOrNull() ?: 15).coerceIn(1,60)
                _state.update { it.copy(longBreak = value.toString()) }
                viewModelScope.launch {
                    manager.saveLongBreak(value)
                }
            }
            is SettingsEvent.SetWorkSession -> {
                _state.update { it.copy(
                    workSession = event.workSession
                ) }
            }

            is SettingsEvent.SetShortBreak -> {
                _state.update { it.copy(
                    shortBreak = event.shortBreak
                ) }
            }

            is SettingsEvent.SetLongBreak -> {
                _state.update { it.copy(
                    longBreak = event.longBreak
                ) }
            }



            SettingsEvent.HideDialog -> {
                state.value.isChangingValue = false
            }
            SettingsEvent.ShowDialog -> {
                state.value.isChangingValue = true
            }

        }
    }

}