package com.neskvik.pomotask.pomodoro

data class PomodoroState(
    val timerMode: TimerMode = TimerMode.WORK,
    val completedSessions: Int = 0,
    val isRunning: Boolean = false,
    val currentTime: Long = 25 * 60 * 1000L,
    val sessionTotalTime: Long = 25 * 60 * 1000L
)
