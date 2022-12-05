package com.example.memoi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memoi.repository.TodoRepository
import com.example.memoi.todo.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.ArrayList

class TodoListViewModel: ViewModel() {
    private val repository = TodoRepository()

    private val _todoList = MutableLiveData<ArrayList<Todo>>()
    val todoList : LiveData<ArrayList<Todo>> get() = _todoList

    // will be true, when repository.selectTodo() is done
    var isReady = false

    init {
        update()
    }

    fun update() {
        _todoList.value = repository.selectTodo(this)
    }

    fun add(todo: Todo) {
        this._todoList.value?.add(todo)
        repository.insertTodo(todo)
    }

    fun getTodayList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {

            for (todo in this) {
                // don't get non-today
                if (todo.date!=null && (LocalDate.parse(todo.date)).isEqual(LocalDate.now()))
                    resList.add(todo)
            }
        }

        return resList
    }

    fun getList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {
            // deep-copy all instances
            for (todo in this) {
                resList.add(todo)
            }
        }

        return resList
    }

}
