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
            /*  todo: fix this
                // snapshot.value becomes java.util.HashMap
                val hm = snapshot.value as HashMap<*, String>
                val res = TodoBuilder(
                    hm["title"], hm["description"],
                    hm["date"], hm["time"],
                    hm["location"], hm["url"]
                )
                todoData.postValue(res.build())*/
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get value")
            }
        })
    }


    fun postTodo(key: String, newTodo: Todo) {
        val todoRef = database.getReference("list/$key")
        //val csvRef = database.getReference("list/$key")
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

}