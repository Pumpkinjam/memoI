package com.example.memoi

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.R
import com.example.memoi.databinding.ListTodoBinding
import com.example.memoi.todo.Todo
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

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

                /* time not null -> clock
                 * date not null & time null -> calender
                 * date null & time null -> check
                 */
                val imgsrc = if (todo.time != null) R.drawable.clock else
                        (if (todo.date == null) R.drawable.check else R.drawable.calendar)

                imgTodo.setImageResource(imgsrc)

                txtTodoInfoTitle.text = todo.title
                txtTodoInfoDesc.text = todo.description
                txtTodoInfoDate.text = todo.date
                txtTodoInfoTime.text = todo.time
                if (todo.url == null) {
                    btnUrlMove.visibility = View.INVISIBLE
                }
                else {
                    btnUrlMove.setOnClickListener {
                        val address = todo.url
                        binding.root.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(address)))
                    }
                }


                root.setOnClickListener {
                    // todo: hmm...
                /*
                    val tmp = AlertDialog.Builder(binding.root.context)
                    tmp.setTitle("할일 삭제하기")
                    tmp.setMessage("${todo.title}을(를) 삭제하시겠습니까?")
                 */
                }
            }
        }
    }
}