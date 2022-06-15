package com.example.vamsemka


import CitySK
import MainWeather
import StoreUserEmail
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.vamsemka.ui.theme.VamSemkaTheme
import getCountryCodes
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


var selectedPage by mutableStateOf(0)
var cities = mutableListOf<CitySK>()
var WeatherArr = mutableListOf<MainWeather>()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        val vm = TodoViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            VamSemkaTheme {
                TodoView(vm)
            }
        }
    }
}



@Composable
fun TodoView(vm: TodoViewModel) {


if(!cities.isEmpty()) {
/*
  for(city in cities){

      LaunchedEffect(Unit) {
          city.id?.let { vm.getTodoList(it) }
      }

  }
*/
}

    Column(){
        Menu()
        when (selectedPage) {
            0 -> {
                if(!cities.isEmpty()) {
                if (vm.errorMessage.isEmpty()) {
                    if(!vm.wasFetched){
                        WeatherCard(MainWeather())
                    }else{
                    for(weather in vm.todoList){
                    WeatherCard(weather)
                    }
                    }
                    // vm.todoList.city?.name?.let { Text(it) }
                    //  Text("IM STUFF FLUSHED")
                } else {
                    Text(vm.errorMessage)
                }
            }
            }
            1 -> {
                AddPlace(vm)
            }
            else -> {
                print("x is neither 1 nor 2")
            }
        }
    }

}

@Composable
fun AddPlace(vm: TodoViewModel) {
    val cityList = getCountryCodes(LocalContext.current);
    TopAppBar(title = { Text("") }, navigationIcon = {
        IconButton(onClick = {  selectedPage = 0}) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                modifier = Modifier,
                contentDescription = "Search"
            )
        }
    }, actions = {

    }

    )
    var findValue by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = findValue,
        onValueChange = { findValue = it },
        label = { Text("Zadaj obec") },

    )
    val foundCities= arrayListOf<CitySK>();
    if(findValue.length>2){
        for (city in cityList) {
            if(city.name?.contains(findValue) == true){
                foundCities.add(city)
            }
        }
    }
    if(!foundCities.isEmpty()){
        for (city in foundCities) {
            Row(){
            Text(

                buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                        append("${city.name}")
                    }

                }
            )
            Button(onClick = {
                cities.add(city)
                    city.id?.let { vm.getTodoList(it) }
            }) {
                    Icon(Icons.Rounded.NoteAdd, contentDescription = "Localized description")
                }
            }
        }
    }
}

@Composable
fun Menu() {

    Row(){
    Button(onClick = {
        selectedPage=1
    }) {
        Icon(Icons.Rounded.NoteAdd, contentDescription = "Localized description")
    }
        Button(onClick = {}) {
            Icon(Icons.Rounded.ViewList, contentDescription = "Localized description")
        }
        Text(

            buildAnnotatedString {

                withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                    append("$selectedPage")
                }

            }
        )
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun WeatherCard(WeatherProp: MainWeather) {

    if(WeatherProp.list.isEmpty()){
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxHeight(),


        )

    }else{
        val temperature = (WeatherProp.list[0].main?.temp?.minus(273.15))?.roundToInt()
        val simpleSunrise = SimpleDateFormat("hh:mm")
        val simpleSunset = SimpleDateFormat("HH:mm")
        val sunrise = simpleSunrise.format(WeatherProp.city?.sunrise?.times(1000) ?:0 )
        val sunset = simpleSunset.format((WeatherProp.city?.sunset?.plus(12*60*60)?.times(1000)) ?: 0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
          //  verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8),fontSize = 30.sp)
                    ) {
                        append("${WeatherProp.city?.name}")
                    }
                }
            )
            Text(

                buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 50.sp)) {
                        append("${temperature} °C")
                    }

                }
            )
            Text(

                buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                        append("${WeatherProp.list[0].weather[0].description}")
                    }

                }
            )
            Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("Teraz")
                            }

                        }
                    )
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("$temperature°")
                            }

                        }
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${WeatherProp.list[0].weather[0].icon}.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[0].dtTxt)?.substring(10,16)}")
                            }

                        }
                    )
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[1].main?.temp?.minus(273.15))?.roundToInt()}°")
                            }

                        }
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${WeatherProp.list[1].weather[0].icon}.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[2].dtTxt)?.substring(10,16)}")
                            }

                        }
                    )
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[2].main?.temp?.minus(273.15))?.roundToInt()}°")
                            }

                        }
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${WeatherProp.list[2].weather[0].icon}.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[3].dtTxt)?.substring(10,16)}")
                            }

                        }
                    )
                    Text(

                        buildAnnotatedString {

                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 20.sp)) {
                                append("${(WeatherProp.list[3].main?.temp?.minus(273.15))?.roundToInt()}°")
                            }

                        }
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${WeatherProp.list[3].weather[0].icon}.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Button(

                onClick = {
                //your onclick code here

            }) {
                Text(
                    fontSize = 20.sp,
                    text = "Predpoveď na najbližšie dni")
            }

                Row(
                    //verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                   // verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly

                ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/02d.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                            ) {
                                append("Východ slnka $sunrise")
                            }
                        }
                    )
                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Pocitovo")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].main?.feelsLike?.minus(273.15))?.roundToInt()}°")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Oblačnosť")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].clouds?.all)}%")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Rýchlosť vetra")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].wind?.speed)}km/h")
                                }
                            }
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/02n.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                            ) {
                                append("Západ slnka $sunset")
                            }
                        }
                    )

                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Vlhkosť")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].main?.humidity)}%")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Tlak")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].main?.pressure)} mbar")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 15.sp)
                                ) {
                                    append("Metrov nad morom")
                                }
                            }
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(WeatherProp.list[1].main?.seaLevel)} m")
                                }
                            }
                        )
                    }
                }
                }
            }

    }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}