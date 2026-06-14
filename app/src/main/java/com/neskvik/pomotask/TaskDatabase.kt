package com.neskvik.pomotask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("INSERT INTO category (cid, name, color) VALUES (1, 'Задача', '#fc0303')")
                        db.execSQL("INSERT INTO category (cid, name, color) VALUES (2, 'Работа', '#5c84fa')")
                        db.execSQL("INSERT INTO category (cid, name, color) VALUES (3, 'Учёба', '#aff536')")
                    }
                })
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}