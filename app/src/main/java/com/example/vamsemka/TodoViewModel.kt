package com.example.vamsemka

import MainWeather
import Todo
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val emptyWeather = MainWeather()
    private var _todoList: MainWeather by mutableStateOf(emptyWeather)
    var errorMessage: String by mutableStateOf("")
    val todoList: MainWeather
        get() = _todoList

    fun getTodoList() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _todoList=apiService.getWeather()


            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}