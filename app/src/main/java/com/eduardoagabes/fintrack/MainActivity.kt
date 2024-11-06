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
        val adapterCategory = CategoryListAdapter()
        rvListCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvListExpenses = findViewById<RecyclerView>(R.id.rv_expense)
        val adapterExpenses = ExpensesListAdapter()
        rvListExpenses.adapter = adapterExpenses
        rvListExpenses.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterExpenses.submitList(expenses)

        adapterCategory.setOnClickListener { selected ->
            val categoryTemp = categories.map { item ->
                when {
                    item.icon == selected.icon && !item.isSelected -> item.copy(isSelected = true)
                    item.icon == selected.icon && item.isSelected -> item.copy(isSelected = false)
                    else -> item

                }
            }

            val expensesTemp =
                expenses.filter { it.icon == selected.icon }
            adapterExpenses.submitList(expensesTemp)
            adapterCategory.submitList(categoryTemp)
        }

        rvListCategories.adapter = adapterCategory
        adapterCategory.submitList(categories)

        rvListExpenses.adapter = adapterExpenses
        adapterExpenses.submitList(expenses)
    }

}


val categories = listOf(
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_key
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_car
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_gas
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_food
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_clothes
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_eletricity
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_graphic
    ),
    CategoryUiData(
        isSelected = false,
        R.drawable.ic_credit_card
    ),
    CategoryUiData(
        isSelected = true,
        R.drawable.ic_game
    ),
    CategoryUiData(
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

