package com.example.dn3lsweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu"){
        composable("main_menu"){
            MainMenuButtons(navController)
        }
        composable("moreweather"){
            moreWeather(navController)
        }
        composable("search"){
            Search(navController)
        }
    }
}

//@Preview
@Composable
fun MainMenuButtons(navController: NavHostController){
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 100.dp, end = 25.dp)){
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            verticalArrangement = spacedBy(32.dp),
            horizontalAlignment = Alignment.End)
        {
            Button(onClick = { navController.navigate("moreweather") }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
                Text(text = "Daytime weather", fontSize = 20.sp)
            }

            Button(onClick = { navController.navigate("search") }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
                Text(text = "Change city", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun Search(navController: NavHostController){
    Button(onClick = { navController.popBackStack() }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
        Text(text = "this is Search \n Back", fontSize = 20.sp)
    }
}

@Composable
fun moreWeather(navController: NavHostController){
    Button(onClick = { navController.popBackStack() }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
        Text(text = "this is Mw \n Back", fontSize = 20.sp)
    }
}

data class WeatherResponse(
    val current_weather: CurrentWeather?,
    val daily: Daily?
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int,
    val windspeed: Double,
    val time: String
)

data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>
)

interface WeatherApi {
    @GET("forecast")
    fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("timezone") timezone: String = "auto"
    ): Call<WeatherResponse>
}

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherApi: WeatherApi = retrofit.create(WeatherApi::class.java)
}