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

    private val _csvList = ArrayList<MutableLiveData<String>>()
    val csvList : ArrayList<MutableLiveData<String>> get() = _csvList

/*  overloaded&merged by fun add(Task)
    fun add(todo: Todo) {
        val newCsv = todo.toCsvFormat()

        this._csvList.add(MutableLiveData<String>(newCsv))
        repository.postCsv(todo.created, newCsv)
        repository.observeCsv(todo.created, this._csvList.last())
    }
*/
    fun add(task: Task) {
        //val newCsv = if (task is Todo) (task as Todo).toCsvFormat() else task.toCsvFormat()
        val newCsv = task.toCsvFormat()

        this._csvList.add(MutableLiveData<String>(newCsv))
        repository.postCsv(task.created, newCsv)
        repository.observeCsv(task.created, this._csvList.last())
    }

    fun getTodoList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()
        for (data in _csvList) {
            val str = data.value!!

            // don't get past-Task
            val tmp = TodoBuilder.of(str).build() as Todo
            if (tmp.date==null || !tmp.date.isBefore(LocalDate.now())) {
                resList.add(tmp)
            }
        }

        return resList
    }

    fun getTaskList(): ArrayList<Task> {
        val resList = ArrayList<Task>()
        for (data in _csvList) {
            val str = data.value!!
            resList.add(TaskBuilder.of(str).build())
        }

        return resList
    }

}