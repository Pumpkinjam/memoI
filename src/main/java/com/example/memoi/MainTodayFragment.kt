package com.example.memoi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.memoi.databinding.FragmentMainTodayBinding
import com.example.memoi.viewmodel.TodoListViewModel

class MainTodayFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()

    var binding: FragmentMainTodayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainTodayBinding.inflate(inflater, container, false);

        binding?.btnAddNew?.setOnClickListener {
            // todo : save all lists
            (activity as MainActivity).goToFragment(AddNewFragment())
        }

        (activity as MainActivity).showTray()

        return binding?.root
    }

}