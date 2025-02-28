package com.example.e_book.data_Layer.network.repo_Impl

import android.util.Log
import com.example.e_book.common.BookCategoryModel
import com.example.e_book.common.BookModel
import com.example.e_book.common.ResultState
import com.example.e_book.domain_Layer.repo.AllBookRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AllBookRepoImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) :
    AllBookRepo {
    override fun getAllBook(): Flow<ResultState<List<BookModel>>>  = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookModel> = emptyList()
                items = snapshot.children.mapNotNull { it.getValue<BookModel>() }
//                items= snapshot.children.map { value ->
//                     value.getValue<BookModel>()!!
//                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))

            }

        }
        firebaseDatabase.reference.child("Book").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }
    }

    override fun getAllBookByCategory(category: String): Flow<ResultState<List<BookModel>>>  = callbackFlow {
        Log.d("Test Tag", "onDataChange: ${category}")

        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookModel> = emptyList()
                items = snapshot.children.filter {
                    it.getValue<BookModel>()?.bookCategory == category
                }.mapNotNull {
                    it.getValue<BookModel>()
                }
//                items= snapshot.children.map { value ->
//                     value.getValue<BookModel>()!!
//                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))

            }

        }
        firebaseDatabase.reference.child("Book").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }

    }

    override fun getAllBookCategory(): Flow<ResultState<List<BookCategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var items: List<BookCategoryModel> = emptyList()
                items = snapshot.children.mapNotNull { it.getValue<BookCategoryModel>() }
//                items= snapshot.children.map { value ->
//                     value.getValue<BookModel>()!!
//                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))

            }

        }
        firebaseDatabase.reference.child("BookCategory").addValueEventListener(valueEvent)
        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }


    }
}