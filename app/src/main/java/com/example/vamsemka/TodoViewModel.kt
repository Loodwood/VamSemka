package com.example.vamsemka

import MainWeather
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val emptyWeather = MainWeather()
    private var _todoList = mutableListOf<MainWeather>()

    var wasFetched: Boolean by mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")
    val todoList: MutableList<MainWeather>
        get() = _todoList


    fun getTodoList(id: Int) {

        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _todoList.add(apiService.getWeather(id))
                wasFetched = true;
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}