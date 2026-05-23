package com.neskvik.pomotask.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.neskvik.pomotask.category.Category
import java.time.LocalDate

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val tid: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var description: String?,
    @ColumnInfo var category: Category,
    @ColumnInfo var deadline: LocalDate,
    @ColumnInfo(name = "use_pomodoro") var usePomodoro: Boolean,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean

)