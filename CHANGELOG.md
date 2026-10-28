# Changelog

All notable changes to the **SimplySeek** (Word Search) Android project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Planned
- Target word cross-off checklist & game win animations.
- Sound effects, haptics, and game state save/load integration.

---

## [0.5.0] - 2026-03-31

### Added
- **Swipe-to-Select Canvas Overlay** in [`WordSearchGrid.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt):
  - Created `@Composable fun InteractiveWordSearchGrid` overlaying a Jetpack Compose `Canvas` on top of the text grid.
  - Implemented `Modifier.pointerInput` with `detectDragGestures` to detect drag gestures.
  - Calculates starting cell row/col and center position on drag start based on cell size (`40.dp`).
  - Dynamically draws a thick semi-transparent cyan selection line (`Color(0x9900E5FF)`) with `StrokeCap.Round` connecting cell centers.
  - Implemented 8-way angle snapping (`atan2` + `PI / 4` rounding) ensuring lines strictly snap to horizontal, vertical, or 45° diagonal vectors.
  - Invokes `onSelectionComplete(startCell, endCell)` callback when drag gesture completes.

---

## [0.4.0] - 2026-03-31

### Added
- **Jetpack Compose Word Search Grid UI** in [`WordSearchGrid.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt):
  - Added `@Composable fun WordSearchGrid` to render a 2D list of `Cell` objects.
  - Implemented dark "tech fluid" aesthetic with dark obsidian background, cyan monospace typography, and neon gradient container borders.
  - Rendered square cell tiles (`CellTile`) with configurable dimensions (default `40.dp`).
  - Created Compose Preview `@Composable fun WordSearchGridPreview()`.

---

## [0.3.0] - 2026-03-31

### Added
- **Room Database Integration** in [`com.squarestudios.simplyseek.db`](app/src/main/java/com/squarestudios/simplyseek/db/AppDatabase.kt):
  - `SavedGame` Entity: Persists serialized 2D grid (`List<List<Cell>>`), target words (`List<String>`), elapsed time, and difficulty level.
  - `UserStats` Entity: Tracks total XP, total puzzles completed, and current daily streak.
  - `SavedGameDao` & `UserStatsDao`: Standard DAOs providing reactive `Flow` and coroutine `suspend` methods for insert, update, read, and delete operations.
  - `Converters`: TypeConverters converting `List<List<Cell>>` and `List<String>` to/from JSON strings via Gson.
  - `AppDatabase`: Abstract RoomDatabase singleton instance.
- **Room TypeConverters Unit Tests** in [`ConvertersTest.kt`](app/src/test/java/com/squarestudios/simplyseek/db/converter/ConvertersTest.kt).

---

## [0.2.0] - 2026-03-31

### Added
- **Procedural Word Search Generator** in [`WordSearchGenerator.kt`](app/src/main/java/com/squarestudios/simplyseek/generator/WordSearchGenerator.kt):
  - Procedurally places words in 8 directions (Horizontal, Vertical, Diagonal; forward and backward).
  - Handles character overlaps when intersecting words share identical letters.
  - Fills remaining empty spaces with random uppercase letters ('A'..'Z').
  - Returns populated 2D `List<List<Cell>>` array and `GridState`.
- **Generator Unit Tests** in [`WordSearchGeneratorTest.kt`](app/src/test/java/com/squarestudios/simplyseek/generator/WordSearchGeneratorTest.kt):
  - Verified 10x10 and custom grid size generation, letter bounds, and searchability of placed words.

---

## [0.1.0] - 2026-03-31

### Added
- **Core Data Models** in [`com.squarestudios.simplyseek.model`](app/src/main/java/com/squarestudios/simplyseek/model/WordSearchModels.kt):
  - `Cell`: Represents a character at a specific row and column (`char: Char`, `rowIndex: Int`, `colIndex: Int`).
  - `FoundWord`: Tracks a found word on the grid (`word: String`, `startCell: Cell`, `endCell: Cell`).
  - `GridState`: Holds the 2D grid (`List<List<Cell>>`), target words (`List<String>`), and discovered words (`List<FoundWord>`).
- **Unit Tests** in [`com.squarestudios.simplyseek.model.WordSearchModelsTest`](app/src/test/java/com/squarestudios/simplyseek/model/WordSearchModelsTest.kt) verifying creation and state integrity of all core data models.
- **Changelog**: Initialized `CHANGELOG.md` for project version tracking.
