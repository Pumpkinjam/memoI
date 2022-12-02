package com.example.memoi.repository

import com.example.memoi.todo.Todo
import com.google.firebase.database.DatabaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.*
//import retrofit2.*
//import retrofit2.http.GET
import java.lang.reflect.InvocationTargetException

class TodoRepository {
    val database = FirebaseFirestore.getInstance()



    fun postTodo(key: String, newTodo: Todo) {

    }

    fun removeTodo(key: String) {

    }

}