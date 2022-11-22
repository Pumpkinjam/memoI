package com.example.memoi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memoi.repository.TodoRepository
import com.example.memoi.todo.*
import java.time.LocalDate
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class TodoListViewModel: ViewModel() {
    private val repository = TodoRepository()

    private val _todoList = ArrayList<MutableLiveData<Todo>>()
    val todoList : ArrayList<MutableLiveData<Todo>> get() = _todoList


    fun add(todo: Todo) {
        this._todoList.add(MutableLiveData<Todo>(todo))
        repository.postTodo(todo.created, todo)
        repository.observeTodo(todo.created, this._todoList.last())
    }

    fun getList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        for (data in _todoList) {
            if (data.value != null) {
                val tmp = data.value!!

                // don't get past-Task
                if (tmp.date==null || !(LocalDate.parse(tmp.date)).isBefore(LocalDate.now()))
                    resList.add(tmp)
            }
            else continue
        }

        return resList
    }

    fun getAllList(): ArrayList<Task> {
        val resList = ArrayList<Task>()
        for (data in _todoList) {
            if (data.value != null) resList.add(data.value!!)
        }

        return resList
    }

}