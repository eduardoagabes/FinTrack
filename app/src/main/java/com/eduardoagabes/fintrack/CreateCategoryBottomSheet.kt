package com.eduardoagabes.fintrack

import ColorAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateCategoryBottomSheet
 : BottomSheetDialogFragment() {

    private var selectedCategory: CategoryUiData? = null
    private var selectedColor: Int = R.color.white
    private var selectedButton: Button? = null
    private var categoriesEntity = listOf<CategoryEntity>()

    private lateinit var colorAdapter: ColorAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_category_botton_sheet, container, false)

        val rvColors = view.findViewById<RecyclerView>(R.id.rv_set_color)
        rvColors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        val rvCreateCategory = view.findViewById<RecyclerView>(R.id.rv_selecet_category)
        rvCreateCategory.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        val listCategories = mutableListOf<CategoryUiData>()
        getCategory(listCategories)

        val categoriesAdapter = CategoryListAdapter()
        categoriesAdapter.setOnClickListener { categoria ->
            listCategories.forEach { it.isSelected = false }
            categoria.isSelected = true
            selectedCategory = categoria
            categoriesAdapter.notifyDataSetChanged()
        }

        colorAdapter = ColorAdapter(listOf(Color.RED, Color.GREEN, Color.BLUE)) { color ->
            selectedColor = color
            colorAdapter.notifyDataSetChanged()
        }

        rvCreateCategory.adapter = categoriesAdapter
        categoriesAdapter.submitList(listCategories)

        rvColors.adapter = colorAdapter

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
