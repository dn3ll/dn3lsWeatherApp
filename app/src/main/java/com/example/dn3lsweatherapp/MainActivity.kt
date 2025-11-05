package com.example.dn3lsweatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



import com.example.dn3lsweatherapp.ui.theme.gameboyButtonColor
import com.example.dn3lsweatherapp.ui.theme.gameboyFontFamily
import com.example.dn3lsweatherapp.ui.theme.gameboyScreenColor
import com.example.dn3lsweatherapp.ui.theme.gameboyScreenWrapColor
import com.example.dn3lsweatherapp.ui.theme.gameboyShellColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
            MoreWeather(navController, viewModel(), dataStoreManager)
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
            Column() {
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
                        Column(Modifier
                            .align(alignment = Alignment.TopCenter),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        )
                        {
                            if (city.cityName.isNotEmpty()) {
                                Text(modifier = Modifier
                                    .padding(top = 10.dp),
                                    fontFamily = gameboyFontFamily,
                                    text = "City: ${city.cityName}", fontSize = 22.sp)
                            }

                            when {
                                weatherData != null -> {

                                    Text(
                                        fontFamily = gameboyFontFamily,
                                        text = "Temperature: ${weatherData.current_weather?.temperature?.roundToInt()?: "-"}°C",
                                        fontSize = 18.sp
                                    )

                                    var weatherDisplayed: String
                                    var weatherPicId = R.drawable.empty
                                    when (weatherData.current_weather?.weathercode) {
                                        0 -> {
                                            weatherDisplayed = "clear"
                                            weatherPicId = R.drawable.clear
                                        }
                                        in 1..3 -> {
                                            weatherDisplayed = "cloudy"
                                            weatherPicId = R.drawable.cloudy
                                        }
                                        in 45..48 -> {
                                            weatherDisplayed = "fog"
                                            weatherPicId = R.drawable.fog
                                        }
                                        in 51..67 -> {
                                            weatherDisplayed = "rain"
                                            weatherPicId = R.drawable.rain
                                        }
                                        in 71..86 -> {
                                            weatherDisplayed = "snow"
                                            weatherPicId = R.drawable.snow
                                        }
                                        in 95..99 -> {
                                            weatherDisplayed = "tornado"
                                            weatherPicId = R.drawable.tornado
                                        }
                                        else -> weatherDisplayed = "unknown"
                                    }

                                    Text(
                                        fontFamily = gameboyFontFamily,
                                        text = "Weather: ${weatherDisplayed}",
                                        fontSize = 18.sp
                                    )

                                    Image(
                                        painter = painterResource(id = weatherPicId),
                                        contentDescription = "weatherpic",
                                        modifier = Modifier
                                            .size(180.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )


                                }

                                city.cityName.isNotEmpty() -> {
                                    Text( modifier = Modifier
                                        .offset(y=50.dp),
                                        text = "Loading...",
                                        fontFamily = gameboyFontFamily,
                                        fontSize = 20.sp
                                    )
                                }
                                else -> {
                                    Text( modifier = Modifier,
                                        fontFamily = gameboyFontFamily,
                                        text = "Select a city", fontSize = 18.sp)
                                }
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
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-65).dp, y = 10.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "More",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-106).dp, y = (-13.5).dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold

                    )

                    Button(
                        modifier = Modifier
                            .size(60.dp)
                            .align(alignment = Alignment.TopEnd),
                        onClick = { navController.navigate("search") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Button(
                        modifier = Modifier
                            .size(60.dp)
                            .align(alignment = Alignment.BottomEnd)
                            .offset(x = (-40).dp)
                            ,
                        onClick = { navController.navigate("moreWeather") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Image(
                        painter = painterResource(id = R.drawable.d_pad),
                        contentDescription = "dpad",
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
    val geocodingState = viewModel.geocodingData.collectAsState(initial = null)
    val geocodingData = geocodingState.value

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
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
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.5.dp, Color.Black, RoundedCornerShape(15.dp))
                        .background(gameboyScreenColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text(text = "Enter city", fontFamily = gameboyFontFamily) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Black
                            ),
                            singleLine = true,
                            textStyle = TextStyle(fontFamily = gameboyFontFamily)
                        )

                        OutlinedButton(
                            onClick = {
                                val q = query.trim()
                                if (q.isNotEmpty()) {
                                    Log.d("AddCityScreen", "Search clicked: '$q'")
                                    viewModel.fetchGeocoding(q)
                                } else {
                                    Log.d("AddCityScreen", "Empty query - ignoring")
                                }
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RectangleShape,
                            border = BorderStroke(1.dp, Color.Black)
                        ) {
                            Text(text = "Search", fontFamily = gameboyFontFamily,
                                color = Color.Black)
                        }

                        if (geocodingData != null) {
                            val snapshot = geocodingData
                            val result = snapshot.results?.firstOrNull()
                            if (result != null) {
                                Text(text = "Found: ${result.name}",
                                    fontFamily = gameboyFontFamily)
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            dataStoreManager.saveData(
                                                CityData(result.name, result.latitude, result.longitude)
                                            )
                                        }
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(50.dp)
                                        .align(Alignment.CenterHorizontally),
                                    shape = RectangleShape,
                                    border = BorderStroke(1.dp, Color.Black)
                                ) {
                                    Text(text = "Save city", fontFamily = gameboyFontFamily, color = Color.Black)
                                }
                            } else {
                                Text(text = "City not found", fontFamily = gameboyFontFamily, color = Color.Black)
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 15.dp, end = 30.dp)
                    .height(140.dp)
            )
            {
                Text(
                    text = "Back",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-65).dp, y = 10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    modifier = Modifier
                        .size(60.dp)
                        .align(alignment = Alignment.TopEnd),
                    onClick = { navController.popBackStack() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                ) {

                }

                Button(
                    modifier = Modifier
                        .size(60.dp)
                        .align(alignment = Alignment.BottomEnd)
                        .offset(x = (-40).dp),
                    onClick = {/**/ },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                ) {

                }

                Image(
                    painter = painterResource(id = R.drawable.d_pad),
                    contentDescription = "dpad",
                    modifier = Modifier
                        .size(140.dp)
                        .align(alignment = Alignment.CenterStart)
                )
            }
        }
    }
}









    @Composable
    fun MoreWeather(
        navController: NavHostController,
        viewModel: WeatherViewModel = viewModel(),
        dataStoreManager: DataStoreManager
    ) {
        val city by dataStoreManager.getData()
            .collectAsState(initial = CityData("", 0.0, 0.0))
        val weatherData = viewModel.weatherData.collectAsState().value

        Box(
            modifier = Modifier
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
                        Text(
                            modifier = Modifier
                                .align(alignment = Alignment.TopCenter)
                                .padding(top = 10.dp),
                            fontFamily = gameboyFontFamily,
                            text = "Coming soon", fontSize = 22.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, start = 15.dp, end = 30.dp)
                        .height(140.dp)

                )
                {
                    Text(
                        text = "Back",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-65).dp, y = 10.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )



                    Button(
                        modifier = Modifier
                            .size(60.dp)
                            .align(alignment = Alignment.TopEnd),
                        onClick = { navController.popBackStack() },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Button(
                        modifier = Modifier
                            .size(60.dp)
                            .align(alignment = Alignment.BottomEnd)
                            .offset(x = (-40).dp),
                        onClick = {/**/ },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = gameboyButtonColor)
                    ) {

                    }

                    Image(
                        painter = painterResource(id = R.drawable.d_pad),
                        contentDescription = "dpad",
                        modifier = Modifier
                            .size(140.dp)
                            .align(alignment = AbsoluteAlignment.CenterLeft)
                    )
                }


            }
        }
    }

