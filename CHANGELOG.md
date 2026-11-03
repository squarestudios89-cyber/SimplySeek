# Changelog

All notable changes to the **SimplySeek** (Word Search) Android project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.19.0] - 2026-11-03

### Added
- **Live Global Leaderboards & Google Authentication Integration**:
  - Implemented [`AuthRepository.kt`](app/src/main/java/com/squarestudios/simplyseek/auth/AuthRepository.kt) leveraging Android's official Credential Manager (`androidx.credentials`, `googleid`, and `GoogleAuthProvider`) for one-tap Google Sign-In and player session tracking.
  - Applied `com.google.gms.google-services` plugin and integrated `google-services.json`, linking SimplySeek with the Firebase backend project (`simplyseek`).
  - Added dedicated destination screen [`LeaderboardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/LeaderboardScreen.kt) (`Screen.Leaderboard`) displaying real-time Firestore leaderboards partitioned by difficulty tier (`EASY`, `MEDIUM`, `HARD`).
  - Styled high-contrast cybernetic leaderboard cards with podium rank badges (Gold, Silver, Bronze), player avatars loaded via Coil Compose (`AsyncImage`), verified Google indicators, and formatted solve times (`01:24`).
  - Added an active game stopwatch timer to [`GameBoardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt), with direct **"🏆 SUBMIT SCORE TO LEADERBOARD"** and **"VIEW LEADERBOARDS"** actions upon puzzle completion.
  - Added a prominent **"🏆 GLOBAL LEADERBOARDS"** direct action button to [`HomeScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt).
  - Enhanced [`LeaderboardRepository.kt`](app/src/main/java/com/squarestudios/simplyseek/leaderboard/LeaderboardRepository.kt) and [`LeaderboardEntry.kt`](app/src/main/java/com/squarestudios/simplyseek/leaderboard/LeaderboardEntry.kt) to associate player IDs, avatar URLs, and categories with leaderboard scores.
  - Added unit test suite in [`LeaderboardEntryTest.kt`](app/src/test/java/com/squarestudios/simplyseek/leaderboard/LeaderboardEntryTest.kt).

---

## [0.18.0] - 2026-11-03

### Added
- **Massive Word Pool Expansion Across All Categories (1,600+ Curated Words)**:
  - Substantially deepened word pools across all **26 categories** in [`CategoryCatalog.kt`](app/src/main/java/com/squarestudios/simplyseek/model/CategoryCatalog.kt), increasing every category to **60–80 curated words each** (bringing total catalog capacity to over **1,600+ words**).
  - Delivers extended gameplay longevity per topic: players can solve 10+ non-overlapping puzzles in any single category, with millions of unique procedural combinations across Easy, Medium, and Hard tiers.
  - Verified that all words strictly contain uppercase alphabetic characters (`A`..`Z`) and comply with the $\le 10$ character limit for mobile screen grids.
  - Updated home screen teaser badge in [`HomeScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt) to `26 TOPICS & 1,600+ WORDS READY`.
  - Added automated catalog audit test `testWordPoolDepthAndLengthLimits` in [`CategoryCatalogTest.kt`](app/src/test/java/com/squarestudios/simplyseek/model/CategoryCatalogTest.kt).

---

## [0.17.0] - 2026-11-03

### Added
- **Tactile Haptic Feedback Engine for Word Circling & Tile Traversal**:
  - Implemented [`GameHapticFeedback.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/haptics/GameHapticFeedback.kt) and `HapticFeedbackController` interface to deliver physical tactile sensations as players interact with the word search grid:
    - **Tile Selection Ticks**: As the player drags or circles over the letters in [`InteractiveWordSearchGrid`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt), each newly spanned grid tile fires an immediate, subtle tactile tick (`SEGMENT_TICK` / `TEXT_HANDLE_MOVE` / `CLOCK_TICK`). A 35ms debounce threshold prevents high-frequency boundary jitter while preserving high-speed swipe accuracy.
    - **Touchdown Feedback**: Touching down on a starting letter emits a snappy gesture start tap (`GESTURE_START` / `KEYBOARD_TAP`).
    - **Word Discovered Confirmation**: Successfully identifying a valid target word delivers a distinct, rewarding multi-stage confirmation vibration (`CONFIRM` / waveform vibration).
  - Added `<uses-permission android:name="android.permission.VIBRATE" />` to [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml) with multi-tiered fallback to Android `Vibrator` and `VibratorManager` for seamless support across all Android devices and OEM variants.
  - Added unit test suite in [`GameHapticFeedbackTest.kt`](app/src/test/java/com/squarestudios/simplyseek/ui/haptics/GameHapticFeedbackTest.kt).

---

## [0.16.0] - 2026-11-03

### Added
- **Massive Category & Fandom Catalog Expansion (26 Categories, 1,000+ Curated Words)**:
  - Added new `CategoryGroup.MOVIES_TV("Movies & TV")` filter tab to [`CategoryCatalog.kt`](app/src/main/java/com/squarestudios/simplyseek/model/CategoryCatalog.kt).
  - Added 11 deep pop culture and fandom categories (35 to 50 curated words each, all verified $\le 10$ characters for mobile grid fit):
    - **Harry Potter** (`harry_potter`, 50 words): Hogwarts, Gryffindor, Slytherin, Ravenclaw, Hufflepuff, Dumbledore, Voldemort, Hermione, Quidditch, Snitch, Horcrux, Patronus, etc.
    - **The Big Bang Theory** (`big_bang_theory`, 44 words): Sheldon, Leonard, Penny, Howard, Raj, Amy, Bernadette, Bazinga, Caltech, Pasadena, String, Physics, etc.
    - **Lord of the Rings** (`lord_of_the_rings`, 40 words): Frodo, Aragorn, Gandalf, Legolas, Gimli, Sauron, Mordor, Rivendell, Gondor, Rohan, Gollum, etc.
    - **Star Wars** (`star_wars`, 40 words): Skywalker, Vader, Yoda, Kenobi, Solo, Chewbacca, Palpatine, Jedi, Sith, Lightsaber, Falcon, Tatooine, etc.
    - **The Office** (`the_office`, 40 words): Michael, Dwight, Jim, Pam, Andy, Stanley, Scranton, Dunder, Mifflin, Dundies, Sabre, Beet, etc.
    - **Friends** (`friends_tv`, 36 words): Ross, Rachel, Monica, Chandler, Joey, Phoebe, Central, Perk, Smelly, Geller, Tribbiani, Gunther, etc.
    - **Breaking Bad** (`breaking_bad`, 36 words): Heisenberg, Walter, Jesse, Pinkman, Gus, Fring, Saul, Goodman, Hank, Schrader, Pollos, Meth, etc.
    - **Stranger Things** (`stranger_things`, 35 words): Eleven, Mike, Dustin, Lucas, Will, Hopper, Joyce, Hawkins, Upside, Down, Demogorgon, Mindflayer, etc.
    - **Pokémon World** (`pokemon_world`, 39 words): Pikachu, Charizard, Blastoise, Venusaur, Mewtwo, Gengar, Eevee, Snorlax, Lucario, Kanto, Pokeball, etc.
    - **Anime Legends** (`anime_legends`, 38 words): Naruto, Sasuke, Goku, Vegeta, Luffy, Zoro, Tanjiro, Nezuko, Eren, Levi, Deku, Bakugo, Saitama, etc.
    - **Marvel Universe** (`marvel_universe`, 40 words): Iron Man, Captain, Thor, Hulk, Hawkeye, Natasha, Thanos, Wakanda, Asgard, Vibranium, Infinity, Avenger, etc.
  - Total catalog now contains **26 distinct categories** and **over 1,000+ words** enabling virtually limitless procedural puzzle generation.

### Changed
- **Active Puzzle Indexing & HUD Counter**:
  - Updated [`GameBoardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt) header to display the current sequential puzzle number (`DIFFICULTY: ${difficulty.uppercase()} • PUZZLE #${puzzleSeed + 1}`), providing visible progress tracking across endless replays within any selected theme.
  - Updated [`HomeScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt) footer badge to read `26 TOPICS & 1,000+ WORDS READY`.
  - Expanded [`CategoryCatalogTest.kt`](app/src/test/java/com/squarestudios/simplyseek/model/CategoryCatalogTest.kt) to validate >= 25 categories, fandom IDs, `MOVIES_TV` filtering, and keyword presence.

---

## [0.15.1] - 2026-11-03

### Added
- **Expanded Puzzle Word Counts & Dense Placement Engine**:
  - Increased target words per puzzle across all difficulty tiers:
    - **Easy**: **6 words** on 8x8 grid.
    - **Medium**: **8 words** on 9x9 grid (up from 6).
    - **Hard**: **13 words** on 10x10 grid (up from 8, delivering dense 12–14 word puzzles).
  - Enhanced [`WordSearchGenerator.kt`](app/src/main/java/com/squarestudios/simplyseek/generator/WordSearchGenerator.kt) with an intelligent fallback scan across shuffled matrix coordinates, ensuring dense 12–14 word puzzles with intersecting characters place reliably without skipping words.
  - Expanded all 15 category word pools in [`CategoryCatalog.kt`](app/src/main/java/com/squarestudios/simplyseek/model/CategoryCatalog.kt) to 28–32 words each (bringing the catalog to over 430+ words total).
  - Updated difficulty tier HUD cards in [`CategorySelectScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/CategorySelectScreen.kt) to display `8x8 • 6W`, `9x9 • 8W`, and `10x10 • 13W`.
  - Added unit test `testDenseWordPlacement` in [`WordSearchGeneratorTest.kt`](app/src/test/java/com/squarestudios/simplyseek/generator/WordSearchGeneratorTest.kt).

---

## [0.15.0] - 2026-11-03

### Fixed
- **System Navigation Bar & Insets Alignment**:
  - Added `enableEdgeToEdge()` in [`MainActivity.kt`](app/src/main/java/com/squarestudios/simplyseek/MainActivity.kt) and applied `Modifier.safeDrawingPadding()` across all screens ([`CategorySelectScreen`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/CategorySelectScreen.kt), [`HomeScreen`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt), [`GameBoardScreen`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt), and [`StatsScreen`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/StatsScreen.kt)).
  - Fixed issue where the bottom action button ("START PUZZLE" on CategorySelectScreen) and AdMob banner overlapped the phone's 3-button Home/Back navigation bar, preventing inadvertent exits to the phone's launcher.

### Changed
- **Mobile Grid Sizing & Font Legibility Overhaul**:
  - Adjusted phone grid sizing tiers to keep cells large and comfortable on mobile screens:
    - **Easy**: `8x8` grid (5 words, max length 8) — large ~40dp tiles, 23sp bold font.
    - **Medium**: `9x9` grid (6 words, max length 9) — spacious ~36dp tiles, 21sp bold font (replaced cramped 10x10).
    - **Hard**: `10x10` grid (8 words, max length 10) — comfortable ~32dp tiles, 19sp bold font (replaced unplayable 12x12 on phones).
  - Reclaimed over 60.dp of horizontal screen real-estate by eliminating redundant padding in [`InteractiveWordSearchGrid`](app/src/main/java/com/squarestudios/simplyseek/ui/components/WordSearchGrid.kt) (container padding reduced to 6.dp, cell spacing reduced from 4.dp to 2.dp).
  - Boosted cell font scaling factor from `0.45` to `0.58` with `FontWeight.ExtraBold`, making letters nearly 2x larger on screen.
  - Refined touch coordinate math in `getCellCoordinates` with symmetric center-based rounding for forgiving and accurate 8-directional word selection.

---

## [0.14.1] - 2026-11-03

### Fixed
- **Startup Crash Resolution (Room Database Annotation Processor)**:
  - Fixed `RuntimeException: Cannot find implementation for com.squarestudios.simplyseek.db.AppDatabase. AppDatabase_Impl does not exist` occurring immediately on app startup.
  - Replaced Java's `annotationProcessor(libs.room.compiler)` with Kotlin's KAPT `kapt(libs.room.compiler)` in [`app/build.gradle.kts`](app/build.gradle.kts) and applied `org.jetbrains.kotlin.kapt` in [`build.gradle.kts`](build.gradle.kts).
  - Ensured Room generates `AppDatabase_Impl` for all Kotlin `@Database` and `@Dao` definitions, restoring flawless app startup and Room database initialization.

---

## [0.14.0] - 2026-11-03

### Added
- **Home Dashboard & Live User Progression System**:
  - Upgraded [`HomeScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/HomeScreen.kt) into an interactive player dashboard backed by Room database Kotlin Flows (`observeUserStats`).
  - Added **Operator Level & Rank** badges (`Novice Seeker`, `Grid Apprentice`, `Seek Specialist`, `Code Hunter`, `Grid Overlord`, `Cyber Grandmaster`) with real-time level progress bars.
  - Implemented neon stats metric cards:
    - **Puzzles Solved** (Emerald neon)
    - **Daily Streak Tracker** (Tangerine flame with daily date validation)
    - **Mastery Tier** (Violet lavender)
  - Created [`UserStatsRepository.kt`](app/src/main/java/com/squarestudios/simplyseek/db/repository/UserStatsRepository.kt) encapsulating XP rewards (+50 to +150 XP based on difficulty tier), puzzle counts, and streak updates.
  - Wired puzzle completion events in [`GameBoardScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/GameBoardScreen.kt) to record progression directly into the Room database.
  - Upgraded [`StatsScreen.kt`](app/src/main/java/com/squarestudios/simplyseek/ui/screens/StatsScreen.kt) to observe real-time player progression.
  - Added unit test suite [`UserStatsRepositoryTest.kt`](app/src/test/java/com/squarestudios/simplyseek/db/repository/UserStatsRepositoryTest.kt).
  - Updated [`AS_BUILT.md`](AS_BUILT.md) with Section 4.6 and maintenance log.

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
