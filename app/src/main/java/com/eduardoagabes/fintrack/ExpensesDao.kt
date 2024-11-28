package com.eduardoagabes.fintrack

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    fun delete(expenseEntity: ExpensesEntity)

    @Query("Select * From expensesentity where category is :categoryName")
    fun getAllByCategoryName(categoryName: Int): List<ExpensesEntity>

    @Delete
    fun deleteAll(expenseEntity: List<ExpensesEntity>)
}