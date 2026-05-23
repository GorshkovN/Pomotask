package com.neskvik.pomotask.category

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey (autoGenerate = true)
    val cid: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var color: Color
)
