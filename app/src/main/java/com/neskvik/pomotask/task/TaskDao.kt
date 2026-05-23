package com.neskvik.pomotask.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.neskvik.pomotask.category.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY name")
    suspend fun getAllTasksSortedByName(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY category")
    suspend fun getAllTasksSortedByCategory(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY deadline")
    suspend fun getAllTasksSortedByDeadline(): Flow<List<Task>>



}