package com.squarestudios.simplyseek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.squarestudios.simplyseek.model.CategoryCatalog
import com.squarestudios.simplyseek.model.CategoryGroup
import com.squarestudios.simplyseek.model.WordCategory

private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechButtonNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

@Composable
fun CategorySelectScreen(
    onStartGame: (category: String, difficulty: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val difficulties = listOf("Easy", "Medium", "Hard")

    var selectedGroup by remember { mutableStateOf(CategoryGroup.ALL) }
    var selectedCategory by remember { mutableStateOf(CategoryCatalog.allCategories.first()) }
    var selectedDifficulty by remember { mutableStateOf(difficulties[1]) }

    val filteredCategories = remember(selectedGroup) {
        CategoryCatalog.getCategoriesByGroup(selectedGroup)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechDarkBackground)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Text(
            text = "SELECT TOPIC",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = TechButtonNeon,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Choose from endless categories & themes",
            fontSize = 12.sp,
            color = TechTextCyan.copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Group Filter Horizontal Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CategoryGroup.entries.forEach { group ->
                val isSelected = group == selectedGroup
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TechButtonNeon.copy(alpha = 0.25f) else TechTileBackground)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) TechButtonNeon else TechButtonNeon.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .clickable { selectedGroup = group }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = group.label.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        color = if (isSelected) TechButtonNeon else TechTextCyan.copy(alpha = 0.8f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable Categories List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
        ) {
            items(filteredCategories, key = { it.id }) { category ->
                val isSelected = category.id == selectedCategory.id
                CategoryCard(
                    category = category,
                    isSelected = isSelected,
                    onSelect = { selectedCategory = category },
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Difficulty Selector Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(TechContainerBackground)
                .border(1.dp, TechButtonNeon.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(10.dp),
        ) {
            Text(
                text = "DIFFICULTY TIER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TechTextCyan,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                difficulties.forEach { diff ->
                    val isSelected = diff == selectedDifficulty
                    val sizeLabel = when (diff) {
                        "Easy" -> "8x8"
                        "Hard" -> "12x12"
                        else -> "10x10"
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) TechButtonNeon else TechTileBackground)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) TechButtonNeon else TechButtonNeon.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .clickable { selectedDifficulty = diff }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = diff.uppercase(),
                                color = if (isSelected) Color.Black else TechButtonNeon,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = sizeLabel,
                                color = if (isSelected) Color.Black.copy(alpha = 0.7f) else TechTextCyan.copy(alpha = 0.6f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Start Button
        Button(
            onClick = { onStartGame(selectedCategory.id, selectedDifficulty) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TechButtonNeon),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = "START PUZZLE: ${selectedCategory.title.uppercase()}",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: WordCategory,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) TechButtonNeon.copy(alpha = 0.18f) else TechContainerBackground)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) TechButtonNeon else TechButtonNeon.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onSelect)
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) TechButtonNeon.copy(alpha = 0.3f) else TechTileBackground)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) TechButtonNeon else TechButtonNeon.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = category.icon,
                    fontSize = 20.sp,
                )
            }

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = category.title,
                        color = if (isSelected) TechButtonNeon else Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )

                    Text(
                        text = "${category.wordPool.size} words",
                        fontSize = 10.sp,
                        color = TechTextCyan.copy(alpha = 0.6f),
                        fontFamily = FontFamily.Monospace,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = category.description,
                    color = TechTextCyan.copy(alpha = 0.75f),
                    fontSize = 11.sp,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectScreenPreview() {
    CategorySelectScreen(onStartGame = { _, _ -> })
}
