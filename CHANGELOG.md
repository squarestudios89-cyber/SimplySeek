# Changelog

All notable changes to the **SimplySeek** (Word Search) Android project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.13.0] - 2026-10-30

### Added
- **Endless Category & Word Bank Architecture**:
  - Created [`CategoryCatalog.kt`](app/src/main/java/com/squarestudios/simplyseek/model/CategoryCatalog.kt) with **15 diverse playable categories** and over **350+ curated words**:
    - **Tech & Gaming**: Computer Hardware, Software & Coding, Video Game Classics, PlayStation Hits, Xbox Franchises.
    - **Automotive**: Car Makes, Car Engine Parts.
    - **Pop Culture**: Cinema Blockbusters, Comic Book Heroes, Culinary & Food, Rock Legends.
    - **Science & Nature**: Science & Physics, Deep Space & Cosmos, Wildlife & Animals, World Geography.
  - Implemented procedural word pool sampling (`CategoryCatalog.getWordsForCategory(...)`) with seed-based shuffling and grid length constraint filtering, providing endless unique puzzle variations for every category.
  - Upgraded [`CategorySelectScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/CategorySelectScreen.kt) with filter group tabs, scrollable card selector displaying category icons, word counts, and sample words, and tiered difficulty previews.
  - Integrated dynamic category resolution into [`GameBoardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt).
  - Added unit test suite [`CategoryCatalogTest.kt`](app/src/test/java/com/squarestudios/simplyseek/model/CategoryCatalogTest.kt).
  - Updated [`AS_BUILT.md`](AS_BUILT.md) living documentation with Section 4.5.

---

## [0.12.0] - 2026-10-30

### Added
- **Permanent Word Highlighting & Discovered Word Tracking**:
  - Multi-color palette ([`FoundWordColors`](class://com.squarestudios.simplyseek.ui.components.FoundWordColors)) assigning each discovered word a distinct neon highlight (emerald green, electric cyan, lemon gold, neon pink, lavender, orange, etc.).
  - Persistent glowing rounded capsule highlight and laser core connecting discovered words on the board canvas across all selection styles.
  - Interactive target word chips at the bottom of [`GameBoardScreen`](class://com.squarestudios.simplyseek.ui.screens.GameBoardScreenKt#GameBoardScreen) that light up with matching neon colors, checkmarks (`✓`), and strikethroughs upon discovery.
  - Integrated `LinearProgressIndicator` tracking discovery progress (`X / Y` words found).
  - Puzzle completion celebration card with "Play Next Puzzle" generator.
  - "New Puzzle" generator button and dynamic screen-width auto-scaling with `BoxWithConstraints`.
  - Created [`AS_BUILT.md`](AS_BUILT.md) comprehensive living architectural specification and systems reference document.

### Fixed
- Fixed bug where `SnapshotStateList` mutations did not trigger `remember` recalculation in [`InteractiveWordSearchGrid`](class://com.squarestudios.simplyseek.ui.components.WordSearchGridKt#InteractiveWordSearchGrid), causing discovered words to lose highlight on the board.
- Added `LocalInspectionMode.current` check to [`AdMobBanner`](class://com.squarestudios.simplyseek.ads.AdMobBannerKt#AdMobBanner) to fix preview render crashes in Android Studio.

---

## [0.11.0] - 2026-10-28

### Added
- **Multi-Style Selection System**:
  - Added `SelectionStyle` enum in [`WordSearchGrid.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt):
    - `ILLUMINATED_TILES`: Individual tiles light up with neon fill (`0x4D00E5FF`) and border (`0xFF00E5FF`), removing the thick highlighter line over text.
    - `LASER_LINE`: Thin 4.dp sharp neon laser beam + target ring beneath finger.
    - `CAPSULE_OUTLINE`: Hollow neon rounded outline frame.
    - `CLASSIC_HIGHLIGHT`: Translucent thick line.
  - Added color state animations (`animateColorAsState`) for smooth tile lighting transitions.

---

## [0.10.0] - 2026-10-28

### Added
- **Firebase Firestore Global Leaderboards**:
  - Integrated `com.google.firebase:firebase-firestore-ktx` via Firebase BoM (`33.10.0`).
  - Created [`LeaderboardEntry.kt`](app/src/main/java/com/squarestudios/simplyseek/leaderboard/LeaderboardEntry.kt) data model storing `playerName`, `timeInSeconds`, `difficulty`, and `timestamp`.
  - Created [`LeaderboardRepository.kt`](app/src/main/java/com/squarestudios/simplyseek/leaderboard/LeaderboardRepository.kt):
    - `submitScore(...)`: Pushes player completion time and difficulty to the `"leaderboards"` collection.
    - `getTopScoresFlow(...)`: Returns a real-time Kotlin `Flow` querying the top 50 fastest times ordered by lowest time first for a given difficulty.

---

## [0.9.0] - 2026-10-28

### Added
- **Google Play Billing Integration**:
  - Added `com.android.billingclient:billing-ktx:7.1.1` dependency.
  - Implemented [`BillingManager.kt`](app/src/main/java/com/squarestudios/simplyseek/billing/BillingManager.kt) to manage the `premium_remove_ads` one-time in-app purchase.
  - Handles `BillingClient` setup, connection lifecycle, querying product details, launching purchase flows, and acknowledging purchases.
  - Exposes reactive `isRemoveAdsPurchased: StateFlow<Boolean>` state to dynamically show or hide AdMob banners across the app.

---

## [0.8.0] - 2026-10-28

### Added
- **Google AdMob Integration**:
  - Integrated `com.google.android.gms:play-services-ads:23.6.0`.
  - Added [`SimplySeekApplication.kt`](app/src.main/java/com/squarestudios/simplyseek/SimplySeekApplication.kt) initializing `MobileAds.initialize(this)`.
  - Created [`AdMobBanner.kt`](app/src/main/java/com/squarestudios/simplyseek/ads/AdMobBanner.kt) wrapping `AndroidView` and `AdView` for banner ads at the bottom of the screen.
  - Implemented [`InterstitialAdManager.kt`](app/src/main/java/com/squarestudios/simplyseek/ads/InterstitialAdManager.kt) to preload and present full-screen interstitial ads upon puzzle completion.
  - Added AdMob Application ID metadata and permissions to [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml).

---

## [0.7.0] - 2026-10-28

### Added
- **Jetpack Compose Navigation System**:
  - Integrated `androidx.navigation:navigation-compose` in [`AppNavHost.kt`](app/src/main/java/com/squarestudios/simplyseek/navigation/AppNavHost.kt).
  - Defined routes in [`Screen.kt`](app/src/main/java/com/squarestudios/simplyseek/navigation/Screen.kt): `Home`, `CategorySelect`, `GameBoard`, `Stats`.
  - Implemented navigation argument passing (`category` and `difficulty`) from `CategorySelect` to `GameBoard`.
  - Built dark "tech fluid" UI screens:
    - [`HomeScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt): Welcome screen & menu.
    - [`CategorySelectScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/CategorySelectScreen.kt): Category & difficulty selector.
    - [`GameBoardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt): Interactive Word Search game screen with live word progress.
    - [`StatsScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/StatsScreen.kt): Player statistics & streak display.
  - Wired `AppNavHost` inside [`MainActivity.kt`](app/src/main/java/com/squarestudios/simplyseek/MainActivity.kt).

---

## [0.6.0] - 2026-10-28

### Added
- **Selection Matching & Permanent Highlight System**:
  - Created [`WordSearchSelectionManager.kt`](app/src/main/java/com/squarestudios/simplyseek/logic/WordSearchSelectionManager.kt) to extract letter strings along straight lines (horizontal, vertical, diagonal) and match them against target words (both forward and reverse).
  - Integrated `onDragEnd` matching logic inside `InteractiveWordSearchGrid`.
  - Added permanent neon green highlights (`Color(0x9900E676)`) on Canvas overlay for all discovered `FoundWord` objects.
  - Automatically clears temporary drag selection lines upon release if no match is found.
- **Selection Manager Unit Tests** in [`WordSearchSelectionManagerTest.kt`](app/src/test/java/com/squarestudios/simplyseek/logic/WordSearchSelectionManagerTest.kt):
  - Verified forward, reverse, and diagonal word extraction and matching accuracy.

---

## [0.5.0] - 2026-10-28

### Added
- **Swipe-to-Select Canvas Overlay** in [`WordSearchGrid.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt):
  - Created `@Composable fun InteractiveWordSearchGrid` overlaying a Jetpack Compose `Canvas` on top of the text grid.
  - Implemented `Modifier.pointerInput` with `detectDragGestures` to detect drag gestures.
  - Calculates starting cell row/col and center position on drag start based on cell size (`40.dp`).
  - Dynamically draws a thick semi-transparent cyan selection line (`Color(0x9900E5FF)`) with `StrokeCap.Round` connecting cell centers.
  - Implemented 8-way angle snapping (`atan2` + `PI / 4` rounding) ensuring lines strictly snap to horizontal, vertical, or 45° diagonal vectors.
  - Invokes `onSelectionComplete(startCell, endCell)` callback when drag gesture completes.

---

## [0.4.0] - 2026-10-28

### Added
- **Jetpack Compose Word Search Grid UI** in [`WordSearchGrid.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt):
  - Added `@Composable fun WordSearchGrid` to render a 2D list of `Cell` objects.
  - Implemented dark "tech fluid" aesthetic with dark obsidian background, cyan monospace typography, and neon gradient container borders.
  - Rendered square cell tiles (`CellTile`) with configurable dimensions (default `40.dp`).
  - Created Compose Preview `@Composable fun WordSearchGridPreview()`.

---

## [0.3.0] - 2026-10-28

### Added
- **Room Database Integration** in [`com.squarestudios.simplyseek.db`](app/src/main/java/com/squarestudios/simplyseek/db/AppDatabase.kt):
  - `SavedGame` Entity: Persists serialized 2D grid (`List<List<Cell>>`), target words (`List<String>`), elapsed time, and difficulty level.
  - `UserStats` Entity: Tracks total XP, total puzzles completed, and current daily streak.
  - `SavedGameDao` & `UserStatsDao`: Standard DAOs providing reactive `Flow` and coroutine `suspend` methods for insert, update, read, and delete operations.
  - `Converters`: TypeConverters converting `List<List<Cell>>` and `List<String>` to/from JSON strings via Gson.
  - `AppDatabase`: Abstract RoomDatabase singleton instance.
- **Room TypeConverters Unit Tests** in [`ConvertersTest.kt`](app/src/test/java/com/squarestudios/simplyseek/db/converter/ConvertersTest.kt).

---

## [0.2.0] - 2026-10-28

### Added
- **Procedural Word Search Generator** in [`WordSearchGenerator.kt`](app/src/main/java/com/squarestudios/simplyseek/generator/WordSearchGenerator.kt):
  - Procedurally places words in 8 directions (Horizontal, Vertical, Diagonal; forward and backward).
  - Handles character overlaps when intersecting words share identical letters.
  - Fills remaining empty spaces with random uppercase letters ('A'..'Z').
  - Returns populated 2D `List<List<Cell>>` array and `GridState`.
- **Generator Unit Tests** in [`WordSearchGeneratorTest.kt`](app/src/test/java/com/squarestudios/simplyseek/generator/WordSearchGeneratorTest.kt):
  - Verified 10x10 and custom grid size generation, letter bounds, and searchability of placed words.

---

## [0.1.0] - 2026-10-28

### Added
- **Core Data Models** in [`com.squarestudios.simplyseek.model`](app/src.main/java/com/squarestudios/simplyseek/model/WordSearchModels.kt):
  - `Cell`: Represents a character at a specific row and column (`char: Char`, `rowIndex: Int`, `colIndex: Int`).
  - `FoundWord`: Tracks a found word on the grid (`word: String`, `startCell: Cell`, `endCell: Cell`).
  - `GridState`: Holds the 2D grid (`List<List<Cell>>`), target words (`List<String>`), and discovered words (`List<FoundWord>`).
- **Unit Tests** in [`com.squarestudios.simplyseek.model.WordSearchModelsTest`](app/src/test/java/com/squarestudios/simplyseek/model/WordSearchModelsTest.kt) verifying creation and state integrity of all core data models.
- **Changelog**: Initialized `CHANGELOG.md` for project version tracking.
