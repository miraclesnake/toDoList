package com.example.todolist

import android.content.Context
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper {
    private val filename = "listinfo.dat"

    fun writeData(item: ArrayList<String>, context: Context){
        val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        val oas = ObjectOutputStream(fos)
        oas.writeObject(item)
        oas.close()
    }

    fun readData(context: Context): ArrayList<String> {
        val itemList: ArrayList<String>
        itemList = try{
            val fis: FileInputStream = context.openFileInput(filename)
            val ois = ObjectInputStream(fis)
            ois.readObject() as ArrayList<String>
        }catch (e: FileNotFoundException){
            ArrayList()
        }


        return itemList
    }
}