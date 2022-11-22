package com.example.memoi.repository

import androidx.lifecycle.MutableLiveData
import com.example.memoi.todo.Todo
import com.example.memoi.todo.TodoBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.HashMap

class TodoRepository {
    val database = Firebase.database

    fun observeTodo(key: String, todoData: MutableLiveData<Todo>) {

        val todoRef = database.getReference("list/$key")


        todoRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // snapshot.value becomes java.util.HashMap
                val hm = snapshot.value as HashMap<*, String>
                val res = TodoBuilder(
                    hm["title"], hm["description"],
                    hm["date"], hm["time"], hm["location"]
                )
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
        todoRef.setValue(newTodo)
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