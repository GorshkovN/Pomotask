package com.neskvik.pomotask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task

@Database(
    entities = [Task::class, Category::class],
    version = 1,
)

abstract class TaskDatabase: RoomDatabase() {

    abstract  val dao: TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase{
            synchronized(this){
                return  INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}