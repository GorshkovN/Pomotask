package com.neskvik.pomotask.entities

import androidx.datastore.preferences.core.intPreferencesKey

data class Settings(
    val workSession: Int,
    val shortBreak: Int,
    val longBreak: Int,
//    val taskReminder: Boolean,  // пока без этого
//    val timerSound: Boolean
)
