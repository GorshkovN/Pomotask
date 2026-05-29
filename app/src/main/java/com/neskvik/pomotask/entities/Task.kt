package com.neskvik.pomotask.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.neskvik.pomotask.entities.Category

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val tid: Int = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var description: String?,
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @ColumnInfo var deadline: Long?,
    @ColumnInfo(name = "use_pomodoro") var usePomodoro: Boolean,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean

)