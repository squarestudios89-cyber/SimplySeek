package com.squarestudios.simplyseek.ui.screens

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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
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
fun StatsScreen(
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

    val stats = liveStats ?: UserStats(
        id = 1L,
        totalXp = 0L,
        totalPuzzlesCompleted = 0,
        currentDailyStreak = 0,
        lastStreakDate = "",
    )

    val xpPerLevel = 250L
    val level = (stats.totalXp / xpPerLevel) + 1
    val currentLevelXp = stats.totalXp % xpPerLevel
    val levelProgress = (currentLevelXp.toFloat() / xpPerLevel.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Text(
            text = "CAREER STATS",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = TechButtonNeon,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Lifetime Word Search Progression",
            fontSize = 12.sp,
            color = TechTextCyan.copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Level & Operator Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(TechContainerBackground)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(TechGold, TechButtonNeon)),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(18.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(TechGold.copy(alpha = 0.2f))
                                .border(1.5.dp, TechGold, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = "👑", fontSize = 20.sp)
                        }

                        Column {
                            Text(
                                text = "LEVEL $level OPERATOR",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = TechGold,
                            )
                            Text(
                                text = "Next rank at Level ${level + 1}",
                                fontSize = 11.sp,
                                color = TechTextCyan.copy(alpha = 0.75f),
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }

                    Text(
                        text = "${stats.totalXp} XP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = TechGold,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "TIER PROGRESSION",
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
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = TechGold,
                    trackColor = TechTileBackground,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Key Metric Breakdown Cards
        StatDetailCard(
            title = "TOTAL EXPERIENCE POINTS",
            value = "${stats.totalXp} XP",
            subtext = "Accumulated across all solved word puzzles",
            icon = "⚡",
            accentColor = TechButtonNeon,
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatDetailCard(
            title = "PUZZLES SOLVED",
            value = "${stats.totalPuzzlesCompleted} Puzzles",
            subtext = "Fully discovered word search grids",
            icon = "🧩",
            accentColor = TechEmerald,
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatDetailCard(
            title = "CONSECUTIVE DAILY STREAK",
            value = "${stats.currentDailyStreak} Days",
            subtext = if (stats.lastStreakDate.isNotEmpty()) "Last active: ${stats.lastStreakDate}" else "Play today to start your streak!",
            icon = "🔥",
            accentColor = TechOrange,
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatDetailCard(
            title = "PUZZLE TIERS UNLOCKED",
            value = "15 / 15 Categories",
            subtext = "Tech, Cars, Games, Cinema, Comics & Cosmos",
            icon = "🌌",
            accentColor = TechPurple,
        )
    }
}

@Composable
private fun StatDetailCard(
    title: String,
    value: String,
    subtext: String,
    icon: String,
    accentColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TechContainerBackground)
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TechTileBackground)
                    .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = icon, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TechTextCyan.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = accentColor,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtext,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TechTextCyan.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    StatsScreen(
        initialStats = UserStats(
            id = 1L,
            totalXp = 1250L,
            totalPuzzlesCompleted = 14,
            currentDailyStreak = 5,
            lastStreakDate = "2026-11-03",
        )
    )
}
