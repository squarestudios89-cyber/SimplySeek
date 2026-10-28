package com.squarestudios.simplyseek.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.generator.WordSearchGenerator
import com.squarestudios.simplyseek.model.Cell

// Tech Fluid Color Palette
private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechBorderNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

/**
 * Composable that displays a 2D grid of [Cell] objects with a dark "tech fluid" aesthetic.
 *
 * @param grid The 2D list of [Cell] objects representing the Word Search board.
 * @param modifier Custom modifier for styling and layout positioning.
 * @param cellSize The fixed square size for every cell tile (default: 40.dp).
 * @param cellSpacing The spacing between cell tiles in the grid (default: 4.dp).
 */
@Composable
fun WordSearchGrid(
    grid: List<List<Cell>>,
    modifier: Modifier = Modifier,
    cellSize: Dp = 40.dp,
    cellSpacing: Dp = 4.dp,
) {
    Box(
        modifier = modifier
            .background(TechDarkBackground)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Outer Tech Container with gradient border
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(TechContainerBackground)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            TechBorderNeon.copy(alpha = 0.5f),
                            Color(0xFF7C4DFF).copy(alpha = 0.3f),
                            TechBorderNeon.copy(alpha = 0.2f),
                        ),
                    ),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(cellSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                for (row in grid) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        for (cell in row) {
                            CellTile(
                                cell = cell,
                                cellSize = cellSize,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Renders an individual square cell tile containing a letter.
 */
@Composable
fun CellTile(
    cell: Cell,
    cellSize: Dp = 40.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .clip(RoundedCornerShape(8.dp))
            .background(TechTileBackground)
            .border(
                width = 1.dp,
                color = TechBorderNeon.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = cell.char.toString(),
            color = TechTextCyan,
            fontSize = (cellSize.value * 0.45f).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordSearchGridPreview() {
    val sampleGenerator = WordSearchGenerator()
    val sampleGrid = sampleGenerator.generateGrid(
        size = 8,
        words = listOf("KOTLIN", "SEARCH", "SEEK", "TECH"),
    )

    WordSearchGrid(grid = sampleGrid)
}
