package com.example.memoi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.R
import com.example.memoi.databinding.ListTaskBinding
import com.example.memoi.databinding.ListTodoBinding
import com.example.memoi.todo.Task
import com.example.memoi.todo.Todo

class TaskAdapter(val taskList: ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.Holder {
        val binding = ListTaskBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: TaskAdapter.Holder, position: Int) {
        holder.bind(taskList.get(position))
    }

    override fun getItemCount(): Int = taskList.size

    class Holder(private val binding: ListTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with (binding) {
                txtTaskInfoTitle.text = task.title
                txtTaskInfoDesc.text = task.description
                txtTaskInfoDate.text = task.dateToString()
                txtTaskInfoTime.text = task.timeToString()

            }
        }
    }
}