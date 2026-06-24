package com.neskvik.pomotask.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.neskvik.pomotask.entities.Settings
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class DataStoreManager(val context: Context) {
    suspend fun saveSettings(settings: Settings){
        context.dataStore.edit{ preferences ->
            preferences[intPreferencesKey("work_session")] = settings.workSession
            preferences[intPreferencesKey("short_break")] = settings.shortBreak
            preferences[intPreferencesKey("long_break")] = settings.longBreak
//            preferences[booleanPreferencesKey("task_reminder")] = settings.taskReminder
//            preferences[booleanPreferencesKey("timer_sound")] = settings.timerSound
        }
    }
    suspend fun saveWorkSession(workSession: Int){
        context.dataStore.edit{ preferences ->
            preferences[intPreferencesKey("work_session")] = workSession
        }
    }
    suspend fun saveShortBreak(shortBreak: Int){
        context.dataStore.edit{ preferences ->
            preferences[intPreferencesKey("short_break")] = shortBreak
        }
    }
    suspend fun saveLongBreak(longBreak: Int){
        context.dataStore.edit{ preferences ->
            preferences[intPreferencesKey("long_break")] = longBreak
        }
    }
//    suspend fun saveTaskReminder(taskReminder: Boolean){
//        context.dataStore.edit{ preferences ->
//            preferences[booleanPreferencesKey("task_reminder")] = taskReminder
//        }
//    }
//    suspend fun saveTimerSound(timerSound: Boolean){
//        context.dataStore.edit{ preferences ->
//            preferences[booleanPreferencesKey("timer_sound")] = timerSound
//        }
//    }


    suspend fun saveAuthData(token: String, username: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("auth_token")] = token
            prefs[stringPreferencesKey("auth_username")] = username
            prefs[stringPreferencesKey("auth_email")] = email
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey("auth_token"))
            prefs.remove(stringPreferencesKey("auth_username"))
            prefs.remove(stringPreferencesKey("auth_email"))
        }
    }

    fun getAuthToken() = context.dataStore.data.map { it[stringPreferencesKey("auth_token")] }
    fun getAuthUsername() = context.dataStore.data.map { it[stringPreferencesKey("auth_username")] }
    fun getAuthEmail() = context.dataStore.data.map { it[stringPreferencesKey("auth_email")] }


    fun getSettings()  = context.dataStore.data.map { preferences ->
        return@map Settings(
            preferences[intPreferencesKey("work_session")] ?: 25,
            preferences[intPreferencesKey("short_break")] ?: 5,
            preferences[intPreferencesKey("long_break")] ?: 15,
//            preferences[booleanPreferencesKey("task_reminder")] ?: false,
//            preferences[booleanPreferencesKey("timer_sound")] ?: false

        )
    }
}