package com.example.memoi.repository

import androidx.lifecycle.MutableLiveData
import com.example.memoi.todo.Todo
import com.example.memoi.todo.TodoBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.*
import com.google.firebase.ktx.Firebase
import java.lang.reflect.InvocationTargetException
import kotlin.collections.HashMap

class TodoRepository {

    // static field
    companion object {
        var n = 1;
        var snapCatcher: DataSnapshot? = null;
    }


    private val database = Firebase.database

    fun getTodo(key: String): Todo {

        val tmpRef = database.getReference("list")

        tmpRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                TodoRepository.snapCatcher = snapshot
            }

            override fun onCancelled(error: DatabaseError) {
                println("oh... error occurred at TodoRepository.getTodo")
                println(error.message)
            }
        })

        tmpRef.setValue(getN())

        // actually non-null (maybe)
        return (snapCatcher!!.child(key).getValue() as Todo)
    }

    private fun getN(): Int {
        n = (n+1)%12345678;
        return n
    }

    fun observeTodo(key: String, todoData: MutableLiveData<Todo>) {

        val todoRef = database.getReference("list/$key")

        todoRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                // snapshot.value becomes java.util.HashMap
                val hm = snapshot.value as? HashMap<*, *>

                // initializing arrayList for TodoBuilder
                val arr = ArrayList<String?>(7)
                for (i in 0..7) {
                    arr.add(null)
                }

                if (hm == null) println("Why???")
                // todo : solve null-cast problem
                for (mapKey in hm!!.keys) {
                    //arr.add(java.lang.String.valueOf(hm[key]))
                    val idx = when (mapKey) {
                        "created" -> 0
                        "title" -> 1
                        "description" -> 2
                        "date" -> 3
                        "time" -> 4
                        "location" -> 5
                        else -> 6   // index for garbage data (ex. timestamp)
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

    fun postTodo(newTodo: Todo) {
        val todoRef = database.getReference("list/${newTodo.created}")
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

    fun removeTodo(todo: Todo) {
        database.getReference("list/${todo.created}").removeValue()
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