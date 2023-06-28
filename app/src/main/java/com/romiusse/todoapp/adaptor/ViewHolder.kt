package com.romiusse.todoapp.adaptor

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.romiusse.todoapp.databinding.ItemTaskBinding
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.utils.Utils

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val binding = ItemTaskBinding.bind(itemView)
    var infoItem = binding.infoItem
    var deadlineItem = binding.deadlineItem
    var checkBoxItem = binding.checkboxItem
    var priorityIcon = binding.priorityIcon
    var textItem = binding.textItem


    fun bind(item: TodoItem) = with(binding) {
        textItem.text = item.text
        if (item.deadline != null) deadlineItem.text =
            item.deadline?.let { Utils.convertDateToString(it) }
        else deadlineItem.text = ""
    }

}