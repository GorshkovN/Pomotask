package com.neskvik.pomotask.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neskvik.pomotask.setting.DataStoreManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _state = MutableStateFlow(PomodoroState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null

    private var workMs = 25 * 60 * 1000L
    private var shortBreakMs = 5 * 60 * 1000L
    private var longBreakMs = 15 * 60 * 1000L

    init {
        viewModelScope.launch {
            dataStoreManager.getSettings().collect { settings ->
                workMs = settings.workSession * 60 * 1000L
                shortBreakMs = settings.shortBreak * 60 * 1000L
                longBreakMs = settings.longBreak * 60 * 1000L
                if (!_state.value.isRunning && _state.value.timerMode == TimerMode.WORK) {
                    _state.update { it.copy(currentTime = workMs, sessionTotalTime = workMs) }
                }
            }
        }
    }

    fun togglePlayPause() {
        if (_state.value.isRunning) pause() else play()
    }

    private fun play() {
        timerJob?.cancel()
        _state.update { it.copy(isRunning = true) }
        timerJob = viewModelScope.launch {
            while (true) {
                while (_state.value.currentTime > 0L) {
                    delay(1000L)
                    _state.update { it.copy(currentTime = (it.currentTime - 1000L).coerceAtLeast(0L)) }
                }
                when (_state.value.timerMode) {
                    TimerMode.WORK -> {
                        val newCompleted = _state.value.completedSessions + 1
                        if (newCompleted % 4 == 0) {
                            _state.update { it.copy(
                                completedSessions = newCompleted,
                                timerMode = TimerMode.LONG_BREAK,
                                sessionTotalTime = longBreakMs,
                                currentTime = longBreakMs
                            )}
                        } else {
                            _state.update { it.copy(
                                completedSessions = newCompleted,
                                timerMode = TimerMode.SHORT_BREAK,
                                sessionTotalTime = shortBreakMs,
                                currentTime = shortBreakMs
                            )}
                        }
                    }
                    TimerMode.SHORT_BREAK, TimerMode.LONG_BREAK -> {
                        _state.update { it.copy(
                            timerMode = TimerMode.WORK,
                            sessionTotalTime = workMs,
                            currentTime = workMs
                        )}
                    }
                }
                delay(500L)
            }
        }
    }

    private fun pause() {
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(isRunning = false) }
    }

    fun resetSession() {
        val total = _state.value.sessionTotalTime
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(isRunning = false, currentTime = total) }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(
            isRunning = false,
            timerMode = TimerMode.WORK,
            completedSessions = 0,
            sessionTotalTime = workMs,
            currentTime = workMs
        )}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
