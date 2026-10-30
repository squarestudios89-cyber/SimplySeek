package com.squarestudios.simplyseek.model

import kotlin.random.Random

/**
 * Filter groups for categorizing puzzle topics in the catalog.
 */
enum class CategoryGroup(val label: String) {
    ALL("All"),
    TECH_GAMING("Tech & Gaming"),
    AUTOMOTIVE("Automotive"),
    POP_CULTURE("Pop Culture"),
    SCIENCE_NATURE("Science & Nature"),
}

/**
 * Represents a playable word search category.
 *
 * @property id Unique programmatic key (e.g., "computer_hardware").
 * @property title Human-readable category title.
 * @property icon Visual emoji or icon indicator.
 * @property group Classification filter group.
 * @property description Subtitle previewing word types.
 * @property wordPool Comprehensive list of candidate words for procedural puzzle generation.
 */
data class WordCategory(
    val id: String,
    val title: String,
    val icon: String,
    val group: CategoryGroup,
    val description: String,
    val wordPool: List<String>,
)

/**
 * Central repository of rich word categories and procedurally sampled word banks.
 */
object CategoryCatalog {

    val allCategories: List<WordCategory> = listOf(
        // 1. Computer Hardware & Tech Components
        WordCategory(
            id = "computer_hardware",
            title = "Computer Hardware",
            icon = "💻",
            group = CategoryGroup.TECH_GAMING,
            description = "CPU, GPU, RAM, Motherboard, SSD...",
            wordPool = listOf(
                "CPU", "GPU", "RAM", "SSD", "CHIPSET", "MOTHERBOARD", "MONITOR",
                "KEYBOARD", "ROUTER", "COOLER", "HEATSINK", "FIRMWARE", "STORAGE",
                "OPTICAL", "POWER", "BUS", "CACHE", "TRANSISTOR", "SOCKET",
                "THERMAL", "FAN", "DISPLAY", "GRAPHICS", "MEMORY", "HARDDRIVE",
            ),
        ),

        // 2. Software & Coding
        WordCategory(
            id = "software_coding",
            title = "Software & Coding",
            icon = "⚡",
            group = CategoryGroup.TECH_GAMING,
            description = "Kotlin, Compiler, Function, Algorithm...",
            wordPool = listOf(
                "KOTLIN", "PYTHON", "JAVA", "COMPILER", "DEBUGGER", "DATABASE",
                "FUNCTION", "VARIABLE", "COROUTINE", "ALGORITHM", "RECURSION",
                "SYNTAX", "LIBRARY", "RUNTIME", "POINTER", "THREAD", "BINARY",
                "BOOLEAN", "ARRAY", "OBJECT", "INTERFACE", "BACKEND", "FRONTEND",
            ),
        ),

        // 3. Video Game Classics
        WordCategory(
            id = "gaming_classics",
            title = "Video Game Classics",
            icon = "🎮",
            group = CategoryGroup.TECH_GAMING,
            description = "Mario, Zelda, Pokemon, Skyrim, Portal...",
            wordPool = listOf(
                "MARIO", "ZELDA", "POKEMON", "METROID", "TETRIS", "PACMAN", "DOOM",
                "SONIC", "PORTAL", "FALLOUT", "SKYRIM", "MINECRAFT", "WITCHER",
                "CYBERPUNK", "WARCRAFT", "DIABLO", "HALFLIFE", "STARCRAFT", "RESIDENT",
                "QUAKE", "BIOSHOCK", "TERRARIA", "OVERWATCH", "APEX",
            ),
        ),

        // 4. PlayStation Hits
        WordCategory(
            id = "playstation_hits",
            title = "PlayStation Hits",
            icon = "🔷",
            group = CategoryGroup.TECH_GAMING,
            description = "God of War, Uncharted, Spiderman, Horizon...",
            wordPool = listOf(
                "GODWAR", "UNCHARTED", "SPIDERMAN", "BLOODBORNE", "HORIZON", "RATCHET",
                "CLANK", "TSUSHIMA", "INFAMOUS", "RETURNAL", "LASTOFUS", "TURISMO",
                "KILLZONE", "DAYSGONE", "RESISTANCE", "SOCOM", "JAK", "DAXTER",
                "ASTRO", "SHADOW", "SLYCOOPER", "KNACK",
            ),
        ),

        // 5. Xbox Franchises
        WordCategory(
            id = "xbox_franchises",
            title = "Xbox Franchises",
            icon = "🟩",
            group = CategoryGroup.TECH_GAMING,
            description = "Halo, Forza, Gears, Fable, Starfield...",
            wordPool = listOf(
                "HALO", "FORZA", "GEARS", "FABLE", "STARFIELD", "CRACKDOWN",
                "PERFECTDARK", "HELLBLADE", "BANJO", "KAZOOIE", "SEATHIEVES",
                "PSYCHONAUTS", "CONKER", "FUSION", "ORIX", "FALLOUT", "PREY",
                "AVOWED", "CONTABAND", "EVERWILD",
            ),
        ),

        // 6. Car Makes & Manufacturers
        WordCategory(
            id = "car_makes",
            title = "Car Makes",
            icon = "🏎️",
            group = CategoryGroup.AUTOMOTIVE,
            description = "Ferrari, Porsche, Toyota, BMW, Audi...",
            wordPool = listOf(
                "FERRARI", "PORSCHE", "LAMBORGHINI", "TOYOTA", "BMW", "MERCEDES",
                "AUDI", "HONDA", "NISSAN", "FORD", "CHEVROLET", "MCLAREN",
                "SUBARU", "MAZDA", "VOLKSWAGEN", "HYUNDAI", "BUGATTI", "JAGUAR",
                "BENTLEY", "VOLVO", "MASERATI", "PAGANI", "DODGE", "CORVETTE",
            ),
        ),

        // 7. Car Engine & Mechanical Parts
        WordCategory(
            id = "car_engine",
            title = "Car Engine Parts",
            icon = "🔧",
            group = CategoryGroup.AUTOMOTIVE,
            description = "Piston, Crankshaft, Turbo, Radiator...",
            wordPool = listOf(
                "PISTON", "CAMSHAFT", "CRANKSHAFT", "CYLINDER", "TURBO", "RADIATOR",
                "ALTERNATOR", "EXHAUST", "MANIFOLD", "SPARKPLUG", "INJECTOR",
                "CLUTCH", "FLYWHEEL", "VALVES", "GASKET", "STARTER", "CARBURETOR",
                "SUPERCHARGER", "INTERCOOLER", "HEADER", "TIMINGBELT", "OILPUMP",
            ),
        ),

        // 8. Cinema & Blockbusters
        WordCategory(
            id = "movies_cinema",
            title = "Cinema Blockbusters",
            icon = "🎬",
            group = CategoryGroup.POP_CULTURE,
            description = "Inception, Matrix, Interstellar, Avatar...",
            wordPool = listOf(
                "INCEPTION", "MATRIX", "INTERSTELLAR", "GLADIATOR", "AVATAR", "TITANIC",
                "TERMINATOR", "ALIEN", "PREDATOR", "GODFATHER", "JURASSIC", "CASABLANCA",
                "SCARFACE", "GOODFELLAS", "ROCKY", "STARWARS", "JAWS", "SHINING",
                "PULPFICTION", "BATMAN", "AVENGERS", "DUNE", "OPPENHEIMER",
            ),
        ),

        // 9. Comic Book Heroes & Characters
        WordCategory(
            id = "comic_characters",
            title = "Comic Book Heroes",
            icon = "🦸",
            group = CategoryGroup.POP_CULTURE,
            description = "Batman, Superman, Wolverine, Deadpool...",
            wordPool = listOf(
                "BATMAN", "SUPERMAN", "SPIDERMAN", "IRONMAN", "WOLVERINE", "THOR",
                "HULK", "DEADPOOL", "FLASH", "AQUAMAN", "DAREDEVIL", "CYCLOPS",
                "NIGHTWING", "ROBIN", "JOKER", "MAGNETO", "PUNISHER", "SHAZAM",
                "HAWKEYE", "SUPERGIRL", "CATWOMAN", "CARNAGE", "VENOM",
            ),
        ),

        // 10. Science & Quantum Physics
        WordCategory(
            id = "science_physics",
            title = "Science & Physics",
            icon = "🔬",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Atom, Gravity, Quantum, Relativity, Fusion...",
            wordPool = listOf(
                "ATOM", "ELECTRON", "NEUTRON", "PROTON", "GRAVITY", "QUANTUM",
                "PHOTON", "QUARK", "FUSION", "FISSION", "ENERGY", "KINETIC",
                "VELOCITY", "RELATIVITY", "ENTROPY", "MAGNETISM", "RADIATION",
                "PARTICLE", "THERMAL", "PLASMA", "COLLIDER", "MASS",
            ),
        ),

        // 11. Deep Space & Cosmos
        WordCategory(
            id = "deep_space",
            title = "Deep Space & Cosmos",
            icon = "🌌",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Galaxy, Nebula, Supernova, Black Hole...",
            wordPool = listOf(
                "GALAXY", "NEBULA", "SUPERNOVA", "BLACKHOLE", "ASTEROID", "COMET",
                "ANDROMEDA", "PULSAR", "QUASAR", "ORBIT", "ECLIPSE", "PLANET",
                "SATELLITE", "METEOR", "HORIZON", "TELESCOPE", "COSMOS", "AURORA",
                "SOLAR", "VOYAGER", "HUBBLE", "CLUSTER",
            ),
        ),

        // 12. Wildlife & Animals
        WordCategory(
            id = "wild_animals",
            title = "Wildlife & Animals",
            icon = "🦁",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Cheetah, Elephant, Leopard, Falcon, Dolphin...",
            wordPool = listOf(
                "CHEETAH", "ELEPHANT", "KANGAROO", "LEOPARD", "DOLPHIN", "OCTOPUS",
                "PENGUIN", "FALCON", "PLATYPUS", "CHAMELEON", "GIRAFFE", "GORILLA",
                "RHINOCEROS", "CROCODILE", "PANTHER", "BUFFALO", "ANTELOPE", "EAGLE",
                "JAGUAR", "WALRUS", "GAZELLE",
            ),
        ),

        // 13. World Geography & Landmarks
        WordCategory(
            id = "world_wonders",
            title = "World Geography",
            icon = "🗺️",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Everest, Amazon, Sahara, Nile, Pacific...",
            wordPool = listOf(
                "EVEREST", "AMAZON", "SAHARA", "NILE", "PACIFIC", "COLOSSEUM",
                "PYRAMIDS", "TAJMAHAL", "GLACIER", "VOLCANO", "CANYON", "ARCHIPELAGO",
                "ISLAND", "SAVANNA", "TUNDRA", "RAINFOREST", "ATLANTIC", "HIMALAYA",
                "DANUBE", "VICTORIA",
            ),
        ),

        // 14. Culinary & Food
        WordCategory(
            id = "food_culinary",
            title = "Culinary & Food",
            icon = "🍕",
            group = CategoryGroup.POP_CULTURE,
            description = "Pizza, Sushi, Burger, Ramen, Croissant...",
            wordPool = listOf(
                "PIZZA", "BURGER", "PASTA", "SUSHI", "TACO", "CROISSANT", "LASAGNA",
                "RAMEN", "RISOTTO", "BURRITO", "CHOCOLATE", "WAFFLE", "FALAFEL",
                "GELATO", "TIRAMISU", "PANCAKE", "BAGEL", "DUMPLING", "PRETZEL",
                "BARBECUE", "ESPRESSO",
            ),
        ),

        // 15. Rock Legends & Bands
        WordCategory(
            id = "music_rock",
            title = "Rock Legends",
            icon = "🎸",
            group = CategoryGroup.POP_CULTURE,
            description = "Beatles, Queen, Nirvana, Metallica, Pink Floyd...",
            wordPool = listOf(
                "BEATLES", "QUEEN", "NIRVANA", "METALLICA", "ACDC", "PINKFLOYD",
                "LEDZEPPELIN", "AEROSMITH", "RADIOHEAD", "COLDPLAY", "BONJOVI",
                "JOURNEY", "KISS", "RAMONES", "SLIPKNOT", "GENESIS", "DEFLEPPARD",
                "SCORPIONS", "VANHALEN",
            ),
        ),
    )

    /**
     * Filters categories by [CategoryGroup].
     */
    fun getCategoriesByGroup(group: CategoryGroup): List<WordCategory> {
        return if (group == CategoryGroup.ALL) {
            allCategories
        } else {
            allCategories.filter { it.group == group }
        }
    }

    /**
     * Finds a category by its ID or case-insensitive title match.
     */
    fun getCategoryById(identifier: String): WordCategory? {
        val lower = identifier.trim().lowercase()
        return allCategories.find {
            it.id.equals(lower, ignoreCase = true) ||
                it.title.equals(lower, ignoreCase = true) ||
                it.id.replace('_', ' ').equals(lower, ignoreCase = true)
        }
    }

    /**
     * Samples a randomized subset of words for a game puzzle based on difficulty and seed.
     *
     * @param categoryId Category ID or title.
     * @param difficulty "Easy", "Medium", or "Hard".
     * @param seed Integer seed to deterministically shuffle the pool for "New Puzzle" buttons.
     */
    fun getWordsForCategory(
        categoryId: String,
        difficulty: String,
        seed: Int = 0,
    ): List<String> {
        val category = getCategoryById(categoryId) ?: allCategories.first()

        val (maxWordLength, wordCount) = when (difficulty.lowercase()) {
            "easy" -> Pair(8, 5)
            "hard" -> Pair(12, 9)
            else -> Pair(10, 7) // Medium
        }

        // Filter words that fit on the grid dimension
        val eligibleWords = category.wordPool
            .filter { it.length <= maxWordLength }
            .distinct()

        // Deterministic shuffle using seed so repeating the seed reproduces the same puzzle
        val rng = Random(category.id.hashCode() + seed * 31 + difficulty.hashCode())
        val shuffled = eligibleWords.shuffled(rng)

        return shuffled.take(wordCount)
    }
}
