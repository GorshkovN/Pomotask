package com.neskvik.pomotask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task
import com.neskvik.pomotask.entities.relations.CategoryWithTasks
import com.neskvik.pomotask.entities.relations.TaskWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM task")
    fun getAllTasksWithCategory(): Flow<List<TaskWithCategory>>

    @Transaction
    @Query("SELECT * FROM task ORDER BY name")
    fun getAllTasksWithCategorySortedByName(): Flow<List<TaskWithCategory>>

    @Transaction
    @Query("SELECT * FROM task ORDER BY category_id")
    fun getAllTasksWithCategorySortedByCategory(): Flow<List<TaskWithCategory>>

    @Transaction
    @Query("SELECT * FROM task ORDER BY deadline")
    fun getAllTasksWithCategorySortedByDeadline(): Flow<List<TaskWithCategory>>




    //region Category
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<Category>>

    @Insert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
    //endregion
}