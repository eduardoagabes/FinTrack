package com.eduardoagabes.fintrack

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ExpensesListAdapter :
    ListAdapter<ExpensesUiData, ExpensesListAdapter.ExpensesViewHolder>(ExpensesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_expenses, parent, false)
        return ExpensesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val expenses = getItem(position)
        holder.bind(expenses)
    }

    class ExpensesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivExpenses = view.findViewById<ImageView>(R.id.iv_expenses)
        private val tvName = view.findViewById<TextView>(R.id.tv_name_expense)
        private val tvValue = view.findViewById<TextView>(R.id.tv_value)
        private val setColor = view.findViewById<View>(R.id.set_color_expense)

        fun bind(expensesUiData: ExpensesUiData) {
            ivExpenses.setImageResource(expensesUiData.icon)
            tvName.text = expensesUiData.name
            tvValue.text = "- $ ${expensesUiData.value}"


            val background = setColor.background as GradientDrawable
            background.setColor(expensesUiData.color)

        }
    }

    class ExpensesDiffUtils : DiffUtil.ItemCallback<ExpensesUiData>() {
        override fun areItemsTheSame(oldItem: ExpensesUiData, newItem: ExpensesUiData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExpensesUiData, newItem: ExpensesUiData): Boolean {
            return oldItem.name == newItem.name
        }
    }
}