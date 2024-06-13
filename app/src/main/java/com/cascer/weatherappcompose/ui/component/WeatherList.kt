package com.cascer.weatherappcompose.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cascer.weatherappcompose.domain.Weather

@Composable
fun WeatherList(
    modifier: Modifier = Modifier,
    items: List<Weather>,
) {
    Column {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(items) { weather ->
                WeatherCard(
                    modifier = modifier.fillMaxWidth(),
                    weather = weather
                )
            }
        }
    }
}