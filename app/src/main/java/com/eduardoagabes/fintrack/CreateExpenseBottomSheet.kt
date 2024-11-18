import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eduardoagabes.fintrack.CategoryListAdapter
import com.eduardoagabes.fintrack.CategoryUiData
import com.eduardoagabes.fintrack.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateExpenseBottomSheet(
    private val userCategories: List<CategoryUiData>,
    private val onCategorySelected: (CategoryUiData) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var categoriesAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_expense_bottom_sheet, container, false)

        val rvCategories = view.findViewById<RecyclerView>(R.id.rv_selecet_category)

        rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        categoriesAdapter = CategoryListAdapter()
        rvCategories.adapter = categoriesAdapter

        categoriesAdapter.submitList(userCategories)

        categoriesAdapter.setOnClickListener { category ->
            val updatedCategory = category.copy(isSelected = !category.isSelected)

            val updatedList = userCategories.map { item ->
                if (item.id == category.id) updatedCategory else item
            }

            categoriesAdapter.submitList(updatedList)
            onCategorySelected(updatedCategory)

            dismiss()
        }

        return view
    }
}



