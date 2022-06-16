package com.example.vamsemka

import CitySK
import MainWeather
import List
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.vamsemka.ui.theme.VamSemkaTheme
import getCountryCodes
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


var selectedPage by mutableStateOf(0)
var cities = mutableListOf<CitySK>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        val vm = WeatherViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            VamSemkaTheme {
                WeatherAppView(vm)
            }
        }
    }
}




// hlavny component objektu v nom sa vsetku vykresluje
@Composable
fun WeatherAppView(vm: WeatherViewModel) {
    val list = remember { cities.toMutableStateList() }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch { scrollState.scrollTo(0) }
    }
if(vm.todoList.isEmpty()) {

  for(city in list){

      city.id?.let { vm.getWeatherList(it) }

  }

}
    /*
    LaunchedEffect(Unit) {
        vm.todoList
    }*/

    Column(

    ){

        when (selectedPage) {
            0 -> {
                Menu()
                if(!cities.isEmpty()) {
                if (vm.errorMessage.isEmpty()) {
                    if(!vm.wasFetched){
                        WeatherCard(MainWeather())
                    }else{

                        Row(
                            modifier = Modifier
                                .padding(50.dp)
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.SpaceBetween

                        ){
                         for(weather in vm.todoList){
                             Column(modifier = Modifier
                                 ){
                                 WeatherCard(weather)
                             }
                         /*
                             Column(modifier = Modifier
                                 .fillMaxSize()){
                          WeatherCard(weather)
                             }
                         }*/
                         }
                        }

                    }
                    // vm.todoList.city?.name?.let { Text(it) }
                    //  Text("IM STUFF FLUSHED")
                } else {
                    Text(vm.errorMessage)
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){
                    Image(
                        painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/11n@2x.png"),
                        contentDescription = "My content description",
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        modifier = Modifier.padding(50.dp),
                        text ="\"Nebola pridaná žiadna obec. Pomocou tlačidla pravo hore ju pridáš.\"",
                        textAlign = TextAlign.Center

                   )
                }
            }
            }
            1 -> {
                AddPlace(vm)
            }
            2 -> {
                RemovePlace(vm)
            }
            else -> {
                print("x is neither 1 nor 2")
            }
        }
    }

}
//na odstranenie z mutable listu aby sa nam aplikacia potom prekreslila ked sa pocet prvkov v liste zmeni
@Composable
fun RemovePlace(vm: WeatherViewModel) {
    val list = cities.toMutableList()
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
    for ((pom, city) in list.withIndex()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(

                buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                        append("${city.name}")
                    }

                }
            )
            OutlinedButton(onClick = {
                vm.removeFromWeatherList(pom)
                cities.removeAt(pom);
                selectedPage = 0
            }) {
                Icon(Icons.Rounded.Remove, contentDescription = "Localized description")
            }

        }
    }
}
//na pridanie do mutable listu aby sa nam aplikacia potom prekreslila ked sa pocet prvkov v liste zmeni
//najskor vyhlada zadono hodnotu v liste miest ak najde tak sa zobrazi tlacitko a nazov
@Composable
fun AddPlace(vm: WeatherViewModel) {
    val cityList = getCountryCodes(LocalContext.current);
    TopAppBar(
        title = { Text("") }, navigationIcon = {
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
        modifier = Modifier.fillMaxWidth().padding(5.dp),
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
            Row(  modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
            Text(

                buildAnnotatedString {

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                        append("${city.name}")
                    }

                }
            )
            OutlinedButton(onClick = {
                     cities.add(city)
                    findValue=""
                    city.id?.let { vm.getWeatherList(it) }

            }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Localized description")
                }

            }

        }
    }

}

// horne menu kde sa mozem dostat na obrazovku zmazania alebo pridania
@Composable
fun Menu() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        OutlinedButton(
            modifier = Modifier.padding(5.dp),
            onClick = {
        selectedPage=1
    }) {
        Icon(Icons.Rounded.Add, contentDescription = "Localized description")
    }
        OutlinedButton(
            modifier = Modifier.padding(5.dp),
            onClick = {
            selectedPage=2
        }) {
            Icon(Icons.Rounded.ViewList, contentDescription = "Localized description")
        }

    }
}
// hlavna karta kde ukazuje ake je pocasie na danom mieste
@SuppressLint("SimpleDateFormat")
@Composable
fun WeatherCard(WeatherProp: MainWeather) {
    var forecastDays by remember { mutableStateOf(false) }
    if(WeatherProp.list.isEmpty()){
        Column( horizontalAlignment = Alignment.CenterHorizontally,){
        CircularProgressIndicator(

        )
        }
    }else{
        val temperature = (WeatherProp.list[0].main?.temp?.minus(273.15))?.roundToInt()
        val simpleSunrise = SimpleDateFormat("hh:mm")
        val simpleSunset = SimpleDateFormat("HH:mm")
        val sunrise = simpleSunrise.format(WeatherProp.city?.sunrise?.times(1000) ?:0 )
        val sunset = simpleSunset.format((WeatherProp.city?.sunset?.plus(12*60*60)?.times(1000)) ?: 0)


        if(!forecastDays){
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()).padding(15.dp),
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
                    forecastDays = true

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
                    horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(5.dp)
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
                    modifier = Modifier.padding(5.dp),
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
        } else{

            val foundNoon=  arrayListOf<List>()
            val foundNight = arrayListOf<List>()

            for(item in WeatherProp.list){
                if(item.dtTxt?.contains("12:00") == true){
                    foundNoon.add(item)
                }
                if(item.dtTxt?.contains("21:00") == true){
                    foundNight.add(item)
                }
            }

            val value = if (foundNoon.size > foundNight.size ) {
                foundNight.size
            } else {
                foundNoon.size
            }
            OutlinedButton(onClick = {

                forecastDays = false
            }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Localized description")
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()).padding(15.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(

                    buildAnnotatedString {

                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 25.sp)) {
                            append("Predpoveď na najbližšie dni")
                        }

                    }
                )
                Row(
                    modifier = Modifier
                        .padding(top = 100.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    for((l, item) in foundNoon.withIndex()){
                        Column(modifier = Modifier.padding(10.dp).background(Color(0xFFBDC4FE)),
                            horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                ) {
                                    append("${(item.dtTxt)?.substring(5,10)}")

                                }
                            }
                        )
                            Image(
                                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${item.weather[0].icon}.png"),
                                contentDescription = "My content description",
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                    ) {
                                        append("${(item.main?.feelsLike?.minus(273.15))?.roundToInt()}°")
                                    }
                                }
                            )
                            Image(
                                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${foundNight[l].weather[0].icon}.png"),
                                contentDescription = "My content description",
                                modifier = Modifier.size(50.dp)
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(fontWeight = FontWeight.W900, fontSize = 17.sp)
                                    ) {
                                        append("${(foundNight[l].main?.feelsLike?.minus(273.15))?.roundToInt()}°")
                                    }
                                }
                            )
                            Text(

                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle( fontWeight = FontWeight.W900, fontSize = 17.sp)
                                    ) {
                                        append("${(foundNight[l].wind?.speed)}km/h")
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