package com.example.memoi.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.memoi.todo.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.*
//import retrofit2.*
//import retrofit2.http.GET
import java.lang.reflect.InvocationTargetException

class TodoRepository {
    val database = Firebase.firestore

    suspend fun selectTodo() {
        database.collection("/todoList")
            .get()
            .addOnSuccessListener { result ->
                println("/////////////\nsucceed\n///////////")
                // todo: return it.
                for (todo in result) {
                    Log.d(TAG, "${todo.id} : ${todo.data}")
                }
            }
            .addOnFailureListener { e ->
                println("\n\n\n\n\n\nfailed.\n\n\n\n")
                // better exception handling...?
                //e.printStackTrace()
                throw e
            }
    }

    fun insertTodo(key: String, newTodo: Todo) {
        val obj = with(newTodo) {
            hashMapOf(
                "created" to created,
                "title" to title,
                "description" to (description ?: "null"),
                "date" to (date ?: "null"),
                "time" to (time ?: "null"),
                "url" to (url ?: "null")
            )
        }

        database.collection("todoList")
            .add(obj)
            .addOnSuccessListener { _ ->
                println("post succeed")
            }
            .addOnFailureListener { _ ->
                println("post failed")
            }
    }

    fun removeTodo(key: String) {

    }

}