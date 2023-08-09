package com.example.todolist

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ListItemBinding

class MainActivity : AppCompatActivity() {
    lateinit var listAdapter: DoListAdapter
    lateinit var mainBinding: ActivityMainBinding
    var itemList = ArrayList<String>()
    var fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        itemList = fileHelper.readData(applicationContext)
        listAdapter = DoListAdapter(this@MainActivity, itemList, fileHelper)

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = listAdapter
        mainBinding.button.setOnClickListener {
            val itemName: String = mainBinding.editText.text.toString()
            itemList.add(itemName)
            mainBinding.editText.setText("")
            fileHelper.writeData(itemList, applicationContext)
            listAdapter.notifyItemInserted(itemList.size - 1)
        }
    }
}
