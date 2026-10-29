package com.squarestudios.simplyseek.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squarestudios.simplyseek.generator.WordSearchGenerator
import com.squarestudios.simplyseek.logic.WordSearchSelectionManager
import com.squarestudios.simplyseek.model.Cell
import com.squarestudios.simplyseek.model.FoundWord
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.round
import kotlin.math.sin

/**
 * Available visual selection styles that the player can choose from.
 */
enum class SelectionStyle(val displayName: String) {
    ILLUMINATED_TILES("Illuminated Grid"),
    LASER_LINE("Laser Filament"),
    CAPSULE_OUTLINE("Neon Capsule"),
    CLASSIC_HIGHLIGHT("Classic Highlight"),
}

// Tech Fluid Color Palette
private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechBorderNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)

// Illuminated Tile Grid Colors
private val ActiveTileBackground = Color(0x4D00E5FF)
private val ActiveTileBorder = Color(0xFF00E5FF)
private val ActiveTileText = Color(0xFFFFFFFF)

private val FoundTileBackground = Color(0x4D00E676)
private val FoundTileBorder = Color(0xFF00E676)
private val FoundTileText = Color(0xFFB9F6CA)

private val SelectionLineCyan = Color(0x9900E5FF)
private val PermanentHighlightGreen = Color(0x9900E676)

/**
 * Data class holding active drag selection state.
 */
data class SelectionLine(
    val startCenter: Offset,
    val snappedEndCenter: Offset,
    val startCell: Cell,
    val currentCell: Cell,
)

/**
 * Interactive Word Search grid supporting customizable selection visual styles.
 *
 * @param grid The 2D list of [Cell] objects.
 * @param targetWords List of target words to match against.
 * @param foundWords List of [FoundWord] objects discovered so far.
 * @param selectionStyle Visual selection indicator style chosen by the user.
 * @param modifier Custom layout modifier.
 * @param cellSize Square cell tile size (default: 40.dp).
 * @param cellSpacing Space between tiles (default: 4.dp).
 * @param onWordFound Callback when a target word is matched upon drag completion.
 */
@Composable
fun InteractiveWordSearchGrid(
    grid: List<List<Cell>>,
    targetWords: List<String> = emptyList(),
    foundWords: List<FoundWord> = emptyList(),
    selectionStyle: SelectionStyle = SelectionStyle.ILLUMINATED_TILES,
    modifier: Modifier = Modifier,
    cellSize: Dp = 40.dp,
    cellSpacing: Dp = 4.dp,
    onWordFound: ((FoundWord) -> Unit)? = null,
) {
    var activeSelection by remember { mutableStateOf<SelectionLine?>(null) }

    val density = LocalDensity.current
    val cellSizePx = with(density) { cellSize.toPx() }
    val cellSpacingPx = with(density) { cellSpacing.toPx() }
    val containerPaddingPx = with(density) { 12.dp.toPx() }

    val cellStepPx = cellSizePx + cellSpacingPx

    val numRows = grid.size
    val numCols = if (grid.isNotEmpty()) grid[0].size else 0

    // Compute set of currently active dragged cells (for Illuminated Tiles mode)
    val activeSelectedCellPairs = remember(activeSelection, grid, selectionStyle) {
        if (selectionStyle != SelectionStyle.ILLUMINATED_TILES) return@remember emptySet()
        val selection = activeSelection ?: return@remember emptySet()
        WordSearchSelectionManager.extractCellsBetween(selection.startCell, selection.currentCell, grid)
            .map { Pair(it.rowIndex, it.colIndex) }
            .toSet()
    }

    // Compute set of permanently found cell pairs (for Illuminated Tiles mode)
    val foundCellPairs = remember(foundWords, grid, selectionStyle) {
        if (selectionStyle != SelectionStyle.ILLUMINATED_TILES) return@remember emptySet()
        foundWords.flatMap { foundWord ->
            WordSearchSelectionManager.extractCellsBetween(foundWord.startCell, foundWord.endCell, grid)
                .map { Pair(it.rowIndex, it.colIndex) }
        }.toSet()
    }

    Box(
        modifier = modifier
            .background(TechDarkBackground)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Outer Tech Container
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
                            val coord = Pair(cell.rowIndex, cell.colIndex)
                            val isSelected = activeSelectedCellPairs.contains(coord)
                            val isFound = foundCellPairs.contains(coord)

                            CellTile(
                                cell = cell,
                                cellSize = cellSize,
                                isSelected = isSelected,
                                isFound = isFound,
                            )
                        }
                    }
                }
            }

            // Selection & Highlight Overlay Canvas
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(grid, targetWords, foundWords, selectionStyle) {
                        detectDragGestures(
                            onDragStart = { startOffset ->
                                val (startRow, startCol) = getCellCoordinates(
                                    offset = startOffset,
                                    paddingPx = containerPaddingPx,
                                    cellStepPx = cellStepPx,
                                    numRows = numRows,
                                    numCols = numCols,
                                )
                                if (startRow in 0 until numRows && startCol in 0 until numCols) {
                                    val startCell = grid[startRow][startCol]
                                    val startCenter = getCellCenterOffset(
                                        row = startRow,
                                        col = startCol,
                                        paddingPx = containerPaddingPx,
                                        cellStepPx = cellStepPx,
                                        cellSizePx = cellSizePx,
                                    )
                                    activeSelection = SelectionLine(
                                        startCenter = startCenter,
                                        snappedEndCenter = startCenter,
                                        startCell = startCell,
                                        currentCell = startCell,
                                    )
                                }
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val currentSelection = activeSelection ?: return@detectDragGestures
                                val currentTouch = change.position

                                val dx = (currentTouch.x - currentSelection.startCenter.x).toDouble()
                                val dy = (currentTouch.y - currentSelection.startCenter.y).toDouble()
                                val distance = hypot(dx, dy)

                                if (distance > 5) {
                                    val angleRad = atan2(dy, dx)
                                    val snappedAngleRad = round(angleRad / (PI / 4.0)) * (PI / 4.0)

                                    val snappedEndX = (currentSelection.startCenter.x + distance * cos(snappedAngleRad)).toFloat()
                                    val snappedEndY = (currentSelection.startCenter.y + distance * sin(snappedAngleRad)).toFloat()
                                    val snappedEndCenter = Offset(snappedEndX, snappedEndY)

                                    val (currRow, currCol) = getCellCoordinates(
                                        offset = snappedEndCenter,
                                        paddingPx = containerPaddingPx,
                                        cellStepPx = cellStepPx,
                                        numRows = numRows,
                                        numCols = numCols,
                                    )

                                    val currentCell = grid[currRow][currCol]

                                    activeSelection = currentSelection.copy(
                                        snappedEndCenter = snappedEndCenter,
                                        currentCell = currentCell,
                                    )
                                }
                            },
                            onDragEnd = {
                                activeSelection?.let { selection ->
                                    val match = WordSearchSelectionManager.checkWordMatch(
                                        startCell = selection.startCell,
                                        endCell = selection.currentCell,
                                        grid = grid,
                                        targetWords = targetWords,
                                        alreadyFoundWords = foundWords,
                                    )
                                    if (match != null) {
                                        onWordFound?.invoke(match)
                                    }
                                }
                                activeSelection = null
                            },
                            onDragCancel = {
                                activeSelection = null
                            },
                        )
                    },
            ) {
                // Render Canvas elements for non-Illuminated Tile styles
                if (selectionStyle != SelectionStyle.ILLUMINATED_TILES) {
                    // 1. Permanent Highlights for Found Words
                    for (foundWord in foundWords) {
                        val startCenter = getCellCenterOffset(
                            row = foundWord.startCell.rowIndex,
                            col = foundWord.startCell.colIndex,
                            paddingPx = containerPaddingPx,
                            cellStepPx = cellStepPx,
                            cellSizePx = cellSizePx,
                        )
                        val endCenter = getCellCenterOffset(
                            row = foundWord.endCell.rowIndex,
                            col = foundWord.endCell.colIndex,
                            paddingPx = containerPaddingPx,
                            cellStepPx = cellStepPx,
                            cellSizePx = cellSizePx,
                        )

                        val strokeWidth = when (selectionStyle) {
                            SelectionStyle.LASER_LINE -> 6.dp.toPx()
                            SelectionStyle.CAPSULE_OUTLINE -> cellSizePx * 0.75f
                            else -> cellSizePx * 0.75f
                        }

                        drawLine(
                            color = PermanentHighlightGreen,
                            start = startCenter,
                            end = endCenter,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                        )
                    }

                    // 2. Active Drag Selection Line
                    activeSelection?.let { selection ->
                        when (selectionStyle) {
                            SelectionStyle.LASER_LINE -> {
                                drawLine(
                                    color = TechBorderNeon,
                                    start = selection.startCenter,
                                    end = selection.snappedEndCenter,
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round,
                                )
                            }

                            SelectionStyle.CAPSULE_OUTLINE -> {
                                drawLine(
                                    color = TechBorderNeon,
                                    start = selection.startCenter,
                                    end = selection.snappedEndCenter,
                                    strokeWidth = cellSizePx * 0.75f,
                                    cap = StrokeCap.Round,
                                )
                            }

                            SelectionStyle.CLASSIC_HIGHLIGHT -> {
                                drawLine(
                                    color = SelectionLineCyan,
                                    start = selection.startCenter,
                                    end = selection.snappedEndCenter,
                                    strokeWidth = cellSizePx * 0.7f,
                                    cap = StrokeCap.Round,
                                )
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CellTile(
    cell: Cell,
    cellSize: Dp = 40.dp,
    isSelected: Boolean = false,
    isFound: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val targetBgColor = when {
        isFound -> FoundTileBackground
        isSelected -> ActiveTileBackground
        else -> TechTileBackground
    }

    val targetBorderColor = when {
        isFound -> FoundTileBorder
        isSelected -> ActiveTileBorder
        else -> TechBorderNeon.copy(alpha = 0.15f)
    }

    val targetTextColor = when {
        isFound -> FoundTileText
        isSelected -> ActiveTileText
        else -> TechTextCyan
    }

    val animatedBgColor by animateColorAsState(targetValue = targetBgColor, animationSpec = tween(150), label = "bgColor")
    val animatedBorderColor by animateColorAsState(targetValue = targetBorderColor, animationSpec = tween(150), label = "borderColor")
    val animatedTextColor by animateColorAsState(targetValue = targetTextColor, animationSpec = tween(150), label = "textColor")

    Box(
        modifier = modifier
            .size(cellSize)
            .clip(RoundedCornerShape(8.dp))
            .background(animatedBgColor)
            .border(
                width = if (isSelected || isFound) 2.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = cell.char.toString(),
            color = animatedTextColor,
            fontSize = (cellSize.value * 0.45f).sp,
            fontWeight = if (isSelected || isFound) FontWeight.ExtraBold else FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

private fun getCellCoordinates(
    offset: Offset,
    paddingPx: Float,
    cellStepPx: Float,
    numRows: Int,
    numCols: Int,
): Pair<Int, Int> {
    val col = ((offset.x - paddingPx) / cellStepPx).toInt().coerceIn(0, (numCols - 1).coerceAtLeast(0))
    val row = ((offset.y - paddingPx) / cellStepPx).toInt().coerceIn(0, (numRows - 1).coerceAtLeast(0))
    return Pair(row, col)
}

private fun getCellCenterOffset(
    row: Int,
    col: Int,
    paddingPx: Float,
    cellStepPx: Float,
    cellSizePx: Float,
): Offset {
    val x = paddingPx + col * cellStepPx + cellSizePx / 2f
    val y = paddingPx + row * cellStepPx + cellSizePx / 2f
    return Offset(x, y)
}

@Composable
fun WordSearchGrid(
    grid: List<List<Cell>>,
    modifier: Modifier = Modifier,
    cellSize: Dp = 40.dp,
    cellSpacing: Dp = 4.dp,
) {
    InteractiveWordSearchGrid(
        grid = grid,
        modifier = modifier,
        cellSize = cellSize,
        cellSpacing = cellSpacing,
    )
}

@Preview(showBackground = true)
@Composable
fun InteractiveWordSearchGridPreview() {
    val sampleGenerator = WordSearchGenerator()
    val sampleGrid = sampleGenerator.generateGrid(
        size = 8,
        words = listOf("KOTLIN", "SEARCH", "SEEK", "TECH"),
    )

    val sampleFound = listOf(
        FoundWord(
            word = "TECH",
            startCell = sampleGrid[0][0],
            endCell = sampleGrid[0][3],
        )
    )

    InteractiveWordSearchGrid(
        grid = sampleGrid,
        targetWords = listOf("KOTLIN", "SEARCH", "SEEK", "TECH"),
        foundWords = sampleFound,
        selectionStyle = SelectionStyle.ILLUMINATED_TILES,
    )
}
