package com.cascer.weatherappcompose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cascer.weatherappcompose.domain.Weather

@Composable
fun WeatherCard(modifier: Modifier = Modifier, weather: Weather) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Row(
            modifier = modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weather.forecasts.size.toString(),
                modifier = Modifier.padding(end = 12.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Column(
                modifier = modifier.weight(2f)
            ) {
                Text(
                    text = weather.name,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row {
                    Text(
                        modifier = modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        text = "min: ${weather.main.tempMinFormatted}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        modifier = modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = "max: ${weather.main.tempMaxFormatted}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            Column(
                modifier = modifier.weight(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = weather.main.tempFormatted,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png",
                    contentDescription = "Icon",
                )
            }
        }
    }
}