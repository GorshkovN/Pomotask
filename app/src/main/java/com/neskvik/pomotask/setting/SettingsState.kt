package com.neskvik.pomotask.setting

import androidx.compose.runtime.annotation.FrequentlyChangingValue

data class SettingsState (
    val workSession: String = "",
    val shortBreak: String = "",
    val longBreak: String = "",
    var isChangingValue: Boolean = false
)
