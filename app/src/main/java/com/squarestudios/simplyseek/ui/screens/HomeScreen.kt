package com.squarestudios.simplyseek.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.db.entity.UserStats
import com.squarestudios.simplyseek.db.repository.UserStatsRepository

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)
private val TechGold = Color(0xFFFFD600)
private val TechEmerald = Color(0xFF00E676)
private val TechPurple = Color(0xFFB388FF)
private val TechOrange = Color(0xFFFF9100)

@Composable
fun HomeScreen(
    onNavigateToCategorySelect: () -> Unit,
    onNavigateToStats: () -> Unit,
    modifier: Modifier = Modifier,
    initialStats: UserStats? = null,
) {
    val isInspection = LocalInspectionMode.current
    val context = LocalContext.current

    val statsRepository = remember(context) {
        if (!isInspection) UserStatsRepository.getInstance(context) else null
    }

    val liveStats by (statsRepository?.observeStats()?.collectAsState(initial = initialStats)
        ?: remember { androidx.compose.runtime.mutableStateOf(initialStats) })

    val currentStats = liveStats ?: UserStats(
        id = 1L,
        totalXp = 0L,
        totalPuzzlesCompleted = 0,
        currentDailyStreak = 0,
        lastStreakDate = "",
    )

    // Calculate level progression based on XP (each level is 250 XP)
    val xpPerLevel = 250L
    val currentLevel = (currentStats.totalXp / xpPerLevel) + 1
    val currentLevelXp = currentStats.totalXp % xpPerLevel
    val levelProgress = (currentLevelXp.toFloat() / xpPerLevel.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // App Title & Header Branding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TechTileBackground)
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(TechButtonNeon, TechPurple)),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 14.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "NEON WORD SEARCH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TechButtonNeon,
                    letterSpacing = 2.sp,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "SIMPLY SEEK",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = TechButtonNeon,
                letterSpacing = 1.sp,
            )

            Text(
                text = "Cybernetic Word Hunting Engine",
                fontSize = 13.sp,
                color = TechTextCyan.copy(alpha = 0.7f),
                fontFamily = FontFamily.Monospace,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live User Stats Dashboard
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TechContainerBackground)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                TechButtonNeon.copy(alpha = 0.6f),
                                TechPurple.copy(alpha = 0.3f),
                                TechButtonNeon.copy(alpha = 0.15f),
                            ),
                        ),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(16.dp),
            ) {
                // Dashboard Header: Player Rank & Level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(TechButtonNeon.copy(alpha = 0.2f))
                                .border(1.5.dp, TechButtonNeon, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "⚡",
                                fontSize = 16.sp,
                            )
                        }

                        Column {
                            Text(
                                text = "OPERATOR LEVEL $currentLevel",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = TechGold,
                            )
                            Text(
                                text = "Rank: ${getPlayerRankTitle(currentLevel)}",
                                fontSize = 11.sp,
                                color = TechTextCyan.copy(alpha = 0.75f),
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }

                    // Total XP Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTileBackground)
                            .border(1.dp, TechGold.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = "${currentStats.totalXp} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            color = TechGold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Level Progress Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "NEXT LEVEL PROGRESS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TechTextCyan.copy(alpha = 0.7f),
                        )
                        Text(
                            text = "$currentLevelXp / $xpPerLevel XP",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TechTextCyan,
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { levelProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TechGold,
                        trackColor = TechTileBackground,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Metrics Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // Puzzles Solved Metric
                    DashboardMetricCard(
                        modifier = Modifier.weight(1f),
                        label = "SOLVED",
                        value = "${currentStats.totalPuzzlesCompleted}",
                        subtext = "Puzzles Finished",
                        icon = "🧩",
                        accentColor = TechEmerald,
                    )

                    // Daily Streak Metric
                    DashboardMetricCard(
                        modifier = Modifier.weight(1f),
                        label = "STREAK",
                        value = "${currentStats.currentDailyStreak}d",
                        subtext = if (currentStats.currentDailyStreak > 0) "Daily Streak 🔥" else "Start Today!",
                        icon = "🔥",
                        accentColor = TechOrange,
                    )

                    // Accuracy / Mastery Tier
                    DashboardMetricCard(
                        modifier = Modifier.weight(1f),
                        label = "MASTERY",
                        value = "TIER $currentLevel",
                        subtext = "Word Hunter",
                        icon = "🎯",
                        accentColor = TechPurple,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onNavigateToCategorySelect,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "🎮 PLAY GAME",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 17.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onNavigateToStats,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(listOf(TechButtonNeon, TechPurple)),
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "📊 DETAILED STATS & LEADERBOARDS",
                    color = TechButtonNeon,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Category Teaser
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(TechTileBackground.copy(alpha = 0.5f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "FEATURED:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TechTextCyan.copy(alpha = 0.6f),
            )
            Text(
                text = "15 TOPICS & 350+ WORDS READY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TechButtonNeon,
            )
        }
    }
}

@Composable
private fun DashboardMetricCard(
    label: String,
    value: String,
    subtext: String,
    icon: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TechTileBackground)
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(text = icon, fontSize = 12.sp)
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TechTextCyan.copy(alpha = 0.7f),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = accentColor,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtext,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                color = TechTextCyan.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}

private fun getPlayerRankTitle(level: Long): String {
    return when {
        level >= 20 -> "Cyber Grandmaster"
        level >= 15 -> "Grid Overlord"
        level >= 10 -> "Code Hunter"
        level >= 5 -> "Seek Specialist"
        level >= 2 -> "Grid Apprentice"
        else -> "Novice Seeker"
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToCategorySelect = {},
        onNavigateToStats = {},
        initialStats = UserStats(
            id = 1L,
            totalXp = 680L,
            totalPuzzlesCompleted = 7,
            currentDailyStreak = 3,
            lastStreakDate = "2026-11-03",
        ),
    )
}
