package com.example.memoi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.memoi.databinding.FragmentMainTodoBinding
import com.example.memoi.viewmodel.TodoListViewModel

class MainTodoFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()

    var binding: FragmentMainTodoBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainTodoBinding.inflate(inflater, container, false);

        binding?.btnAddNew?.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.action_mainTodoFragment_to_addNewFragment)
        }

        (activity as MainActivity).showTray()

        return binding?.root
    }

}