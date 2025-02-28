package com.example.e_book.data_Layer.network

import com.example.e_book.data_Layer.models.CategoryDTO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import javax.inject.Inject

class GetAllCategory @Inject constructor(val firebaseDatabase: FirebaseDatabase) {
    fun getAllCategory(){
        
        val  categoryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val  category = dataSnapshot.getValue<CategoryDTO>()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        firebaseDatabase.reference.child("BookCategory").addValueEventListener(categoryListener)

    }

}