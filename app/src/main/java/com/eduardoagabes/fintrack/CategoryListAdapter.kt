package com.eduardoagabes.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CategoryListAdapter :
    ListAdapter<CategoryUiData, CategoryListAdapter.CategoryViewHolder>(CategoryDiffUtils) {

    private lateinit var onClick: (CategoryUiData) -> Unit

    fun setOnClickListener(onClick: (CategoryUiData) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categories = getItem(position)
        holder.bind(categories, onClick)
    }

    class CategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val ivCategory = view.findViewById<ImageView>(R.id.icon_category)

        fun bind(categoryUiData: CategoryUiData, onClick: (CategoryUiData) -> Unit) {
            ivCategory.setImageResource(categoryUiData.icon)

            ivCategory.isSelected = categoryUiData.isSelected

            if (categoryUiData.isSelected) {
                ivCategory.setBackgroundResource(R.drawable.selected_button_bg)
            } else {
                ivCategory.setBackgroundResource(R.drawable.filter_chips_background)
            }

            view.setOnClickListener {
                onClick.invoke(categoryUiData)
            }
        }
    }

    companion object CategoryDiffUtils : DiffUtil.ItemCallback<CategoryUiData>() {
        override fun areItemsTheSame(oldItem: CategoryUiData, newItem: CategoryUiData): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: CategoryUiData, newItem: CategoryUiData): Boolean {
            return oldItem.icon == newItem.icon
        }

    }

}