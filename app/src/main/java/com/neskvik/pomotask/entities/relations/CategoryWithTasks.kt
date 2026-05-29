package com.neskvik.pomotask.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "cid",
        entityColumn = "category_id"
    )
    val tasks: List<Task>
)
