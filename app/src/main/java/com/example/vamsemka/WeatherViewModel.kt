package com.example.vamsemka

import MainWeather
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//sluzi na ulozenie objektov ktore nam pridu na odpoved od api
class WeatherViewModel : ViewModel() {
    private var _weatherList = mutableListOf<MainWeather>()

    var wasFetched: Boolean by mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")
    val todoList: MutableList<MainWeather>
        get() = _weatherList

    fun removeFromWeatherList(id: Int){
        _weatherList.removeAt(id);
    }

    fun getWeatherList(id: Int) {

        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _weatherList.add(apiService.getWeather(id))
                wasFetched = true;
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

}