package com.example.todoapplication.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.databinding.ItemTodoBinding
import com.example.todoapplication.domain.model.ImportanceLevel
import com.example.todoapplication.domain.model.TodoItem
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

            itemView.contentDescription = accMakeDescription(todoItem, dateFormat)
            ViewCompat.replaceAccessibilityAction(
                itemView,
                AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
                "изменить состояние"
            ) { view, arguments ->
                binding.todoChb.isChecked = !binding.todoChb.isChecked

                val state = if (binding.todoChb.isChecked) "выполнено" else "не выполнено"
                itemView.announceForAccessibility("Состояние изменено на $state")

                val updatedItem = todoItem.copy(completed = binding.todoChb.isChecked)
                onCheckedChange.invoke(updatedItem)

                true
            }
            ViewCompat.replaceAccessibilityAction(
                binding.toDetailedTodoBt,
                AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
                "перейти"
            ) { view, arguments ->
                onClick.invoke(todoItem.id)
                true
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

fun accMakeDescription(todoItem: TodoItem, dateFormat: SimpleDateFormat): String {
    val accImportanceDescription = when (todoItem.importance) {
        ImportanceLevel.IMPORTANT -> "Высокая"
        ImportanceLevel.LOW -> "Низкая"
        ImportanceLevel.BASIC -> "Нет"
    }
    val accDeadlineText =
        todoItem.deadline?.let { "до ${dateFormat.format(it)}" } ?: "без даты выполнения"
    return "Дело: ${todoItem.todo}, важность $accImportanceDescription, $accDeadlineText"
}
