package com.romiusse.todo_list.adaptors

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.romiusse.todoapp.R
import com.romiusse.todoapp.todoList.PriorityItem
import com.romiusse.todoapp.todoList.TodoItem
import java.util.Date


typealias CheckBoxListener = (item: TodoItem) -> Unit
typealias ItemTouchListener = (item: TodoItem) -> Unit

class Adapter: RecyclerView.Adapter<ViewHolder>() {


    private var checkBoxListener: CheckBoxListener? = null
    private var itemTouchListener: ItemTouchListener? = null

    var items = emptyList<TodoItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue){
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task,
            null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var item = items[position]

        holder.bind(item)


        when (item.priority) {
            PriorityItem.HIGH -> {
                holder.priorityIcon.visibility = View.VISIBLE
                holder.priorityIcon.setImageResource(R.drawable.ic_priority)
            }
            PriorityItem.LOW -> {
                holder.priorityIcon.visibility = View.VISIBLE
                holder.priorityIcon.setImageResource(R.drawable.ic_arrow_to_down)
            }
            else -> {
                holder.priorityIcon.visibility = View.GONE
                holder.priorityIcon.setImageResource(R.drawable.ic_arrow_to_down)
            }
        }


        if (item.flag) {
            holder.checkBoxItem.isChecked = true
            holder.textItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else{
            holder.checkBoxItem.isChecked = false
            holder.textItem.paintFlags = Paint.ANTI_ALIAS_FLAG
        }

        if (item.priority == PriorityItem.HIGH) {
            val tint = ContextCompat.getColorStateList(holder.itemView.context,R.drawable.checkbox_extra_tint)
            holder.checkBoxItem.buttonTintList = tint
        } else{
            val tint = ContextCompat.getColorStateList(holder.itemView.context,R.drawable.checkbox_normal_tint)
            holder.checkBoxItem.buttonTintList = tint
        }

        val drawable = AppCompatResources.getDrawable(
            holder.itemView.context,
            R.drawable.checkbox_extra_drawable
        )
        holder.checkBoxItem.buttonDrawable = drawable


        holder.checkBoxItem.setOnClickListener {
            item.flag = holder.checkBoxItem.isChecked
            item.changedAt = Date()
            notifyCheckBoxChanges(item)
        }

        holder.itemView.setOnClickListener { notifyTouches(item) }

    }

    fun setCheckBoxListener(listener: CheckBoxListener){
        checkBoxListener = listener
    }


    fun setItemTouchListener(listener: ItemTouchListener){
        itemTouchListener = listener
    }

    private fun notifyCheckBoxChanges(item: TodoItem){
        checkBoxListener?.let{it.invoke(item)}
    }

    private fun notifyTouches(item: TodoItem){
        itemTouchListener?.let{it.invoke(item)}
    }

}
