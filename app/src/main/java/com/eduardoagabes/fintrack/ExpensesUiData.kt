package com.eduardoagabes.fintrack

import androidx.annotation.DrawableRes

data class ExpensesUiData(
    val isSelected: Boolean,
    @DrawableRes val icon: Int,
    val name: String,
    val value: String,
    val color: Int
)
