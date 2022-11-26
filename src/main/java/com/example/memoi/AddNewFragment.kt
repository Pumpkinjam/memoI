package com.example.memoi

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.databinding.AddNewFragmentBinding
import com.example.memoi.databinding.ListSetTodoPropertiesBinding
import com.example.memoi.todo.Task
import com.example.memoi.todo.TodoBuilder
import com.example.memoi.viewmodel.TodoListViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.util.*

class AddNewFragment : Fragment() {

    lateinit var binding: AddNewFragmentBinding
    lateinit var parentActivity: MainActivity

    val vm: TodoListViewModel by activityViewModels()

    val tempTodo = TodoBuilder()

    // getting attached activity.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddNewFragmentBinding.inflate(inflater, container, false)

        binding.recNewProperties.layoutManager = LinearLayoutManager(parentActivity)
        binding.recNewProperties.adapter = PropertyListAdapter(parentActivity, this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            //vm.setDoSave(false)
            parentActivity.exitFragment()
        }

        binding.btnConfirm.setOnClickListener {
            println("Confirm button clicked")
            //vm.setDoSave(true)
            try {
                vm.add(tempTodo.build())
                parentActivity.exitFragment()
            }
            catch (e: Task.NullIntegrityException) {
                println("?")
                Snackbar.make(this.requireView(),
                    "타이틀을 지정하지 않았습니다.", Snackbar.LENGTH_SHORT)
            }
            catch (e: Exception) {
                System.err.println("Something's wrong... in AddNewFragment.onViewCreated")
                e.printStackTrace()
            }
        }
    }


    private class PropertyListAdapter(val parentActivity: Activity, val currentFragment: AddNewFragment)
        : RecyclerView.Adapter<PropertyListAdapter.Holder>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyListAdapter.Holder {
            val binding = ListSetTodoPropertiesBinding.inflate(LayoutInflater.from(parent.context))
            return Holder(binding, parentActivity as MainActivity, currentFragment)
        }
        override fun onBindViewHolder(holder: PropertyListAdapter.Holder, position: Int) {
            holder.bind()
        }
        override fun getItemCount(): Int = 1

        class Holder(private val binding: ListSetTodoPropertiesBinding,
                     val parentActivity: MainActivity, val currentFragment: AddNewFragment)
            : RecyclerView.ViewHolder(binding.root)
        {

            private var calendar = Calendar.getInstance()
            private var year = calendar.get(Calendar.YEAR)
            private var month = calendar.get(Calendar.MONTH)
            private var day = calendar.get(Calendar.DAY_OF_MONTH)
            private var hour =calendar.get(Calendar.HOUR_OF_DAY)
            private var minute = calendar.get(Calendar.MINUTE)

            fun bind() {
                with (binding) {

                    inputSetTitle.addTextChangedListener(object: TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?, start: Int, count: Int, after: Int) { }

                        override fun onTextChanged(
                            s: CharSequence?, start: Int, before: Int, count: Int)
                        {
                            currentFragment.tempTodo.setTitle(s.toString())
                        }

                        override fun afterTextChanged(s: Editable?) { }
                    })

                    inputSetDescription.addTextChangedListener(object: TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?, start: Int, count: Int, after: Int) { }

                        override fun onTextChanged(
                            s: CharSequence?, start: Int, before: Int, count: Int)
                        {
                            currentFragment.tempTodo.setDescription(s.toString())
                        }

                        override fun afterTextChanged(s: Editable?) { }
                    })


                    btnSetDate.setOnClickListener {
                        val datePickerDialog =
                            DatePickerDialog.OnDateSetListener { datepicker, year, month, day ->
                                btnSetDate.text = "$year/${(month + 1)}/$day"
                                (parentActivity.binding.frmFragment.getFragment() as AddNewFragment)
                                    .tempTodo.setDate(year, month+1, day)
                            }
                        var date = DatePickerDialog(parentActivity, datePickerDialog,
                            year, month, day)
                        date.show()
                    }

                    btnSetTime.setOnClickListener {
                        val timePickerDialog =
                            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                                btnSetTime.text = "${hour}시 ${minute}분"
                                (parentActivity.binding.frmFragment.getFragment() as AddNewFragment)
                                    .tempTodo.setTime(hour, minute)
                            }
                        var time = TimePickerDialog(parentActivity, timePickerDialog,
                            hour, minute, false)
                        time.show()
                    }

                    inputUrl.addTextChangedListener(object: TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?, start: Int, count: Int, after: Int) { }

                        override fun onTextChanged(
                            s: CharSequence?, start: Int, before: Int, count: Int)
                        {
                            currentFragment.tempTodo.setUrl(s.toString())
                        }

                        override fun afterTextChanged(s: Editable?) { }
                    })

                    btnOpenMap.setOnClickListener {
                        // TODO: open MapView, set Location
                        Snackbar.make(parentActivity.binding.root, "구현중", Snackbar.LENGTH_SHORT)
                    }

                }
            }
        }

    }

}