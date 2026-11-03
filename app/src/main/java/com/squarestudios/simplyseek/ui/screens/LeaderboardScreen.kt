package com.squarestudios.simplyseek.ui.screens

import android.app.Activity
import android.widget.Toast
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.squarestudios.simplyseek.auth.AuthRepository
import com.squarestudios.simplyseek.leaderboard.LeaderboardEntry
import com.squarestudios.simplyseek.leaderboard.LeaderboardRepository
import kotlinx.coroutines.launch

// Tech Theme Tokens
private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)
private val TechGold = Color(0xFFFFD600)
private val TechSilver = Color(0xFFCFD8DC)
private val TechBronze = Color(0xFFFF9100)
private val TechEmerald = Color(0xFF00E676)

/**
 * Screen presenting real-time global player rankings partitioned by difficulty tier.
 */
@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    leaderboardRepository: LeaderboardRepository = remember { LeaderboardRepository() },
    authRepository: AuthRepository = remember { AuthRepository() },
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentUser by authRepository.currentUser.collectAsState()

    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var isSigningIn by remember { mutableStateOf(false) }

    val difficulties = listOf("Easy", "Medium", "Hard")
    val topScoresFlow = remember(selectedDifficulty) {
        leaderboardRepository.getTopScoresFlow(selectedDifficulty)
    }
    val scoresState by topScoresFlow.collectAsState(initial = null)

    Surface(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        color = TechDarkBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = TechButtonNeon,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GLOBAL LEADERBOARDS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                    )
                    Text(
                        text = "WORLDWIDE SPEEDRUN OPERATORS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechTextCyan.copy(alpha = 0.7f),
                    )
                }

                Text(
                    text = "🏆",
                    fontSize = 26.sp,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Player Account Bar
            PlayerAuthCard(
                user = currentUser,
                isSigningIn = isSigningIn,
                onSignInClick = {
                    val activity = context as? Activity
                    if (activity != null) {
                        isSigningIn = true
                        coroutineScope.launch {
                            val result = authRepository.signInWithGoogle(activity)
                            isSigningIn = false
                            result.onSuccess {
                                Toast.makeText(context, "Welcome, ${it.displayName ?: "Seeker"}!", Toast.LENGTH_SHORT).show()
                            }.onFailure { err ->
                                Toast.makeText(context, err.message ?: "Sign-in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onSignOutClick = {
                    authRepository.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                },
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Difficulty Tab Selector
            TabRow(
                selectedTabIndex = difficulties.indexOf(selectedDifficulty).coerceAtLeast(0),
                containerColor = TechContainerBackground,
                contentColor = TechButtonNeon,
                indicator = { tabPositions ->
                    val selectedIndex = difficulties.indexOf(selectedDifficulty).coerceAtLeast(0)
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        color = TechButtonNeon,
                        height = 3.dp,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, TechButtonNeon.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
            ) {
                difficulties.forEach { diff ->
                    val isSelected = selectedDifficulty == diff
                    Tab(
                        selected = isSelected,
                        onClick = { selectedDifficulty = diff },
                        text = {
                            Text(
                                text = diff.uppercase(),
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = if (isSelected) TechButtonNeon else TechTextCyan.copy(alpha = 0.6f),
                            )
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Leaderboard Entries List
            val scores = scoresState
            when {
                scores == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = TechButtonNeon,
                                modifier = Modifier.size(36.dp),
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "CONNECTING TO LIVE LEADERBOARD...",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TechTextCyan.copy(alpha = 0.7f),
                            )
                        }
                    }
                }

                scores.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp),
                        ) {
                            Text(
                                text = "🏆",
                                fontSize = 48.sp,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "NO SCORES LOGGED YET",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Complete a ${selectedDifficulty} puzzle to claim Rank #1 on the worldwide board!",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TechTextCyan.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp),
                    ) {
                        itemsIndexed(scores) { index, entry ->
                            LeaderboardRow(rank = index + 1, entry = entry)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top card showing current user's profile or Google Sign-In button.
 */
@Composable
private fun PlayerAuthCard(
    user: com.google.firebase.auth.FirebaseUser?,
    isSigningIn: Boolean,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TechContainerBackground)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(TechButtonNeon.copy(alpha = 0.35f), Color(0xFF7C4DFF).copy(alpha = 0.2f)),
                ),
                RoundedCornerShape(14.dp),
            )
            .padding(12.dp),
    ) {
        if (user != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    if (!user.photoUrl?.toString().isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Player Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, TechEmerald, CircleShape),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(TechTileBackground)
                                .border(1.5.dp, TechEmerald, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = (user.displayName?.firstOrNull() ?: 'U').uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = TechEmerald,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = user.displayName?.ifBlank { "Operator" } ?: "Operator",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(12.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "GOOGLE VERIFIED",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TechEmerald,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                Text(
                    text = "SIGN OUT",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5252),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(onClick = onSignOutClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = TechTextCyan.copy(alpha = 0.5f),
                        modifier = Modifier.size(34.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "GUEST OPERATOR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                        )
                        Text(
                            text = "Sign in to record verified scores",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TechTextCyan.copy(alpha = 0.6f),
                        )
                    }
                }

                Button(
                    onClick = onSignInClick,
                    enabled = !isSigningIn,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TechButtonNeon,
                        contentColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    if (isSigningIn) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "SIGN IN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single leaderboard row displaying rank badge, player, category, and solve time.
 */
@Composable
private fun LeaderboardRow(
    rank: Int,
    entry: LeaderboardEntry,
) {
    val rankColor = when (rank) {
        1 -> TechGold
        2 -> TechSilver
        3 -> TechBronze
        else -> TechTextCyan.copy(alpha = 0.7f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TechContainerBackground)
            .border(
                width = if (rank <= 3) 1.5.dp else 1.dp,
                color = if (rank <= 3) rankColor.copy(alpha = 0.5f) else TechButtonNeon.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank Badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.15f))
                    .border(1.dp, rankColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "#$rank",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = rankColor,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Player Avatar
            if (entry.userPhotoUrl.isNotBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(entry.userPhotoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .border(1.dp, TechButtonNeon.copy(alpha = 0.5f), CircleShape),
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(TechTileBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = (entry.playerName.firstOrNull() ?: 'P').uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechTextCyan,
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Player Name and Category Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.playerName.ifBlank { "Anonymous Seeker" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (entry.categoryTitle.isNotBlank()) {
                    Text(
                        text = entry.categoryTitle.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TechTextCyan.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Solve Time Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(TechTileBackground)
                    .border(1.dp, TechButtonNeon.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "⏱",
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.width(4.dp))
                val minutes = entry.timeInSeconds / 60
                val seconds = entry.timeInSeconds % 60
                Text(
                    text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = TechButtonNeon,
                )
            }
        }
    }
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    LeaderboardScreen(onNavigateBack = {})
}
