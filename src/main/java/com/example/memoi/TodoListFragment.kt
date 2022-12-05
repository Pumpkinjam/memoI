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
import com.example.memoi.TodoAdapter
import com.example.memoi.databinding.FragmentTodayListBinding
import com.example.memoi.databinding.FragmentTodoListBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.util.ArrayList
import kotlin.concurrent.thread

class TodoListFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var todoList: ArrayList<Todo>

    var binding: FragmentTodoListBinding? = null
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
        todoList = vm.getTodayList()

        binding?.recTodo?.layoutManager = LinearLayoutManager(parentActivity)
        binding?.recTodo?.adapter = TodoAdapter(todoList)

        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        thread(start=true) {
            if (!vm.isReady) {
                while (!vm.isReady);
                //setRecyclerView()

                todoList = vm.getTodayList()

                activity?.runOnUiThread {
                    binding?.recTodo?.layoutManager = LinearLayoutManager(parentActivity)
                    binding?.recTodo?.adapter = TodoAdapter(todoList)
                }
                // debugging: for check
                for (t in todoList) {
                    println(t)
                }
            }
        }
    }

}