package com.example.weatherapp.presentation.ui.home


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.domain.model.WeatherInfo
import com.example.weatherapp.presentation.ui.theme.CloudyGradientEnd
import com.example.weatherapp.presentation.ui.theme.CloudyGradientStart
import com.example.weatherapp.presentation.ui.theme.NightGradientEnd
import com.example.weatherapp.presentation.ui.theme.NightGradientStart
import com.example.weatherapp.presentation.ui.theme.RainyGradientEnd
import com.example.weatherapp.presentation.ui.theme.RainyGradientStart
import com.example.weatherapp.presentation.ui.theme.SnowyGradientEnd
import com.example.weatherapp.presentation.ui.theme.SnowyGradientStart
import com.example.weatherapp.presentation.ui.theme.SunnyGradientEnd
import com.example.weatherapp.presentation.ui.theme.SunnyGradientStart
import com.example.weatherapp.presentation.ui.theme.TextDark
import com.example.weatherapp.presentation.ui.theme.TextLight
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: WeatherViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Determine which gradient to use based on weather
    val backgroundGradient = uiState.weatherInfo?.let { weather ->
        getWeatherGradient(weather.description)
    } ?: Brush.verticalGradient(
        colors = listOf(SunnyGradientStart, SunnyGradientEnd)
    )

    // Fetch weather on first load
    LaunchedEffect(Unit) {
        viewModel.searchWeather()
    }

    // Main container with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            )
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::refreshWeather
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Search bar
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = viewModel::onSearchQueryChanged,
                    onSearch = viewModel::searchWeather
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(top = 100.dp)
                    )
                    Text(
                        text = "Fetching weather data...",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                uiState.error?.let { error ->
                    ErrorCard(error)
                }

                uiState.weatherInfo?.let { weather ->
                    WeatherContent(weather)
                }
            }
        }

    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Search city...",
                color = Color.White.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Text(
                    text = "🔍",
                    fontSize = 24.sp
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color.White.copy(alpha = 0.8f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun WeatherContent(weather: WeatherInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.animateContentSize()
    ) {
        // City name
        Text(
            text = weather.cityName,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Weather emoji
        Text(
            text = weather.getWeatherEmoji(),
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature
        Text(
            text = weather.getFormattedTemp(),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Weather description
        Text(
            text = weather.description.replaceFirstChar { it.uppercase() },
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.9f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Feels like
        Text(
            text = "Feels like ${weather.feelsLike.toInt()}°",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Weather details card
        WeatherDetailsCard(weather)
    }
}

@Composable
fun WeatherDetailsCard(weather: com.example.weatherapp.domain.model.WeatherInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDetailItem(
                    label = "Humidity",
                    value = "${weather.humidity}%",
                    icon = "💧"
                )
                WeatherDetailItem(
                    label = "Wind Speed",
                    value = "${weather.windSpeed} m/s",
                    icon = "💨"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDetailItem(
                    label = "Pressure",
                    value = "${weather.pressure} hPa",
                    icon = "🌡️"
                )
                WeatherDetailItem(
                    label = "Visibility",
                    value = "${weather.visibility / 1000} km",
                    icon = "👁️"
                )
            }
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = icon,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ErrorCard(error: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "😕",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                fontSize = 14.sp,
                color = TextLight
            )
        }
    }
}

@Composable
fun getWeatherGradient(weatherDescription: String): Brush {
    val description = weatherDescription.lowercase()

    return when {
        description.contains("clear") -> {
            Brush.verticalGradient(
                colors = listOf(SunnyGradientStart, SunnyGradientEnd)
            )
        }
        description.contains("cloud") -> {
            Brush.verticalGradient(
                colors = listOf(CloudyGradientStart, CloudyGradientEnd)
            )
        }
        description.contains("rain") || description.contains("drizzle") -> {
            Brush.verticalGradient(
                colors = listOf(RainyGradientStart, RainyGradientEnd)
            )
        }
        description.contains("snow") -> {
            Brush.verticalGradient(
                colors = listOf(SnowyGradientStart, SnowyGradientEnd)
            )
        }
        description.contains("thunder") -> {
            Brush.verticalGradient(
                colors = listOf(NightGradientStart, NightGradientEnd)
            )
        }
        else -> {
            // Default fallback
            Brush.verticalGradient(
                colors = listOf(SunnyGradientStart, SunnyGradientEnd)
            )
        }
    }
}

