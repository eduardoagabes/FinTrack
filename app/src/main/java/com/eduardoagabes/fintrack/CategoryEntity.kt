package com.eduardoagabes.fintrack

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("key")
    val name: String,
    @ColumnInfo("is_selected")
    val isSelected: Boolean,
    @ColumnInfo("icon")
    @DrawableRes val icon: Int
)
