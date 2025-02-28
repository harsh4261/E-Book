package com.example.e_book.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_book.common.BookCategoryModel
import com.example.e_book.common.BookModel
import com.example.e_book.common.ResultState
import com.example.e_book.domain_Layer.repo.AllBookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(val  repo : AllBookRepo) : ViewModel() {
    private val _state : MutableState<ItemsState> = mutableStateOf(ItemsState())

    val state : MutableState<ItemsState> = _state

    init {
        viewModelScope.launch {
            repo.getAllBookCategory().collect{
                when (it) {
                    is ResultState.Error -> {
                        _state.value = ItemsState(error = it.exception.localizedMessage)
                    }

                    ResultState.Loading -> {
                        _state.value = ItemsState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _state.value = ItemsState(category = it.data)
                        Log.d("TAG", "getAllBookCategoryData: ${it.data}")
                    }
                }
            }
        }

    }

    fun getAllBookCategoryData(){
        viewModelScope.launch {
            repo.getAllBookCategory().collect{
                when (it) {
                    is ResultState.Error -> {
                        _state.value = ItemsState(error = it.exception.localizedMessage)
                    }

                    ResultState.Loading -> {
                        _state.value = ItemsState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _state.value = ItemsState(category = it.data)
                        Log.d("TAG", "getAllBookCategoryData: ${it.data}")
                    }

                }
            }
        }
    }

    fun getAllBooks(){
        viewModelScope.launch {
            repo.getAllBook().collect {
                when (it) {
                    is ResultState.Error -> {
                        _state.value = ItemsState(error = it.exception.localizedMessage)
                    }

                    ResultState.Loading -> {
                        _state.value = ItemsState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _state.value = ItemsState(items = it.data)
                    }

                }
            }
        }
    }

    fun getAllBooksByCategory(category : String)  {
        viewModelScope.launch {
            repo.getAllBookByCategory(category).collect {
                when (it) {
                    is ResultState.Error -> {
                        _state.value = ItemsState(error = it.exception.localizedMessage)
                    }

                    ResultState.Loading -> {
                        _state.value = ItemsState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _state.value = ItemsState(items = it.data)

                    }
                }
            }
        }
    }

}

data class ItemsState(
    val items : List<BookModel> = emptyList(),
    val category : List<BookCategoryModel> = emptyList(),
    val error : String = "",
    val isLoading : Boolean = false
    )