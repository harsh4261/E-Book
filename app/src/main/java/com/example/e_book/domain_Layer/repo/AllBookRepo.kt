package com.example.e_book.domain_Layer.repo

import com.example.e_book.common.BookCategoryModel
import com.example.e_book.common.BookModel
import com.example.e_book.common.ResultState
import kotlinx.coroutines.flow.Flow

interface AllBookRepo {

    fun getAllBook() : Flow<ResultState<List<BookModel>>>
    fun getAllBookCategory() : Flow<ResultState<List<BookCategoryModel>>>
    fun getAllBookByCategory(category: String):  Flow<ResultState<List<BookModel>>>


}