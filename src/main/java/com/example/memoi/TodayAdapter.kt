/*
package com.example.memoi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.databinding.ListTodayBinding
import com.example.memoi.todo.Todo

class TodayAdapter(val todoList: ArrayList<Todo>) : RecyclerView.Adapter<TodayAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayAdapter.Holder {
        val binding = ListTodayBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: TodayAdapter.Holder, position: Int) {
        holder.bind(todoList.get(position))
    }

    override fun getItemCount(): Int = todoList.size

    class Holder(private val binding: ListTodayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with (binding) {
                txtTodayInfoTitle.text = todo.title
                txtTodayInfoDesc.text = todo.description
                txtTodayInfoDate.text = todo.date
                txtTodayInfoTime.text = todo.time

                if (todo.url == null) {
                    btnUrlMove.visibility = View.INVISIBLE
                }
                else {
                    btnUrlMove.setOnClickListener {
                        val address = todo.url
                        (binding.root.context as Activity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(address)))
                    }
                }

            }
        }
    }
}*/
