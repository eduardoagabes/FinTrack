package com.eduardoagabes.fintrack

import CreateOrUpdateExpenseBottomSheet
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

    private val categoryAdapter = CategoryListAdapter()
    private val expensesAdapter by lazy {
        ExpensesListAdapter()
    }

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinTrackDataBase::class.java, "database-fin-track"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy { db.getCategoryDao() }
    private val expensesDao: ExpensesDao by lazy { db.getExpensesDao() }

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
        val btnAddExpense = findViewById<Button>(R.id.btn_add_expenses)


        rvListCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvListExpenses.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        btnAddExpense.setOnClickListener {
            showCreateUpdateExpenseBottomSheet()
        }

        expensesAdapter.setOnClickListener { expense ->
            showCreateUpdateExpenseBottomSheet(expense)
        }

        categoryAdapter.setOnClickListener { selected ->
            if (selected.category == R.drawable.ic_add) {
                val createCategoryBottomSheet = CreateCategoryBottomSheet { category, color ->
                    val categoryEntity = CategoryEntity(
                        category = category,
                        color = color,
                        isSelected = false
                    )
                    insertCategory(categoryEntity)
                }
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

                val expenseTemp = if (selected.category != R.drawable.ic_all) {
                    expenses.filter { it.category == selected.category }
                } else {
                    expenses
                }

                categoryAdapter.submitList(categoryTemp)
                expensesAdapter.submitList(expenseTemp)
            }
        }

        rvListCategories.adapter = categoryAdapter

        GlobalScope.launch(Dispatchers.IO) {
            getCategoriesFromDataBase()
        }

        rvListExpenses.adapter = expensesAdapter

        GlobalScope.launch(Dispatchers.IO) {
            getExpensesFromDataBase()
        }

    }

    private fun getCategoriesFromDataBase() {
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
                categoryAdapter.submitList(categoriesUiData)
            }
        }
    }

    private fun getExpensesFromDataBase() {
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
            expensesAdapter.submitList(expensesUiData)
            updateTotalValue()
        }
    }

    private fun insertCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insert(categoryEntity)
            getCategoriesFromDataBase()
        }
    }

    private fun insertExpense(expenseEntity: ExpensesEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expensesDao.insert(expenseEntity)
            getExpensesFromDataBase()
        }
    }

    private fun updateExpense(expenseEntity: ExpensesEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expensesDao.update(expenseEntity)
            getExpensesFromDataBase()
        }
    }

    private fun showCreateUpdateExpenseBottomSheet(expensesUiData: ExpensesUiData? = null) {
        val createExpenseBottomSheet =
            CreateOrUpdateExpenseBottomSheet(
                expense = expensesUiData,
                userCategories = categories,
                onCreateClicked = { selectedCategory, value, expense ->
                    val expenseEntity = ExpensesEntity(
                        name = expense,
                        value = String.format("%.2f", value),
                        category = selectedCategory.category,
                        color = selectedCategory.color
                    )
                    insertExpense(expenseEntity)
                },
                onUpdateClicked = { selectedCategory, value, expenseName, expense ->
                    val expenseEntityUpdate = ExpensesEntity(
                        id = expense.id,
                        name = expenseName,
                        value = String.format("%.2f", value),
                        category = selectedCategory.category,
                        color = selectedCategory.color
                    )
                    updateExpense(expenseEntityUpdate)
                }
            )

        createExpenseBottomSheet.show(
            supportFragmentManager,
            "createExpenseBottomSheet"
        )
    }

    private fun updateTotalValue() {
        val total = expenses.sumOf { it.value.toDouble() }
        val totalValue = findViewById<TextView>(R.id.tv_total_result)
        totalValue.text = "$ ${String.format("%.2f", total)}"
    }
}