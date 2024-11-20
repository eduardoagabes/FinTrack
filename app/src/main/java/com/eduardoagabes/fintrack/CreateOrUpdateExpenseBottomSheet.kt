import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eduardoagabes.fintrack.CategoryListAdapter
import com.eduardoagabes.fintrack.CategoryUiData
import com.eduardoagabes.fintrack.ExpensesUiData
import com.eduardoagabes.fintrack.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateOrUpdateExpenseBottomSheet(
    private val userCategories: List<CategoryUiData>,
    private val expense: ExpensesUiData? = null,
    private val onCategorySelected: (CategoryUiData, Double, String) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var categoriesAdapter: CategoryListAdapter
    private var selectedCategory: CategoryUiData? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.create_or_update_expense_bottom_sheet, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val rvCategories = view.findViewById<RecyclerView>(R.id.rv_selecet_category)
        val btnCreate = view.findViewById<Button>(R.id.btn_create_expense)
        val edtValue = view.findViewById<TextInputEditText>(R.id.tie_value_expense)
        val edtExpense = view.findViewById<TextInputEditText>(R.id.tie_expense)

        categoriesAdapter = CategoryListAdapter()
        rvCategories.adapter = categoriesAdapter

        categoriesAdapter.submitList(userCategories)

        rvCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (expense == null) {
            tvTitle.setText(R.string.create_expense_title)
            btnCreate.setText(R.string.create)
        } else {
            tvTitle.setText(R.string.update_expense_title)
            btnCreate.setText(R.string.update)
            edtExpense.setText(expense.name)
            edtValue.setText(expense.value)

            selectedCategory = userCategories.find { it.category == expense.category }
            userCategories.forEach {
                it.isSelected = it.category == expense.category
            }
            categoriesAdapter.notifyDataSetChanged()
        }

        btnCreate.setOnClickListener {
            val value = edtValue.text.toString().toDoubleOrNull()
            val expense = edtExpense.text.toString()

            if (selectedCategory == null || value == null || expense.isEmpty()) {
                Snackbar.make(view, "Please fill all fields.", Snackbar.LENGTH_SHORT).show()
            } else {
                onCategorySelected.invoke(selectedCategory!!, value ?: 0.0, expense)
                dismiss()
            }
        }

        categoriesAdapter.setOnClickListener { category ->
            userCategories.forEach { it.isSelected = false }
            category.isSelected = true
            selectedCategory = category
            categoriesAdapter.notifyDataSetChanged()
        }

        return view
    }
}



