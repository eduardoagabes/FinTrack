package com.eduardoagabes.fintrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpensesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val value: String,
    var category: Int,
    var color: Int
)
