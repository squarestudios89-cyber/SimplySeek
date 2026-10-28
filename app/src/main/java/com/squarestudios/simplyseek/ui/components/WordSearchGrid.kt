package com.squarestudios.simplyseek.ui.components

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
import com.squarestudios.simplyseek.model.Cell
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.round
import kotlin.math.sin

// Tech Fluid Color Palette
private val TechDarkBackground = Color(0xFF0B0D14)
private val TechContainerBackground = Color(0xFF131722)
private val TechTileBackground = Color(0xFF1B2030)
private val TechBorderNeon = Color(0xFF00E5FF)
private val TechTextCyan = Color(0xFF80D8FF)
private val SelectionLineCyan = Color(0x9900E5FF) // Semi-transparent cyan

/**
 * Data class holding active selection gesture state.
 */
data class SelectionLine(
    val startCenter: Offset,
    val snappedEndCenter: Offset,
    val startCell: Cell,
    val currentCell: Cell,
)

/**
 * Composable that displays a 2D grid of [Cell] objects with a dark "tech fluid" aesthetic
 * and an interactive swipe-to-select Canvas overlay snapping to 8-way angles.
 *
 * @param grid The 2D list of [Cell] objects.
 * @param modifier Custom layout modifier.
 * @param cellSize Square cell tile size (default: 40.dp).
 * @param cellSpacing Space between tiles (default: 4.dp).
 * @param onSelectionComplete Callback invoked when a drag gesture finishes with start and end cells.
 */
@Composable
fun InteractiveWordSearchGrid(
    grid: List<List<Cell>>,
    modifier: Modifier = Modifier,
    cellSize: Dp = 40.dp,
    cellSpacing: Dp = 4.dp,
    onSelectionComplete: ((startCell: Cell, endCell: Cell) -> Unit)? = null,
) {
    var activeSelection by remember { mutableStateOf<SelectionLine?>(null) }

    val density = LocalDensity.current
    val cellSizePx = with(density) { cellSize.toPx() }
    val cellSpacingPx = with(density) { cellSpacing.toPx() }
    val containerPaddingPx = with(density) { 12.dp.toPx() }

    val cellStepPx = cellSizePx + cellSpacingPx

    val numRows = grid.size
    val numCols = if (grid.isNotEmpty()) grid[0].size else 0

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

            // Swipe-to-select Canvas Overlay
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(grid) {
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
                                    onSelectionComplete?.invoke(selection.startCell, selection.currentCell)
                                }
                                activeSelection = null
                            },
                            onDragCancel = {
                                activeSelection = null
                            },
                        )
                    },
            ) {
                activeSelection?.let { selection ->
                    drawLine(
                        color = SelectionLineCyan,
                        start = selection.startCenter,
                        end = selection.snappedEndCenter,
                        strokeWidth = cellSizePx * 0.7f,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}

/**
 * Calculates zero-based cell row and column indices from canvas touch coordinates.
 */
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

/**
 * Calculates the center [Offset] of a cell at (row, col) relative to container top-left.
 */
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

/**
 * Static non-interactive WordSearchGrid.
 */
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
fun InteractiveWordSearchGridPreview() {
    val sampleGenerator = WordSearchGenerator()
    val sampleGrid = sampleGenerator.generateGrid(
        size = 8,
        words = listOf("KOTLIN", "SEARCH", "SEEK", "TECH"),
    )

    InteractiveWordSearchGrid(grid = sampleGrid)
}
