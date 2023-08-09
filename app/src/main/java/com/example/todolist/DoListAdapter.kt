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


class DoListAdapter(var context: Context, var itemList: ArrayList<String>, var fileHelper: FileHelper):
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
        holder.task.text = itemList[position]
        holder.priority.setImageResource(R.drawable.baseline_flag_unmarked)
        holder.priority.setOnClickListener {  }
        holder.isDone.setOnClickListener {
            if (holder.isDone.isChecked){
                holder.task.paintFlags = holder.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                holder.task.paintFlags = 0
            }
        }
        holder.priority.setOnClickListener {
            buttonPressed = !buttonPressed
            val item = itemList[position]
            if (buttonPressed){
                holder.priority.setImageResource(R.drawable.baseline_flag_marked)
                itemList.remove(item)
                notifyItemRemoved(holder.adapterPosition)
                //notifyItemRangeChanged(holder.adapterPosition, itemList.size)
                itemList.add(0, item)
                holder.priority.setImageResource(R.drawable.baseline_flag_marked)
                notifyItemInserted(0)
                //notifyItemRangeChanged(0, itemList.size)
            }else{
                holder.priority.setImageResource(R.drawable.baseline_flag_unmarked)
                itemList.remove(item)
                notifyItemRemoved(holder.adapterPosition)
                //notifyItemRangeChanged(0, itemList.size)
                itemList.add(holder.oldPosition, item)
                notifyItemInserted(holder.oldPosition)
                //notifyItemRangeChanged(holder.oldPosition, itemList.size)
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
                    fileHelper.writeData(itemList, context)
                }
                .create()
                .show()
        }

    }
}
