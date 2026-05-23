package com.neskvik.pomotask.category

sealed interface CategoryEvent {
    object SaveCategory: CategoryEvent
    data class  SetName
}