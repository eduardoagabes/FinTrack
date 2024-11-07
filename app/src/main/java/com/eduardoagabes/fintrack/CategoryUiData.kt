package com.eduardoagabes.fintrack

import androidx.annotation.DrawableRes

data class CategoryUiData(
    val name: String,
    val isSelected: Boolean,
    @DrawableRes val icon: Int
)
