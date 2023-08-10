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
    // Я позмінював ArrayList на MutableList, по суті це одне й те ж саме, але з MutableList працювати
    // особисто мені легше, але під капотом там по суті тей же ArrayList
    var itemList = mutableListOf<TodoItem>()
    var fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //Зчитуємо список з файлу та відразу його трансформуємо за допомогою map в масив з TodoItem
        itemList = fileHelper.readData(applicationContext).map {
            TodoItem(
                it,
                false
            )
        }.toMutableList()
        listAdapter = DoListAdapter(this@MainActivity, itemList, fileHelper)

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = listAdapter
        mainBinding.button.setOnClickListener {
            val itemName: String = mainBinding.editText.text.toString()
            itemList.add(
                TodoItem(itemName, false)
            )
            mainBinding.editText.setText("")
            fileHelper.writeData(itemList.map { it.content }.toMutableList(), applicationContext)
            listAdapter.notifyItemInserted(itemList.size - 1)
        }
    }
}
