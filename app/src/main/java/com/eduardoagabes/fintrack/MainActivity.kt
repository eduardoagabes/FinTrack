package com.eduardoagabes.fintrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

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
            val categoryTemp = categories.map { item ->
                when {
                    item.icon == selected.icon && !item.isSelected -> item.copy(isSelected = true)
                    item.icon == selected.icon && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }

            val expenseTemp =
                if (selected.name != "ALL") {
                    expenses.filter { it.icon == selected.icon }
                } else {
                    expenses
                }
            expensesAdapter.submitList(expenseTemp)

            categoryAdapter.submitList(categoryTemp)
        }

        rvListCategories.adapter = categoryAdapter
        categoryAdapter.submitList(categories)

        rvListExpenses.adapter = expensesAdapter
        expensesAdapter.submitList(expenses)
    }

}

val categories = listOf(
    CategoryUiData(
        name = "ALL",
        isSelected = false,
        R.drawable.add_all
    ),
    CategoryUiData(
        name = "Key",
        isSelected = false,
        R.drawable.ic_key
    ),
    CategoryUiData(
        name = "Car",
        isSelected = false,
        R.drawable.ic_car
    ),
    CategoryUiData(
        name = "Gasoline",
        isSelected = false,
        R.drawable.ic_gas
    ),
    CategoryUiData(
        name = "Food",
        isSelected = false,
        R.drawable.ic_food
    ),
    CategoryUiData(
        name = "Clothes",
        isSelected = false,
        R.drawable.ic_clothes
    ),
    CategoryUiData(
        name = "Ligth",
        isSelected = false,
        R.drawable.ic_eletricity
    ),
    CategoryUiData(
        name = "Graphic",
        isSelected = false,
        R.drawable.ic_graphic
    ),
    CategoryUiData(
        name = "Credit_card",
        isSelected = false,
        R.drawable.ic_credit_card
    ),
    CategoryUiData(
        name = "Game",
        isSelected = true,
        R.drawable.ic_game
    ),
    CategoryUiData(
        name = "Internet",
        isSelected = false,
        R.drawable.ic_internet
    ),
)

val expenses = listOf(
    ExpensesUiData(
        isSelected = false,
        icon = R.drawable.ic_key,
        name = "Key",
        value = "- $ 108,87",
        color = R.color.red
    ),
    ExpensesUiData(
        isSelected = false,
        R.drawable.ic_car,
        name = "Car",
        value = "- $ 80,26",
        color = R.color.white
    ),
    ExpensesUiData(
        isSelected = false,
        R.drawable.ic_gas,
        name = "Gasoline",
        value = "- $ 50,19",
        color = R.color.white
    ),
)

