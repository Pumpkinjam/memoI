package com.example.memoi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memoi.databinding.FragmentMainBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.util.ArrayList

class MainFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var parentActivity: MainActivity
    lateinit var todoList: ArrayList<Todo>

    lateinit var binding: FragmentMainBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        todoList = vm.getList()

        childFragmentManager.beginTransaction().run {
            replace(binding.frgNav.id, TodoListFragment())
            commit()
        }

        binding.btnAddNew.setOnClickListener {
            // todo : save all lists
            parentActivity.goToFragment(AddNewFragment())
            println(vm.getList())
        }


        return binding.root
    }

}