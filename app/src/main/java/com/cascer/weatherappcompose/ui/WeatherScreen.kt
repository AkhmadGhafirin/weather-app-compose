package com.cascer.weatherappcompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cascer.weatherappcompose.presentation.WeatherUiState
import com.cascer.weatherappcompose.presentation.WeatherViewModel
import com.cascer.weatherappcompose.ui.component.TabCard
import com.cascer.weatherappcompose.ui.component.WeatherList
import com.cascer.weatherappcompose.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val weatherUiState by viewModel.weatherUiState.collectAsStateWithLifecycle()

    val selectedTab = remember { mutableStateOf("Clear") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Weather App",
                        maxLines = 1,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Purple40
                )
            )
        }, content = {
            val contentModifier = modifier
                .padding(it)
                .fillMaxSize()

            Column(
                modifier = contentModifier
            ) {
                LazyRow(
                    verticalAlignment = Alignment.Top,
                    modifier = modifier
                        .padding(start = 6.dp, end = 6.dp)
                ) {
                    items(weatherUiState.tabs) { tab ->
                        TabCard(
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                            title = tab,
                            cardColor = if (selectedTab.value == tab) {
                                Color.Blue
                            } else {
                                Color.White
                            },
                            borderColor = if (selectedTab.value == tab) {
                                Color.Blue
                            } else {
                                Color.Black
                            },
                            titleColor = if (selectedTab.value == tab) {
                                Color.White
                            } else {
                                Color.Black
                            },
                            onTabClick = {
                                selectedTab.value = tab
                                viewModel.filterByTab(tab)
                            }
                        )
                    }
                }

                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    if (weatherUiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        when (weatherUiState) {
                            is WeatherUiState.HasWeather -> {
                                WeatherList(
                                    items = (weatherUiState as WeatherUiState.HasWeather).weatherList,
                                )
                            }

                            is WeatherUiState.NoWeather -> {
                                if (weatherUiState.failed.isEmpty()) {
                                    Text("List Weather Empty",)
                                } else {
                                    Text(weatherUiState.failed,)
                                }
                            }
                        }
                    }
                }
            }
        })
}