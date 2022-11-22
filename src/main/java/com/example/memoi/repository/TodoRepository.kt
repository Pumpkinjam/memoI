package com.example.memoi.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TodoRepository {
    val database = Firebase.database

    fun observeCsv(key: String, todoCsv: MutableLiveData<String>) {

        val csvRef = database.getReference("list/$key")

        csvRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                todoCsv.postValue(snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to get value")
            }
        })
    }


    fun postCsv(key: String, newCsv: String) {
        val csvRef =
            database.getReferenceFromUrl(
                "https://memoi-bced4-default-rtdb.firebaseio.com/list/$key")
        //val csvRef = database.getReference("list/$key")
        csvRef.setValue(newCsv)
    }

    fun removeCsv(key: String) {
        database.getReference("list/$key").removeValue()
    }

}