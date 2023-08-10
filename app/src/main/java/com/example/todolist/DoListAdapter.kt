package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
    Для того, щоб реалізувати цю логіку з прапорцями,
    тобі потрібно при створенні нового item в todo створювати не просто новий string, а новий
    обʼєкт в якому будуть 2 поля (TodoItem.kt). Та при зміні стану прапорця
*/
class DoListAdapter(var context: Context, var itemList: MutableList<TodoItem>, var fileHelper: FileHelper):
    RecyclerView.Adapter<DoListAdapter.DoListHolder>(){

    class DoListHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val task: TextView = itemView.findViewById(R.id.textView)
        val isDone: CheckBox = itemView.findViewById(R.id.checkBox)
        val priority: ImageButton = itemView.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoListHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return DoListHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DoListHolder, position: Int) {
        var buttonPressed = false
        //Варто виносити вьюхи в окремі змінні для того, щоб було простіше читати код
        val priorityBtn: ImageButton = holder.priority
        val taskTextView: TextView = holder.task

        taskTextView.text = itemList[position].content
        //Ось тут при створенні чи оновленні елемента в recyclerview перевіряємо чи checked флажок
        if (itemList[position].isChecked) {
            priorityBtn.setImageResource(R.drawable.baseline_flag_marked)
        } else {
            priorityBtn.setImageResource(R.drawable.baseline_flag_unmarked)
        }
        holder.isDone.setOnClickListener {
            if (holder.isDone.isChecked){
                taskTextView.paintFlags = taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                taskTextView.paintFlags = 0
            }
        }
        priorityBtn.setOnClickListener {
            val item = itemList[position]
            if (!item.isChecked){
                itemList.remove(item)
                notifyItemRemoved(holder.adapterPosition)
                itemList.add(0, item)
                itemList[0].isChecked = true
                notifyItemRangeChanged(0, itemList.size)
            } else {
                itemList[position].isChecked = false
                notifyItemRangeChanged(0, itemList.size)
            }
        }
        holder.itemView.setOnClickListener {
            val alert = AlertDialog.Builder(context)
            alert.setTitle("Delete")
                .setMessage("Do you want to delete this task?")
                .setCancelable(false)
                .setIcon(R.drawable.baseline_delete_forever_24)
                .setNegativeButton("no") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("yes") { _, _ ->
                    itemList.removeAt(position)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, itemList.size)
                    fileHelper.writeData(itemList.map { it.content }.toMutableList(), context)
                }
                .create()
                .show()
        }

    }
}
