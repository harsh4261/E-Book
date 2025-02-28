package com.example.e_book.common

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error<T>(val  exception:  Throwable) : ResultState<T>()
    data object Loading : ResultState<Nothing>()

}

data class BookModel(
    val bookName : String="",
    val bookUrl : String="",
    val bookCategory : String =""
)

data class BookCategoryModel(
    val Name : String = ""
)