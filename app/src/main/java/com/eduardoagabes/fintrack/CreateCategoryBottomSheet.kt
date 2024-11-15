package com.eduardoagabes.fintrack

import ColorAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateCategoryBottomSheet(
    private val onCreateClicked: (Int, Int) -> Unit
) : BottomSheetDialogFragment() {

    private var selectedCategory: CategoryUiData? = null
    private var selectedColor: Int = R.color.white

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_category_bottom_sheet, container, false)

        val rvCreateCategory = view.findViewById<RecyclerView>(R.id.rv_selecet_category)
        val btnCreate = view.findViewById<Button>(R.id.btn_category_create)
        val rvColors = view.findViewById<RecyclerView>(R.id.rv_set_color)

        // poner snackbar
        btnCreate.setOnClickListener {
            val category = selectedCategory?.category ?: R.drawable.ic_home
            val color = selectedColor
            onCreateClicked.invoke(category, color)
            dismiss()
        }

        rvColors.adapter = ColorAdapter(
            listOf(
                ContextCompat.getColor(requireContext(), R.color.white),
                ContextCompat.getColor(requireContext(), R.color.grey),
                ContextCompat.getColor(requireContext(), R.color.light_orange),
                ContextCompat.getColor(requireContext(), R.color.light_yellow),
                ContextCompat.getColor(requireContext(), R.color.water_blue),
                ContextCompat.getColor(requireContext(), R.color.green),
                ContextCompat.getColor(requireContext(), R.color.red),
                ContextCompat.getColor(requireContext(), R.color.black),
                ContextCompat.getColor(requireContext(), R.color.pink),
                ContextCompat.getColor(requireContext(), R.color.blue),
                ContextCompat.getColor(requireContext(), R.color.brown),
                ContextCompat.getColor(requireContext(), R.color.violet)
            )
        ) { selectedColor ->
            this.selectedColor = selectedColor
        }

        rvCreateCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rvColors.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val listCategories = mutableListOf<CategoryUiData>()
        getCategory(listCategories)

        val categoriesAdapter = CategoryListAdapter()
        categoriesAdapter.setOnClickListener { category ->
            listCategories.forEach { it.isSelected = false }
            category.isSelected = true
            selectedCategory = category
            categoriesAdapter.notifyDataSetChanged()
        }

        rvCreateCategory.adapter = categoriesAdapter
        categoriesAdapter.submitList(listCategories)

        return view
    }

    private fun getCategory(listCategories: MutableList<CategoryUiData>) {
        listCategories.apply {
            add(CategoryUiData(0, R.drawable.ic_home, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_food, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_key, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_internet, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_clothes, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_game, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_car, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_credit_card, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_eletricity, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_gas, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_graphic, false, R.color.white))
            add(CategoryUiData(0, R.drawable.ic_water, false, R.color.white))
        }
    }
}