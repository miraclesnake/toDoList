package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var listAdapter: DoListAdapter
    lateinit var mainBinding: ActivityMainBinding
    var itemList = mutableListOf<TodoItem>()
    var fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        itemList = fileHelper.readData(applicationContext)
            .map {TodoItem(it, false)}
            .toMutableList()
        listAdapter = DoListAdapter(this@MainActivity, itemList, fileHelper)

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = listAdapter
        mainBinding.button.setOnClickListener {
            val itemName: String = mainBinding.editText.text.toString()
            itemList.add(
                TodoItem(itemName, false)
            )
            mainBinding.editText.setText("")
            fileHelper.writeData(itemList.map{it.content}.toMutableList(), applicationContext)
            listAdapter.notifyItemInserted(itemList.size - 1)
        }
    }
}
