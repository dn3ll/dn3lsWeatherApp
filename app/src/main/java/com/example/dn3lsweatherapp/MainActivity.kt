package com.example.dn3lsweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
        val dataStoreManager = DataStoreManager(this)
        setContent {
            AppNavigation(dataStoreManager)
        }
    }
}

@Composable
fun AppNavigation(dataStoreManager: DataStoreManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenuButtons(navController, viewModel(), dataStoreManager)
        }
        composable("moreWeather") {
            moreWeather(navController)
        }
        composable("search") {
            Search(navController, viewModel(), dataStoreManager)
        }
    }
}

@Composable
fun MainMenuButtons(
    navController: NavHostController,
    viewModel: WeatherViewModel = viewModel(),
    dataStoreManager: DataStoreManager
) {
//    val city by dataStoreManager.getData()
//        .collectAsState(initial = CityData("", 0.0, 0.0))
//    val weatherData = viewModel.weatherData.collectAsState().value
//    val paperyellow = Color(0xFFe4dbba)
//    LaunchedEffect(city) {
//        if (city.cityName.isNotEmpty()) {
//            viewModel.fetchWeather(city.latitude, city.longitude)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(paperyellow)
//        ,
//        verticalArrangement = spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        if (city.cityName.isNotEmpty()) {
//            Text(text = "City: ${city.cityName}", fontSize = 22.sp)
//        }
//
//
//        when {
//            weatherData != null -> {
//                Text(
//                    text = "Temperature: ${weatherData.current_weather?.temperature ?: "-"}°C",
//                    fontSize = 20.sp
//                )
//            }
//
//            city.cityName.isNotEmpty() -> {
//                Text(
//                    text = "Loading weather for ${city.cityName}...",
//                    fontSize = 18.sp
//                )
//            }
//
//            else -> {
//                Text(text = "Select a city", fontSize = 18.sp)
//            }
//        }
//
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 100.dp, end = 25.dp)
//        ) {
//            Column(
//                modifier = Modifier.align(Alignment.BottomEnd),
//                verticalArrangement = spacedBy(32.dp),
//                horizontalAlignment = Alignment.End
//            ) {
//                Button(
//                    onClick = { navController.navigate("moreWeather") },
//                    modifier = Modifier.size(width = 200.dp, height = 100.dp)
//                ) {
//                    Text(text = "Daytime weather", fontSize = 20.sp)
//                }
//
//                Button(
//                    onClick = { navController.navigate("search") },
//                    modifier = Modifier.size(width = 200.dp, height = 100.dp)
//                ) {
//                    Text(text = "Change city", fontSize = 20.sp)
//                }
//            }
//        }
//    }
        val gameboyShellColor = Color(0xFFc3c2bb)
        val gameboyScreenWrapColor = Color(0xFF565661)
        val gameboyScreenColor = Color(0xFFcadc9f)
        val gameboyButtonColor = Color(0xFF7d0744)
        val gameboyFontFamily = FontFamily(
            Font(R.font.gameboyfont)
        )

        val city by dataStoreManager.getData()
            .collectAsState(initial = CityData("", 0.0, 0.0))
        val weatherData = viewModel.weatherData.collectAsState().value

        LaunchedEffect(city) {
            if (city.cityName.isNotEmpty()) {
                viewModel.fetchWeather(city.latitude, city.longitude)
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(gameboyShellColor)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 70.dp)
                        .clip(RoundedCornerShape(bottomEnd = 75.dp))
                        .height(400.dp)
                        .background(gameboyScreenWrapColor)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .border(2.5.dp, Color.Black, RoundedCornerShape(15.dp))
                            .background(gameboyScreenColor)
                    ) {
                                if (city.cityName.isNotEmpty()) {
                                    Text(modifier = Modifier
                                        .align(alignment = Alignment.TopCenter)
                                        .padding(top = 10.dp),
                                        fontFamily = gameboyFontFamily,
                                        text = "City: ${city.cityName}", fontSize = 22.sp)
                                }

                                when {
                                weatherData != null -> {
                                    Text(modifier = Modifier
                                        .align(alignment = Alignment.TopCenter)
                                        .offset(y=50.dp),
                                        fontFamily = gameboyFontFamily,
                                        text = "Temperature: ${weatherData.current_weather?.temperature ?: "-"}°C",
                                        fontSize = 16.sp
                                    )
                                }

                                city.cityName.isNotEmpty() -> {
                                    Text( modifier = Modifier.align(alignment = Alignment.TopCenter),
                                        text = "Loading weather for ${city.cityName}...",
                                        fontFamily = gameboyFontFamily,
                                        fontSize = 18.sp
                                    )
                                }

                                else -> {
                                    Text( modifier = Modifier.align(alignment = Alignment.TopCenter),
                                        fontFamily = gameboyFontFamily,
                                        text = "Select a city", fontSize = 18.sp)
                                }
                            }


                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 15.dp, end = 30.dp)
                    .height(140.dp)

                )
                {
                    Text(
                        text = "City",
                        modifier = Modifier.align(Alignment.TopEnd)
                            .offset(x=-65.dp, y = 10.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "More",
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .offset(x=-106.dp, y = -13.5.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold

                    )

                    Button(
                        modifier = Modifier.size(60.dp)
                            .align(alignment = Alignment.TopEnd),
                        onClick = { navController.navigate("search") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Button(
                        modifier = Modifier.size(60.dp)
                            .align(alignment = Alignment.BottomEnd)
                            .offset(x = -40.dp)
                            ,
                        onClick = { navController.navigate("moreWeather") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Image(
                        painter = painterResource(id = R.drawable.d_pad),
                        contentDescription = "Описание изображения",
                        modifier = Modifier
                            .size(140.dp)
                            .align(alignment = AbsoluteAlignment.CenterLeft)
                    )
                }


            }
        }




}

@Composable
fun Search(
    navController: NavHostController,
    viewModel: GeocodingViewModel = viewModel(),
    dataStoreManager: DataStoreManager
) {
    var query by remember { mutableStateOf("") }
    val geocodingData = viewModel.geocodingData.collectAsState().value
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 25.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(text = "Enter city name") }
            )

            Button(
                onClick = { viewModel.fetchGeocoding(query) },
                modifier = Modifier.size(width = 200.dp, height = 100.dp)
            ) {
                Text(text = "Search", fontSize = 20.sp)
            }

            if (geocodingData != null) {
                val result = geocodingData.results?.firstOrNull()
                if (result != null) {
                    Text(text = "Found: ${result.name}")
                    Button(
                        onClick = {
                            scope.launch {
                                dataStoreManager.saveData(
                                    CityData(result.name, result.latitude, result.longitude)
                                )
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier.size(width = 200.dp, height = 100.dp)
                    ) {
                        Text(text = "Save city", fontSize = 20.sp)
                    }
                } else {
                    Text(text = "City not found")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(width = 200.dp, height = 100.dp)
            ) {
                Text(text = "Back", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun moreWeather(navController: NavHostController) {
    Button(
        onClick = { navController.popBackStack() },
        modifier = Modifier.size(width = 200.dp, height = 100.dp)
    ) {
        Text(text = "this is Mw \n Back", fontSize = 20.sp)
    }
}
