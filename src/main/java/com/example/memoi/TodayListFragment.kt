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
import com.example.memoi.databinding.FragmentTodayListBinding
import com.example.memoi.todo.Task
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.time.LocalDate
import java.util.ArrayList

class TodayListFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var todoList: ArrayList<Todo>

    lateinit var binding: FragmentTodayListBinding
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
        binding = FragmentTodayListBinding.inflate(inflater, container, false)
        todoList = ArrayList<Todo>()

        // todo: get todoList&taskList from viewModel
        val todayList = ArrayList<Todo>()
        for (todo in todoList) {
            if (todo.date != null &&
                LocalDate.now().isEqual(LocalDate.parse(todo.date)))
                println("\n\n\n\n" + todo.date)
                todayList.add(todo)
        }


        binding.recToday.layoutManager = LinearLayoutManager(parentActivity)
        binding.recToday.adapter = TodayAdapter(todayList)

        return binding.root
    }

    override fun onPause() {
        //todo: make mvvm work correctly
        super.onPause()
    }

}