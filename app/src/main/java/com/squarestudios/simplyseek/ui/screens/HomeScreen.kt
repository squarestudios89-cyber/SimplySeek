package com.squarestudios.simplyseek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

@Composable
fun HomeScreen(
    onNavigateToCategorySelect: () -> Unit,
    onNavigateToStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "SIMPLY SEEK",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TechButtonNeon,
        )

        Text(
            text = "Tech Fluid Word Search",
            fontSize = 16.sp,
            color = TechTextCyan.copy(alpha = 0.7f),
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNavigateToCategorySelect,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "PLAY GAME",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateToStats,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "STATS & PROGRESS",
                color = TechButtonNeon,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToCategorySelect = {},
        onNavigateToStats = {},
    )
}
