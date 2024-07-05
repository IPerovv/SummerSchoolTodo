package com.example.todoapplication.after_reg.features.todo.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R

import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class JobAdapter(
    private val onClick: (String) -> Unit,
    private val onCheckedChange: (TodoItem) -> Unit
) :
    ListAdapter<TodoItem, JobAdapter.Holder>(createDefaultComparator()) {

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTodoBinding.bind(view)

        fun bind(todoItem: TodoItem) = with(binding) {
            todoBodyTv.text = todoItem.todo
            todoChb.isChecked = todoItem.completed
            todoItem.deadline?.let {
                deadlineTv.text = dateFormat.format(it)
                deadlineTv.visibility = View.VISIBLE
            } ?: run {
                deadlineTv.visibility = View.GONE
            }
            binding.toDetailedTodoBt.setOnClickListener {
                onClick.invoke(todoItem.id)
            }
            binding.todoChb.setOnCheckedChangeListener { _, isChecked ->
                val updatedItem = todoItem.copy(completed = isChecked)
                onCheckedChange.invoke(updatedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getCompletedCount(): Int {
        return currentList.count { it.completed }
    }

    companion object {
        private fun createDefaultComparator(): DiffUtil.ItemCallback<TodoItem> {
            return object : DiffUtil.ItemCallback<TodoItem>() {
                override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
}