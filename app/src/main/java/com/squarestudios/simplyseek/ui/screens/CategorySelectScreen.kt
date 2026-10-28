package com.squarestudios.simplyseek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechTileBackground = Color(0xFF1B2030)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

@Composable
fun CategorySelectScreen(
    onStartGame: (category: String, difficulty: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categories = listOf("Technology", "Animals", "Science", "Space")
    val difficulties = listOf("Easy", "Medium", "Hard")

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedDifficulty by remember { mutableStateOf(difficulties[1]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "SELECT CATEGORY",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TechButtonNeon,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Categories List
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) TechButtonNeon.copy(alpha = 0.2f) else TechTileBackground)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) TechButtonNeon else TechButtonNeon.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .clickable { selectedCategory = category }
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = category,
                    color = if (isSelected) TechButtonNeon else TechTextCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "DIFFICULTY",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TechButtonNeon,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Difficulty Selector Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(0.85f),
        ) {
            difficulties.forEach { diff ->
                val isSelected = diff == selectedDifficulty
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TechButtonNeon else TechTileBackground)
                        .clickable { selectedDifficulty = diff }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = diff,
                        color = if (isSelected) Color.Black else TechTextCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onStartGame(selectedCategory, selectedDifficulty) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = "START PUZZLE",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectScreenPreview() {
    CategorySelectScreen(onStartGame = { _, _ -> })
}
