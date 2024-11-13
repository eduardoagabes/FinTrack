package com.eduardoagabes.fintrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var categories = listOf<CategoryUiData>()
    private var expenses = listOf<ExpensesUiData>()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinTrackDataBase::class.java, "database-fin-track"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy {
        db.getCategoryDao()
    }

    private val expensesDao: ExpensesDao by lazy {
        db.getExpensesDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rvListCategories = findViewById<RecyclerView>(R.id.rv_category)
        val rvListExpenses = findViewById<RecyclerView>(R.id.rv_expense)

        val categoryAdapter = CategoryListAdapter()
        val expensesAdapter = ExpensesListAdapter()



        rvListCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvListExpenses.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        categoryAdapter.setOnClickListener { selected ->
            if (selected.category == R.drawable.ic_add) {

                val createCategoryBottomSheet = CreateCategoryBottomSheet()

                createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")

            } else {
                val categoryTemp = categories.map { item ->
                    when {
                        item.category == selected.category && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.category == selected.category && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                }

                val expenseTemp =
                    if (selected.category != R.drawable.ic_all) {
                        expenses.filter { it.category == selected.category }
                    } else {
                        expenses
                    }
                expensesAdapter.submitList(expenseTemp)
                categoryAdapter.submitList(categoryTemp)
            }
        }

        rvListCategories.adapter = categoryAdapter
        getCategoriesFromDataBase(categoryAdapter)

        rvListExpenses.adapter = expensesAdapter
        getExpensesFromDataBase(expensesAdapter)
    }

    private fun getCategoriesFromDataBase(adapter: CategoryListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
            val categoriesUiData = categoriesFromDb.map {
                CategoryUiData(
                    id = it.id,
                    category = it.category,
                    isSelected = it.isSelected,
                    color = it.color
                )
            }.toMutableList()

            categoriesUiData.add(
                CategoryUiData(
                    id = 0,
                    category = R.drawable.ic_add,
                    isSelected = false,
                    color = R.color.white
                )
            )

            GlobalScope.launch(Dispatchers.Main) {
                categories = categoriesUiData
                adapter.submitList(categoriesUiData)
            }
        }
    }

    private fun getExpensesFromDataBase(adapter: ExpensesListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            val expensesFromDb: List<ExpensesEntity> = expensesDao.getAll()
            val expensesUiData = expensesFromDb.map {
                ExpensesUiData(
                    id = it.id,
                    name = it.name,
                    value = it.value,
                    category = it.category,
                    color = it.color
                )
            }

            GlobalScope.launch(Dispatchers.Main) {
                expenses = expensesUiData
                adapter.submitList(expensesUiData)
            }
        }
    }
}