package com.example.memoi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.MemoI.TaskAdapter
import com.example.MemoI.TodoAdapter
import com.example.memoi.databinding.FragmentTodoListBinding
import com.example.memoi.todo.Task
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.util.ArrayList

class TaskListFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var taskList: ArrayList<Task>

    lateinit var binding: FragmentTodoListBinding
    lateinit var parentActivity: Activity

    // getting attached activity.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        taskList = ArrayList<Task>()

        // todo: get todoList&taskList from viewModel

        // or... binding.recTodo.layoutManager = LinearLayoutManager(activity)
        binding.recTodo.layoutManager = LinearLayoutManager(parentActivity)
        binding.recTodo.adapter = TaskAdapter(taskList)

        return binding.root
    }

    override fun onPause() {
        //todo: make mvvm work correctly
        super.onPause()
    }

}