package com.example.vamsemka

import MainWeather
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.vamsemka.ui.theme.VamSemkaTheme
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


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

    LaunchedEffect(Unit, block = {
        vm.getTodoList()
    })

    if (vm.errorMessage.isEmpty()) {
        WeatherCard(vm.todoList)
       // vm.todoList.city?.name?.let { Text(it) }
      //  Text("IM STUFF FLUSHED")
    } else {
        Text(vm.errorMessage)
    }

}

@Composable
fun Menu() {
    Button(onClick = {}) {
        Text(text = "+",Modifier.padding(start = 10.dp))
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun WeatherCard(WeatherProp: MainWeather) {
    if(WeatherProp.list.isEmpty()){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxHeight().fillMaxHeight(),


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
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
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
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
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
                    modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
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