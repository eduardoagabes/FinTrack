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

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinTrackDataBase::class.java, "database-fin-track"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy {
        db.getCategoryDao()
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

        GlobalScope.launch(Dispatchers.IO) {
            insertDefaultCategory()
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
            val categoryTemp = categories.map { item ->
                when {
                    item.category == selected.category && !item.isSelected -> item.copy(isSelected = true)
                    item.category == selected.category && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }

            val expenseTemp =
                if (selected.category != R.drawable.ic_all) {
                    expenses.filter { it.icon == selected.category }
                } else {
                    expenses
                }
            expensesAdapter.submitList(expenseTemp)
            categoryAdapter.submitList(categoryTemp)
        }

        rvListCategories.adapter = categoryAdapter
        getCategoriesFromDataBase(categoryAdapter)

        rvListExpenses.adapter = expensesAdapter
        expensesAdapter.submitList(expenses)
    }

    private fun insertDefaultCategory() {
        val categoriesEntity = categories.map {
            CategoryEntity(
                category = it.category,
                isSelected = it.isSelected,
                color = it.color
            )
        }

        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insertAll(categoriesEntity)

        }
    }

    private fun insertDefaultExpenses() {
        val expensesEntities = expenses.map {
            ExpensesEntity(
                name = it.name,
                value = it.value,
                category = it.icon,
                color = it.color,

                )
        }
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
            }
            adapter.submitList(categoriesUiData)
        }
    }
}

val categories = listOf(
    CategoryUiData(
        id = 0,
        category = R.drawable.ic_all,
        isSelected = false,
        color = R.color.white
    ),
    CategoryUiData(
        id = 0,
        category = R.drawable.ic_key,
        isSelected = false,
        color = R.color.white
    ),
    CategoryUiData(
        id = 0,
        category = R.drawable.ic_car,
        isSelected = false,
        color = R.color.white

    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_gas,
        color = R.color.white

    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_food,
        color = R.color.white
    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_clothes,
        color = R.color.white
    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_eletricity,
        color = R.color.white

    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_graphic,
        color = R.color.white
    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_credit_card,
        color = R.color.white

    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_game,
        color = R.color.white

    ),
    CategoryUiData(
        id = 0,
        isSelected = false,
        category = R.drawable.ic_internet,
        color = R.color.white
    ),
)

val expenses = listOf(
    ExpensesUiData(
        id = 1,
        name = "Key",
        value = "108,87",
        icon = R.drawable.ic_key,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Car",
        value = "80,26",
        R.drawable.ic_car,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Gasoline",
        value = "50,19",
        R.drawable.ic_gas,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Food",
        value = "80,19",
        R.drawable.ic_food,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Clothes",
        value = "20,00",
        R.drawable.ic_clothes,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Eletricity",
        value = "150,00",
        R.drawable.ic_eletricity,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Graphic",
        value = "250,00",
        R.drawable.ic_graphic,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Credit Card",
        value = "500,00",
        R.drawable.ic_credit_card,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Game",
        value = "35,00",
        R.drawable.ic_game,
        color = R.color.white
    ),
    ExpensesUiData(
        id = 1,
        name = "Eletricity",
        value = "90,00",
        R.drawable.ic_internet,
        color = R.color.white
    ),
)