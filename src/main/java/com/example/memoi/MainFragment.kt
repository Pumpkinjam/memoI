package com.example.memoi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
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

        binding.btnAddNew.setOnClickListener {
            // todo : save all lists
            parentActivity.goToFragment(AddNewFragment())
        }

        // what...
        //val navcon = binding.frgNav.getFragment<NavHostFragment>().navController
        //binding.bottomNav.setupWithNavController(navcon)

        return binding.root
    }

}