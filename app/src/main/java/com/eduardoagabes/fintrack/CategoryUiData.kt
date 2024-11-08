package com.eduardoagabes.fintrack


data class CategoryUiData(
    val id: Long,
    val category: Int,
    val isSelected: Boolean,
    val color: Int = R.color.white
)
