package com.neskvik.pomotask.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val cid: Int,
    @ColumnInfo var name: String,
    @ColumnInfo var color: String
)