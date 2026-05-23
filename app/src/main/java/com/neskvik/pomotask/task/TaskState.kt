package com.neskvik.pomotask.task

import android.graphics.Color
import com.neskvik.pomotask.category.Category

data class TaskState(
    val tasks : List<Task> = emptyList(),
    val name : String = "",
    val description : String = "",
    val category : Category = Category(cid = 1, name = "Test", color = Color()),
    val sortType: SortType = SortType.Name
)
