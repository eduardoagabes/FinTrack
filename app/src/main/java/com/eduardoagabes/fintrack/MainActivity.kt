package com.eduardoagabes.fintrack

import CreateOrUpdateExpenseBottomSheet
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var categories = listOf<CategoryUiData>()
    private var categoriesEntity = listOf<CategoryEntity>()
    private var expenses = listOf<ExpensesUiData>()

    private lateinit var rvListCategories: RecyclerView
    private lateinit var rvListExpenses: RecyclerView
    private lateinit var ctnEmptyView: LinearLayout
    private lateinit var ctnTotalExpenses: LinearLayout
    private lateinit var btnAddExpense: Button
    private lateinit var tvCategoriesLabel: TextView
    private lateinit var tvExpensesLabel: TextView

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

        rvListCategories = findViewById(R.id.rv_category)
        ctnEmptyView = findViewById(R.id.ll_empty_view)
        ctnTotalExpenses = findViewById(R.id.ll_total)
        rvListExpenses = findViewById(R.id.rv_expense)
        btnAddExpense = findViewById(R.id.btn_add_expenses)
        tvCategoriesLabel = findViewById(R.id.tv_categories_label)
        tvExpensesLabel = findViewById(R.id.tv_expenses_label)
        val btnCreateEmpty = findViewById<Button>(R.id.btn_create_empty)

        btnCreateEmpty.setOnClickListener {
            showCreateCategoryBottomSheet()
        }

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

        categoryAdapter.setOnLongClickListener { categoryToBeDeleted ->
            if (categoryToBeDeleted.category != R.drawable.ic_add &&
                categoryToBeDeleted.category != R.drawable.ic_all) {

                val title = this.getString(R.string.category_delete_title)
                val description = this.getString(R.string.category_delete_description)
                val btnText = this.getString(R.string.delete)

                showInfoDialog(
                    title,
                    description,
                    btnText
                ) {
                    val categoryEntityToBeDeleted = CategoryEntity(
                        id = categoryToBeDeleted.id,
                        category = categoryToBeDeleted.category,
                        isSelected = categoryToBeDeleted.isSelected,
                        color = categoryToBeDeleted.color
                    )
                    deleteCategory(categoryEntityToBeDeleted)
                }
            }
        }

        categoryAdapter.setOnClickListener { selected ->
            if (selected.category == R.drawable.ic_add) {
                showCreateCategoryBottomSheet()

            } else {
                val categoryTemp = categories.map { item ->
                    when {
                        item.category == selected.category && item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.category == selected.category && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.category != selected.category && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                }

                if (selected.category != R.drawable.ic_all) {
                    filterExpenseByCategoryName(selected.category)
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        getExpensesFromDataBase()
                    }
                }

                categoryAdapter.submitList(categoryTemp)
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

    private fun showInfoDialog(
        title: String,
        description: String,
        btnText: String,
        onClick: () -> Unit
    ) {
        val infoBottomSheet = InfoBottomSheet(
            title = title,
            description = description,
            btnText = btnText,
            onClick
        )

        infoBottomSheet.show(
            supportFragmentManager,
            "infoBottomSheet"
        )
    }

    private fun getCategoriesFromDataBase() {
        GlobalScope.launch(Dispatchers.IO) {
            val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
            categoriesEntity = categoriesFromDb

            GlobalScope.launch(Dispatchers.Main) {
                if(categoriesEntity.isEmpty()) {
                    rvListCategories.isVisible = false
                    rvListExpenses.isVisible = false
                    btnAddExpense.isVisible = false
                    ctnTotalExpenses.isVisible = false
                    tvCategoriesLabel.isVisible = false
                    tvExpensesLabel.isVisible = false
                    ctnEmptyView.isVisible = true
                } else {
                    rvListCategories.isVisible = true
                    rvListExpenses.isVisible = true
                    btnAddExpense.isVisible = true
                    ctnTotalExpenses.isVisible = true
                    tvCategoriesLabel.isVisible = true
                    tvExpensesLabel.isVisible = true
                    ctnEmptyView.isVisible = false
                }
            }
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

            val categoryListTemp = mutableListOf(
                CategoryUiData(
                    id = 0,
                    category = R.drawable.ic_all,
                    isSelected = true,
                    color = R.color.white
                )
            )

            categoryListTemp.addAll(categoriesUiData)
            GlobalScope.launch(Dispatchers.Main) {
                categories = categoryListTemp
                categoryAdapter.submitList(categories)
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
            updateTotalValue()
            expensesAdapter.submitList(expensesUiData)
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

    private fun deleteExpense(expenseEntity: ExpensesEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expensesDao.delete(expenseEntity)
            getExpensesFromDataBase()
        }
    }

    private fun deleteCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            val expensesToBeDeleted = expensesDao.getAllByCategoryName(categoryEntity.category)
            expensesDao.deleteAll(expensesToBeDeleted)
            categoryDao.delete(categoryEntity)
            getCategoriesFromDataBase()
            getExpensesFromDataBase()
        }
    }

    private fun filterExpenseByCategoryName(category: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val expensesFromDb: List<ExpensesEntity> = expensesDao.getAllByCategoryName(category)
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
                updateTotalValue()
                expensesAdapter.submitList(expensesUiData)
            }
        }
    }

    private fun showCreateUpdateExpenseBottomSheet(expensesUiData: ExpensesUiData? = null) {
        val createExpenseBottomSheet =
            CreateOrUpdateExpenseBottomSheet(
                expense = expensesUiData,
                userCategories = categoriesEntity,
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
                },
                onDeleteClicked = { expense ->
                    val expenseEntityDeleted = ExpensesEntity(
                        id = expense.id,
                        name = expense.name,
                        value = expense.value,
                        category = expense.category,
                        color = expense.color
                    )
                    deleteExpense(expenseEntityDeleted)
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

    private fun showCreateCategoryBottomSheet() {
        val createCategoryBottomSheet = CreateCategoryBottomSheet { category, color ->
            val categoryEntity = CategoryEntity(
                category = category,
                color = color,
                isSelected = false
            )
            insertCategory(categoryEntity)
        }
        createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
    }
}