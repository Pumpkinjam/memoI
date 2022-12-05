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
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.util.ArrayList
import kotlin.concurrent.thread

class TodayListFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var todayList: ArrayList<Todo>

    var binding: FragmentTodayListBinding? = null
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
        todayList = vm.getTodayList()

        binding?.recToday?.layoutManager = LinearLayoutManager(parentActivity)
        binding?.recToday?.adapter = TodoAdapter(todayList)

        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        thread(start=true) {

            while (!vm.isReady);
            //setRecyclerView()

            todayList = vm.getTodayList()

            activity?.runOnUiThread {
                binding?.recToday?.layoutManager = LinearLayoutManager(parentActivity)
                binding?.recToday?.adapter = TodoAdapter(todayList)
            }
            // debugging: for check
            for (t in todayList) {
                println(t)
            }
        }

    }


}