package com.eduardoagabes.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpensesDao {

    @Query("Select * From expensesentity")
    fun getAll(): List<ExpensesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(expensesEntities: List<ExpensesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expenseEntity: ExpensesEntity)

    @Update
    fun update(expenseEntity: ExpensesEntity)
}