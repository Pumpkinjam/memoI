package com.example.memoi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memoi.repository.TodoRepository
import com.example.memoi.todo.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class TodoListViewModel: ViewModel() {
    private val repository = TodoRepository()

    private val _todoList = MutableLiveData<ArrayList<Todo>>()
    val todoList : LiveData<ArrayList<Todo>> get() = _todoList

    fun retrieveTodoList() {

        viewModelScope.launch {
            repository.readList()?.let { jsonTodos ->
                val list = ArrayList<Todo>()/*
                for (idx in 0 until jsonTodos.length()) {
                    jsonTodos.getJSONObject(idx)
                }*/
            }

        }
    }

    fun add(t: Todo) {

    }

}