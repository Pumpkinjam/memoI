package com.example.memoi.repository

import androidx.lifecycle.MutableLiveData
import com.example.memoi.todo.Todo
import com.example.memoi.todo.TodoBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.reflect.InvocationTargetException
import kotlin.collections.HashMap

class TodoRepository {
    val database = Firebase.database

    fun observeTodo(key: String, todoData: MutableLiveData<Todo>) {

        val todoRef = database.getReference("list/$key")

        todoRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                // snapshot.value becomes java.util.HashMap
                val hm = snapshot.value as? HashMap<*, *>

                // initializing arrayList for TodoBuilder
                val arr = ArrayList<String?>(6)
                for (i in 0..6) {
                    arr.add(null)
                }

                if (hm == null) println("Why???")
                // todo : solve null-cast problem
                for (mapKey in hm!!.keys) {
                    //arr.add(java.lang.String.valueOf(hm[key]))
                    val idx = when (mapKey) {
                        "title" -> 0
                        "description" -> 1
                        "date" -> 2
                        "time" -> 3
                        "location" -> 4
                        else -> 5   // index for garbage data (ex. timestamp)
                    }
                    arr[idx] = (hm[mapKey])?.toString()
                }

                // construct with ArrayList
                val res = TodoBuilder(arr)
                todoData.postValue(res.build())
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get value")
            }
        })
    }


    fun postTodo(key: String, newTodo: Todo) {
        val todoRef = database.getReference("list/$key")
        //val csvRef = database.getReference("list/$key")
        println("posting Todo...")

        try {
            todoRef.setValue(newTodo)
        }
        catch (e: InvocationTargetException) {
            e.targetException.printStackTrace()
            return;
        }
        catch (e1: DatabaseException) {
            e1.printStackTrace()
            return;
        }
    }

    fun removeTodo(key: String) {
        database.getReference("list/$key").removeValue()
    }
/*
    fun observeCsv(key: String, todoCsv: MutableLiveData<String>) {

        val csvRef = database.getReference("list/$key")


        csvRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                todoCsv.postValue(snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get value")
            }
        })
    }


    fun postCsv(key: String, newCsv: String) {
        val csvRef =
            database.getReferenceFromUrl(
                "https://memoi-bced4-default-rtdb.firebaseio.com/list/$key")
        //val csvRef = database.getReference("list/$key")
        csvRef.setValue(newCsv)
    }

    fun removeCsv(key: String) {
        database.getReference("list/$key").removeValue()
    }*/

}