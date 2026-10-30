package com.squarestudios.simplyseek.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.ads.AdMobBanner
import com.squarestudios.simplyseek.generator.WordSearchGenerator
import com.squarestudios.simplyseek.model.CategoryCatalog
import com.squarestudios.simplyseek.model.FoundWord
import com.squarestudios.simplyseek.ui.components.FoundWordColors
import com.squarestudios.simplyseek.ui.components.InteractiveWordSearchGrid

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)
private val TechGold = Color(0xFFFFD600)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameBoardScreen(
    category: String,
    difficulty: String,
    modifier: Modifier = Modifier,
    initialFoundWords: List<FoundWord> = emptyList(),
) {
    val size = when (difficulty.lowercase()) {
        "easy" -> 8
        "hard" -> 12
        else -> 10 // Medium
    }

    var puzzleSeed by remember { mutableIntStateOf(0) }

    val categoryInfo = remember(category) {
        CategoryCatalog.getCategoryById(category)
    }
    val categoryDisplayName = categoryInfo?.title ?: category.replace('_', ' ').uppercase()

    val sampleWords = remember(category, difficulty, puzzleSeed) {
        CategoryCatalog.getWordsForCategory(
            categoryId = category,
            difficulty = difficulty,
            seed = puzzleSeed,
        )
    }

    val generator = remember { WordSearchGenerator() }
    val gridState = remember(size, sampleWords, puzzleSeed) {
        generator.generateGridWithState(rows = size, cols = size, words = sampleWords)
    }
    val grid = gridState.grid
    val targetWords = gridState.targetWords.ifEmpty { sampleWords }

    val foundWords = remember(puzzleSeed, initialFoundWords) {
        mutableStateListOf<FoundWord>().apply { addAll(initialFoundWords) }
    }

    val isPuzzleComplete = targetWords.isNotEmpty() && foundWords.size >= targetWords.size
    val progress = if (targetWords.isNotEmpty()) (foundWords.size.toFloat() / targetWords.size).coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header Info & Restart Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = categoryDisplayName.uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechButtonNeon,
                    )
                    Text(
                        text = "DIFFICULTY: ${difficulty.uppercase()}",
                        fontSize = 12.sp,
                        color = TechTextCyan.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Monospace,
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TechTileBackground)
                        .border(1.dp, TechButtonNeon.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable {
                            puzzleSeed++
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "NEW PUZZLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechButtonNeon,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Word Search Grid
            InteractiveWordSearchGrid(
                grid = grid,
                targetWords = targetWords,
                foundWords = foundWords,
                onWordFound = { newFound ->
                    if (foundWords.none { it.word.equals(newFound.word, ignoreCase = true) }) {
                        foundWords.add(newFound)
                    }
                },
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Header & Linear Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TechContainerBackground)
                    .border(1.dp, TechButtonNeon.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "WORDS TO FIND",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechTextCyan,
                    )

                    Text(
                        text = "${foundWords.size} / ${targetWords.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = if (isPuzzleComplete) TechGold else TechButtonNeon,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isPuzzleComplete) TechGold else TechButtonNeon,
                    trackColor = TechTileBackground,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Words Flow List
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    targetWords.forEach { word ->
                        val foundIndex = foundWords.indexOfFirst { it.word.equals(word, ignoreCase = true) }
                        val isFound = foundIndex != -1
                        val chipColor = if (isFound) {
                            FoundWordColors[foundIndex % FoundWordColors.size]
                        } else {
                            TechTextCyan
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isFound) chipColor.copy(alpha = 0.22f) else TechTileBackground
                                )
                                .border(
                                    width = if (isFound) 1.5.dp else 1.dp,
                                    color = if (isFound) chipColor else TechButtonNeon.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                if (isFound) {
                                    Text(
                                        text = "✓",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = chipColor,
                                    )
                                }
                                Text(
                                    text = word.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = if (isFound) FontWeight.Bold else FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isFound) chipColor else TechTextCyan.copy(alpha = 0.9f),
                                    textDecoration = if (isFound) TextDecoration.LineThrough else TextDecoration.None,
                                )
                            }
                        }
                    }
                }
            }

            // Puzzle Completion Celebration Card
            AnimatedVisibility(
                visible = isPuzzleComplete,
                enter = fadeIn() + scaleIn(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TechContainerBackground)
                        .border(1.5.dp, TechGold, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "🎉 PUZZLE COMPLETED!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = TechGold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "All ${targetWords.size} words successfully discovered!",
                        fontSize = 13.sp,
                        color = TechTextCyan,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { puzzleSeed++ },
                        colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = "PLAY NEXT PUZZLE",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TechDarkBackground,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom AdMob Banner
        AdMobBanner()
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

@Preview(showBackground = true)
@Composable
fun GameBoardScreenWithFoundPreview() {
    val sampleGenerator = WordSearchGenerator()
    val words = listOf("KOTLIN", "ANDROID", "COMPOSE", "ROOM")
    val grid = sampleGenerator.generateGrid(size = 8, words = words)

    val sampleFound = listOf(
        FoundWord(
            word = "KOTLIN",
            startCell = grid[0][0],
            endCell = grid[0][5],
        ),
        FoundWord(
            word = "ROOM",
            startCell = grid[1][0],
            endCell = grid[1][3],
        ),
    )

    GameBoardScreen(
        category = "Technology",
        difficulty = "Easy",
        initialFoundWords = sampleFound,
    )
}
