package com.squarestudios.simplyseek.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.ads.AdMobBanner
import com.squarestudios.simplyseek.auth.AuthRepository
import com.squarestudios.simplyseek.db.repository.UserStatsRepository
import com.squarestudios.simplyseek.generator.WordSearchGenerator
import com.squarestudios.simplyseek.leaderboard.LeaderboardRepository
import com.squarestudios.simplyseek.model.CategoryCatalog
import com.squarestudios.simplyseek.model.FoundWord
import com.squarestudios.simplyseek.ui.components.FoundWordColors
import com.squarestudios.simplyseek.ui.components.InteractiveWordSearchGrid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

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
    onNavigateToLeaderboard: () -> Unit = {},
    leaderboardRepository: LeaderboardRepository = remember { LeaderboardRepository() },
    authRepository: AuthRepository = remember { AuthRepository() },
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val statsRepository = remember(context) { UserStatsRepository.getInstance(context) }

    val size = when (difficulty.lowercase()) {
        "easy" -> 8
        "hard" -> 10
        else -> 9 // Medium
    }

    var puzzleSeed by remember { mutableIntStateOf(0) }
    var elapsedTimeSeconds by remember(puzzleSeed) { mutableLongStateOf(0L) }
    var isScoreSubmitted by remember(puzzleSeed) { mutableStateOf(false) }
    var isSubmittingScore by remember(puzzleSeed) { mutableStateOf(false) }

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

    // Active Game Timer
    LaunchedEffect(puzzleSeed, isPuzzleComplete) {
        if (!isPuzzleComplete) {
            while (isActive) {
                delay(1000L)
                elapsedTimeSeconds++
            }
        }
    }

    // Save stats when puzzle is completed
    LaunchedEffect(isPuzzleComplete) {
        if (isPuzzleComplete) {
            withContext(Dispatchers.IO) {
                statsRepository.recordPuzzleCompleted(difficulty)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header Info, Live Timer & Restart Action
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
                        text = "DIFFICULTY: ${difficulty.uppercase()} • PUZZLE #${puzzleSeed + 1}",
                        fontSize = 12.sp,
                        color = TechTextCyan.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Monospace,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Live Solve Timer
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTileBackground)
                            .border(1.dp, TechButtonNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val mins = elapsedTimeSeconds / 60
                        val secs = elapsedTimeSeconds % 60
                        Text(
                            text = "⏱ " + String.format(Locale.US, "%02d:%02d", mins, secs),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TechButtonNeon,
                        )
                    }

                    // New Puzzle Action
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTileBackground)
                            .border(1.dp, TechButtonNeon.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable {
                                puzzleSeed++
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "NEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TechButtonNeon,
                        )
                    }
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
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechTextCyan.copy(alpha = 0.7f),
                    )
                    Text(
                        text = "${foundWords.size} / ${targetWords.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechButtonNeon,
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
                                    if (isFound) chipColor.copy(alpha = 0.22f) else TechTileBackground,
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
                val finalMins = elapsedTimeSeconds / 60
                val finalSecs = elapsedTimeSeconds % 60
                val timeString = String.format(Locale.US, "%02d:%02d", finalMins, finalSecs)

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
                        text = "Discovered all ${targetWords.size} words in $timeString!",
                        fontSize = 13.sp,
                        color = TechTextCyan,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Score Submission Button
                    if (!isScoreSubmitted) {
                        Button(
                            onClick = {
                                isSubmittingScore = true
                                coroutineScope.launch {
                                    val result = leaderboardRepository.submitScore(
                                        playerName = authRepository.playerName,
                                        timeInSeconds = elapsedTimeSeconds,
                                        difficulty = difficulty,
                                        userId = authRepository.userId ?: "",
                                        userPhotoUrl = authRepository.photoUrl ?: "",
                                        categoryTitle = categoryDisplayName,
                                    )
                                    isSubmittingScore = false
                                    result.onSuccess {
                                        isScoreSubmitted = true
                                        Toast.makeText(context, "Score submitted to Global Leaderboard!", Toast.LENGTH_SHORT).show()
                                    }.onFailure { err ->
                                        Toast.makeText(context, err.message ?: "Failed to submit score", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            enabled = !isSubmittingScore,
                            colors = ButtonDefaults.buttonColors(containerColor = TechGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(0.85f),
                        ) {
                            Text(
                                text = if (isSubmittingScore) "SUBMITTING..." else "🏆 SUBMIT TO LEADERBOARD",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black,
                            )
                        }
                    } else {
                        Text(
                            text = "✓ SCORE RECORDED ON LEADERBOARD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00E676),
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToLeaderboard,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, TechButtonNeon.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(
                                text = "LEADERBOARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TechButtonNeon,
                            )
                        }

                        Button(
                            onClick = { puzzleSeed++ },
                            colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "NEXT PUZZLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TechDarkBackground,
                            )
                        }
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
    )

    GameBoardScreen(
        category = "Technology",
        difficulty = "Medium",
        initialFoundWords = sampleFound,
    )
}
