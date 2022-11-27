package com.example.memoi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memoi.repository.TodoRepository
import com.example.memoi.todo.*
import java.time.LocalDate
import java.util.ArrayList

class TodoListViewModel: ViewModel() {
    private val repository = TodoRepository()

    private val _todoList = MutableLiveData<ArrayList<Todo>>()
    val todoList : MutableLiveData<ArrayList<Todo>> get() = _todoList

    init {
        _todoList.value = ArrayList<Todo>()
    }

    fun add(todo: Todo) {
        this._todoList.value?.add(todo)
        repository.postTodo(todo.created, todo)
        repository.observeTodo(todo.created, MutableLiveData(this._todoList.value?.last()) )
    }

    fun getTodayList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {

            for (todo in this) {
                // don't get non-today
                if (todo.date==null || (LocalDate.parse(todo.date)).isEqual(LocalDate.now()))
                    resList.add(todo)
            }
        }

        return resList
    }

    fun getList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {

            for (todo in this) {
                resList.add(todo)
            }
        }

        return resList
    }

}