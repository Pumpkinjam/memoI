package com.example.memoi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.memoi.databinding.FragmentMainBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.io.IOException
import java.util.*

class MainFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var parentActivity: MainActivity
    lateinit var todoList: ArrayList<Todo>

    lateinit var binding: FragmentMainBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as MainActivity
        todoList = ArrayList<Todo>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        //todoList = vm.getList()
        vm.retrieveTodoList()

        childFragmentManager.beginTransaction().run {
            replace(binding.frmTodoList.id, TodoListFragment())
            commit()
        }

        binding.btnAddNew.setOnClickListener {
            parentActivity.goToFragment(AddNewFragment())
            //println(vm.getList())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //vm.getTodo("test")
        //vm.getListFromRepo(ArrayList<String>())
        this.todoList
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        saveKeyList()
        super.onPause()
    }

    private fun saveKeyList() {
        // TODO: imagine that what happens if the saving progress fails...
        // TODO: we need something more safe way
        try {
            // must save " " at the end of the file!!
            var str = ""
            for (td in todoList) str += td.created + " ";
            str += "\n"

            parentActivity.openFileOutput("keyList.sav", Context.MODE_PRIVATE).write(str.toByteArray())

            println("keyList save completed.")
        }
        catch (e: IOException) {
            System.err.println("keyList save failure.")
            e.printStackTrace()
        }
    }

    private fun loadKeyList() {

        val keys: ArrayList<String> = ArrayList<String>()

        println("=================")

        if ("keyList.sav" in parentActivity.fileList())
            println("keyList.sav found!")
        else
            parentActivity.openFileOutput("keyList.sav", Context.MODE_PRIVATE).use {
                println("Creating new file...")
                it.write(" ".toByteArray())
            }

        val line = parentActivity.openFileInput("keyList.sav").bufferedReader().readLine()
        val st = StringTokenizer(line)

        while (st.hasMoreTokens()) {
            val tmpKey = st.nextToken()
            println(tmpKey)
            //this.todoList.add(vm.getTodo(tmpKey))
        }
        //vm.setList(todoList)
        println("----------------\nload complete.\n=================")


    }

}