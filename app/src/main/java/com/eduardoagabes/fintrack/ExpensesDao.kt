package com.eduardoagabes.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpensesDao {

    @Query("Select * From expensesentity")
    fun getAll(): List<ExpensesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(expensesEntities: List<ExpensesEntity>)
}