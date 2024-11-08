package com.eduardoagabes.fintrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["key"], unique = true)])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("key")
    val category: Int,
    @ColumnInfo("is_selected")
    val isSelected: Boolean,
    val color: Int
)
