package com.cascer.weatherappcompose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TabCard(
    modifier: Modifier = Modifier,
    title: String,
    cardColor: Color = Color.White,
    borderColor: Color = Color.Black,
    titleColor: Color = Color.Black,
    onTabClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.padding(top = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        border = BorderStroke(1.dp, borderColor),
        onClick = onTabClick
    ) {
        Text(
            text = title,
            modifier = modifier.padding(4.dp),
            color = titleColor,
            textAlign = TextAlign.Center
        )
    }
}