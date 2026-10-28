package com.squarestudios.simplyseek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.generator.WordSearchGenerator
import com.squarestudios.simplyseek.model.FoundWord
import com.squarestudios.simplyseek.ui.components.InteractiveWordSearchGrid

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

@Composable
fun GameBoardScreen(
    category: String,
    difficulty: String,
    modifier: Modifier = Modifier,
) {
    val size = when (difficulty.lowercase()) {
        "easy" -> 8
        "hard" -> 12
        else -> 10 // Medium
    }

    val sampleWords = remember(category) {
        when (category.lowercase()) {
            "technology" -> listOf("KOTLIN", "ANDROID", "COMPOSE", "ROOM")
            "animals" -> listOf("TIGER", "LION", "EAGLE", "BEAR")
            "science" -> listOf("ATOM", "CELL", "GRAVITY", "FORCE")
            else -> listOf("SPACE", "MOON", "ORBIT", "STAR")
        }
    }

    val generator = remember { WordSearchGenerator() }
    val grid = remember(size, sampleWords) {
        generator.generateGrid(size = size, words = sampleWords)
    }

    val foundWords = remember { mutableStateListOf<FoundWord>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Header Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = category.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TechButtonNeon,
            )

            Text(
                text = "[$difficulty]",
                fontSize = 16.sp,
                color = TechTextCyan,
            )
        }

        // Interactive Word Search Grid
        InteractiveWordSearchGrid(
            grid = grid,
            targetWords = sampleWords,
            foundWords = foundWords,
            onWordFound = { newFound ->
                if (!foundWords.contains(newFound)) {
                    foundWords.add(newFound)
                }
            },
        )

        // Found Progress Footer
        Column(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "FOUND: ${foundWords.size} / ${sampleWords.size}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TechButtonNeon,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Target Words: " + sampleWords.joinToString(", "),
                fontSize = 14.sp,
                color = TechTextCyan.copy(alpha = 0.8f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameBoardScreenPreview() {
    GameBoardScreen(
        category = "Technology",
        difficulty = "Medium",
    )
}
