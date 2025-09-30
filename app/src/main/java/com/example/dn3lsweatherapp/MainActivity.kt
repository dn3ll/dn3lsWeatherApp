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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val DataStoreManager = DataStoreManager(this)
        setContent {
            AppNavigation(DataStoreManager)
        }
    }
}

@Composable
fun AppNavigation(dataStoreManager: DataStoreManager){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu"){
        composable("main_menu"){
            MainMenuButtons(navController, viewModel(), dataStoreManager)
        }
        composable("moreWeather"){
            moreWeather(navController)
        }
        composable("search"){
            Search(navController, viewModel(), dataStoreManager)
        }
    }
}

@Composable
fun MainMenuButtons(navController: NavHostController, viewModel: WeatherViewModel = viewModel(), dataStoreManager: DataStoreManager){

    val city by dataStoreManager.getData()
        .collectAsState(initial = CityData("", 0.0, 0.0))

    val weatherData = viewModel.weatherData.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(city.latitude, city.longitude)
    }

    if (weatherData != null) {
        Text(text = "Temperature: ${weatherData.current_weather?.temperature}°C")
    } else {
        Text(text = "Loading...")
    }

    Box(modifier = Modifier.fillMaxSize().padding(bottom = 100.dp, end = 25.dp)){
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            verticalArrangement = spacedBy(32.dp),
            horizontalAlignment = Alignment.End)
        {
            Button(onClick = { navController.navigate("moreWeather") }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
                Text(text = "Daytime weather", fontSize = 20.sp)
            }

            Button(onClick = { navController.navigate("search") }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
                Text(text = "Change city", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun Search(navController: NavHostController, viewModel: GeocodingViewModel = viewModel(), dataStoreManager: DataStoreManager){
    var query by remember { mutableStateOf("") }
    val geocodingData = viewModel.geocodingData.collectAsState().value
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp, start = 25.dp)){
        Column(
            modifier = Modifier.align(Alignment.TopStart), 
            verticalArrangement = spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(text = "Enter city name") }
            )
            Button(
                onClick = { viewModel.fetchGeocoding(query)
                    scope.launch {
                        dataStoreManager.saveData(CityData(
                            cityName = geocodingData?.results?.firstOrNull()?.name ?: "",
                            latitude = geocodingData?.results?.firstOrNull()?.latitude ?: 0.0,
                            longitude = geocodingData?.results?.firstOrNull()?.longitude ?: 0.0
                        ))
                    }
                          },
                modifier = Modifier.size(width = 200.dp, height = 100.dp)
            ) {
                Text(text = "Search", fontSize = 20.sp)
            }
            if (geocodingData != null) {
                Text(
                    text = "City: ${geocodingData?.results?.firstOrNull()?.name ?: "-"}"
                )
            } else {
                Text(text = "Loading...")
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(width = 200.dp, height = 100.dp)
            ) {
                Text(text = "this is Search \n Back", fontSize = 20.sp)
            }

        }
    }
}

@Composable
fun moreWeather(navController: NavHostController){
    Button(onClick = { navController.popBackStack() }, modifier = Modifier.size(width=200.dp, height = 100.dp)) {
        Text(text = "this is Mw \n Back", fontSize = 20.sp)
    }
}


