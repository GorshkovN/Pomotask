package com.neskvik.pomotask

import android.app.Application

class App: Application() {
    val db by lazy {
        TaskDatabase.getInstance(this)
    }
}