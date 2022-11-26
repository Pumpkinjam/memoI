package com.example.memoi.repository

import androidx.lifecycle.MutableLiveData
import com.example.memoi.todo.Todo
import com.example.memoi.todo.TodoBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.InvocationTargetException
import kotlin.collections.HashMap

interface Retro {
    @GET("list")
    fun getList(): Call<JSONArray>
}

class TodoRepository {

    private val database = Firebase.database
    private val fbUrl = "https://memoi-bced4-default-rtdb.firebaseio.com/"
    private val retrofit: Retrofit? = Retrofit.Builder().baseUrl(fbUrl).build()
    val retrofitApi = retrofit?.create(Retro::class.java)

    suspend fun readList()/*: JSONArray?*/ {

        withContext(Dispatchers.IO) {
            retrofitApi?.getList()?.enqueue(object : Callback<JSONArray> {
                override fun onResponse(call: Call<JSONArray>, response: Response<JSONArray>) {
                    val json: JSONArray? = response.body()
                    println(json)
                }

                override fun onFailure(call: Call<JSONArray>, t: Throwable) {
                    println("f")
                }

            })
        }

    }

    fun observeTodo(key: String, todoData: MutableLiveData<Todo>) {

        val todoRef = database.getReference("list/$key")

        todoRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                // snapshot.value becomes java.util.HashMap
                val hm = snapshot.value as? HashMap<*, *>

                // initializing arrayList for TodoBuilder
                val arr = ArrayList<String?>(7)
                for (i in 0..7) {
                    arr.add(null)
                }

                if (hm == null) println("Why???")
                // todo : solve null-cast problem
                for (mapKey in hm!!.keys) {
                    //arr.add(java.lang.String.valueOf(hm[key]))
                    val idx = when (mapKey) {
                        "created" -> 0
                        "title" -> 1
                        "description" -> 2
                        "date" -> 3
                        "time" -> 4
                        "location" -> 5
                        else -> 6   // index for garbage data (ex. timestamp)
                    }
                    arr[idx] = (hm[mapKey])?.toString()
                }

                // construct with ArrayList
                val res = TodoBuilder(arr)
                todoData.postValue(res.build())
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get value")
            }
        })

    }

    fun postTodo(newTodo: Todo) {
        val todoRef = database.getReference("list/${newTodo.created}")
        //val csvRef = database.getReference("list/$key")
        println("posting Todo...")

        try {
            todoRef.setValue(newTodo)
        }
        catch (e: InvocationTargetException) {
            e.targetException.printStackTrace()
            return;
        }
        catch (e1: DatabaseException) {
            e1.printStackTrace()
            return;
        }
    }

    fun removeTodo(key: String) {
        database.getReference("list/$key").removeValue()
    }

    fun removeTodo(todo: Todo) {
        database.getReference("list/${todo.created}").removeValue()
    }

}