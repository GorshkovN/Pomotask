package com.neskvik.pomotask.setting

import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task
import com.neskvik.pomotask.task.SortType
import com.neskvik.pomotask.task.TaskEvent

sealed interface SettingsEvent {

    object  SaveWorkSession: SettingsEvent
    object  SaveShortBreak : SettingsEvent
    object  SaveLongBreak : SettingsEvent
    data class  SetWorkSession(val workSession: String) : SettingsEvent
    data class  SetShortBreak(val shortBreak: String) : SettingsEvent
    data class  SetLongBreak(val longBreak: String) : SettingsEvent

    object ShowDialog: SettingsEvent
    object HideDialog: SettingsEvent
}