package com.neskvik.pomotask.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    suspend fun getAllItems(): Flow<List<Category>>

    @Query("SELECT * FROM category")
    suspend fun getAllItemsSortedByCategory(): Flow<List<Category>>

    @Insert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

}