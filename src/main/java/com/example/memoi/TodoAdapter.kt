package com.example.memoi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.R
import com.example.memoi.databinding.ListTodoBinding
import com.example.memoi.todo.Todo

class TodoAdapter(val todoList: ArrayList<Todo>) : RecyclerView.Adapter<TodoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.Holder {
        val binding = ListTodoBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: TodoAdapter.Holder, position: Int) {
        holder.bind(todoList.get(position))
    }

    override fun getItemCount(): Int = todoList.size

    class Holder(private val binding: ListTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with (binding) {
                //
                imgTodo.setImageResource(if (todo.getDate() == null) R.drawable.clock else R.drawable.calendar);

                txtTodoInfoTitle.text = todo.title
                txtTodoInfoDesc.text = todo.description
                txtTodoInfoDate.text = todo.date
                txtTodoInfoTime.text = todo.time


                root.setOnClickListener {
                    //TODO("can we edit this to-do?")
                }
            }
        }
    }
}