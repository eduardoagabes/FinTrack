package com.eduardoagabes.fintrack

data class ExpensesUiData(
    val id: Long = 0,
    val name: String,
    val value: String,
    var icon: Int,
    var color: Int
)
