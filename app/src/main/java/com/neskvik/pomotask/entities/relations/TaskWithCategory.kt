package com.neskvik.pomotask.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.neskvik.pomotask.entities.Category
import com.neskvik.pomotask.entities.Task

data class TaskWithCategory(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "cid"
    )
    val category: Category?
)
