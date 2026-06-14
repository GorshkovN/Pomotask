package com.neskvik.pomotask

import android.app.Application
import com.neskvik.pomotask.setting.DataStoreManager
import kotlin.getValue

class App: Application() {
    val db by lazy {
        TaskDatabase.getInstance(this)
    }
    val dataStoreManager by lazy{
        DataStoreManager(this)
    }
}