package com.squarestudios.simplyseek.model

import kotlin.random.Random

/**
 * Filter groups for categorizing puzzle topics in the catalog.
 */
enum class CategoryGroup(val label: String) {
    ALL("All"),
    MOVIES_TV("Movies & TV"),
    TECH_GAMING("Tech & Gaming"),
    POP_CULTURE("Pop Culture"),
    AUTOMOTIVE("Automotive"),
    SCIENCE_NATURE("Science & Nature"),
}

/**
 * Represents a playable word search category.
 *
 * @property id Unique programmatic key (e.g., "harry_potter").
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
        // ==========================================
        // 1. MOVIES & TV FANDOMS
        // ==========================================

        // 1. Harry Potter & Wizarding World
        WordCategory(
            id = "harry_potter",
            title = "Harry Potter",
            icon = "🧙‍♂️",
            group = CategoryGroup.MOVIES_TV,
            description = "Hogwarts, Spells, Houses, Characters, Magical Items...",
            wordPool = listOf(
                "HARRY", "POTTER", "HERMIONE", "RON", "SNAPE", "VOLDEMORT", "HAGRID",
                "DRACO", "SIRIUS", "DOBBY", "NEVILLE", "LUNA", "LUPIN", "GINNY",
                "FRED", "GEORGE", "PERCY", "CEDRIC", "BELLATRIX", "MALFOY", "HOGWARTS",
                "GRYFFINDOR", "SLYTHERIN", "RAVENCLAW", "HUFFLEPUFF", "SNITCH", "NIMBUS",
                "PATRONUS", "HORCRUX", "LUMOS", "ACCIO", "ALOHOMORA", "AZKABAN",
                "HOGSMEADE", "DIAGON", "HEDWIG", "PENSIEVE", "MANDRAKE", "BASILISK",
                "DEMENTOR", "PHOENIX", "GOBLET", "SORTING", "ELDERWAND", "CLOAK",
                "STONE", "GALLEON", "FLOO", "THESTRAL", "BOGGART",
            ),
        ),

        // 2. The Big Bang Theory
        WordCategory(
            id = "big_bang_theory",
            title = "Big Bang Theory",
            icon = "⚛️",
            group = CategoryGroup.MOVIES_TV,
            description = "Sheldon, Leonard, Penny, Bazinga, Caltech, Physics...",
            wordPool = listOf(
                "SHELDON", "LEONARD", "PENNY", "HOWARD", "RAJ", "AMY", "BERNADETTE",
                "STUART", "BAZINGA", "CALTECH", "PHYSICS", "STRING", "COOPER",
                "WOLOWITZ", "CHEESECAKE", "TIARA", "SOFTKITTY", "ROOMMATE", "SPOT",
                "MEEMAW", "WHITEBOARD", "LASER", "ASTRONAUT", "KLINGON", "COMIC",
                "FLASH", "DOPPLER", "TEXAS", "NEBRASKA", "ELEVATOR", "APARTMENT",
                "HALO", "NOBEL", "QUANTUM", "PROTON", "BEVERLY", "KRIPKE",
                "CINNAMON", "BOWTIE", "SCIENCE", "GAMING", "TREK", "STARGATE",
            ),
        ),

        // 3. Lord of the Rings & Middle Earth
        WordCategory(
            id = "lord_of_the_rings",
            title = "Lord of the Rings",
            icon = "💍",
            group = CategoryGroup.MOVIES_TV,
            description = "Frodo, Gandalf, Aragorn, Sauron, Shire, Mordor...",
            wordPool = listOf(
                "FRODO", "GANDALF", "ARAGORN", "LEGOLAS", "GIMLI", "SAMWISE", "SAURON",
                "GOLLUM", "BILBO", "BOROMIR", "MERRY", "PIPPIN", "ARWEN", "GALADRIEL",
                "ELROND", "SARUMAN", "NAZGUL", "SHIRE", "MORDOR", "RIVENDELL", "GONDOR",
                "ROHAN", "ISENGARD", "PALANTIR", "MITHRIL", "ANDURIL", "STING",
                "FELLOWSHIP", "HOBBIT", "ORC", "BALROG", "SMAUG", "TREEBEARD",
                "HELMSDEEP", "EOWYN", "FARAMIR", "DENETHOR", "PRECIOUS", "RINGS", "DWARF",
            ),
        ),

        // 4. Star Wars Universe
        WordCategory(
            id = "star_wars",
            title = "Star Wars",
            icon = "⚔️",
            group = CategoryGroup.MOVIES_TV,
            description = "Jedi, Sith, Skywalker, Vader, Yoda, Grogu, Lightsaber...",
            wordPool = listOf(
                "LUKE", "SKYWALKER", "VADER", "YODA", "KENOBI", "HANSOLO", "CHEWBACCA",
                "LEIA", "PALPATINE", "BOBAFETT", "GROGU", "AHSOKA", "DARTHMAUL", "WINDU",
                "ANAKIN", "PADME", "KYLOREN", "REY", "LIGHTSABER", "JEDI", "SITH",
                "DEATHSTAR", "TATOOINE", "CORUSCANT", "NABOO", "ENDOR", "HOTH", "DAGOBAH",
                "XWING", "DROID", "R2D2", "C3PO", "BB8", "WOOKIEE", "MANDALOR", "FORCE",
                "EMPIRE", "REBEL", "BLASTER", "BESKAR",
            ),
        ),

        // 5. The Office (US)
        WordCategory(
            id = "the_office",
            title = "The Office",
            icon = "📄",
            group = CategoryGroup.MOVIES_TV,
            description = "Michael, Dwight, Jim, Pam, Scranton, Dundie, Paper...",
            wordPool = listOf(
                "MICHAEL", "DWIGHT", "JIM", "PAM", "RYAN", "ANDY", "STANLEY", "KEVIN",
                "ANGELA", "OSCAR", "PHYLLIS", "TOBY", "CREED", "MEREDITH", "KELLY",
                "DARRYL", "JAN", "ERIN", "DUNDIE", "SCRANTON", "BEETFARM", "SABRE",
                "PAPER", "CHILI", "PRETZEL", "SCHRUTE", "HALPERT", "BEESLY", "SCOTT",
                "MANAGER", "SALES", "WAREHOUSE", "BRANCH", "THREAT", "DESK", "JELLO",
                "STAPLER", "PRANK", "DUNDER", "COUPLE",
            ),
        ),

        // 6. Friends Sitcom
        WordCategory(
            id = "friends_tv",
            title = "Friends Sitcom",
            icon = "☕",
            group = CategoryGroup.MOVIES_TV,
            description = "Rachel, Monica, Phoebe, Joey, Chandler, Ross, Central Perk...",
            wordPool = listOf(
                "RACHEL", "MONICA", "PHOEBE", "JOEY", "CHANDLER", "ROSS", "CENTRAL",
                "PERK", "SMELLYCAT", "PIVOT", "UNAGI", "GUNTHER", "JANICE", "GELLER",
                "TRIBBIANI", "BUFFAY", "BING", "GREEN", "COFFEE", "COUCH", "FOUNTAIN",
                "LONDON", "VEGAS", "TURKEY", "MARCEL", "DUCK", "CHICK", "BARISTA",
                "HUGSY", "DINOSAUR", "WEDDING", "APARTMENT", "THANKS", "MONKEY", "LOBSTER",
            ),
        ),

        // 7. Breaking Bad & Better Call Saul
        WordCategory(
            id = "breaking_bad",
            title = "Breaking Bad",
            icon = "⚗️",
            group = CategoryGroup.MOVIES_TV,
            description = "Walter, Heisenberg, Jesse, Saul, Gus, Los Pollos...",
            wordPool = listOf(
                "WALTER", "WHITE", "HEISENBERG", "JESSE", "PINKMAN", "SAUL", "GOODMAN",
                "GUS", "FRING", "MIKE", "HANK", "SCHRADER", "MARIE", "SKYLER", "FLYNN",
                "LOSPOLLOS", "BLUESKY", "CARTEL", "CHEMISTRY", "METH", "LAB", "LAWYER",
                "SLIPPIN", "KIMWEXLER", "HOWARD", "LALO", "NACHO", "HECTOR", "BELL",
                "SALAMANCA", "BADGER", "SKINNYPETE", "POLLOS", "DESERT", "BARREL",
            ),
        ),

        // 8. Stranger Things
        WordCategory(
            id = "stranger_things",
            title = "Stranger Things",
            icon = "🧇",
            group = CategoryGroup.MOVIES_TV,
            description = "Eleven, Mike, Dustin, Hopper, Upside Down, Hawkins...",
            wordPool = listOf(
                "ELEVEN", "MIKE", "DUSTIN", "LUCAS", "WILL", "MAX", "HOPPER", "JOYCE",
                "STEVE", "NANCY", "ROBIN", "JONATHAN", "EDDIE", "DEMOGORGON",
                "MINDFLAYER", "VECNA", "HAWKINS", "STARCOURT", "EGGO", "HELLFIRE",
                "BYERS", "WHEELER", "HENDERSON", "SINCLAIR", "MAYFIELD", "ARCADE",
                "CREEL", "WALKIE", "DUNGEONS", "WAFFLES", "MALL", "RUSSIAN", "SHADOW",
            ),
        ),

        // 9. Cinema Blockbusters
        WordCategory(
            id = "movies_cinema",
            title = "Cinema Blockbusters",
            icon = "🎬",
            group = CategoryGroup.MOVIES_TV,
            description = "Inception, Matrix, Interstellar, Avatar, Titanic...",
            wordPool = listOf(
                "INCEPTION", "MATRIX", "GLADIATOR", "AVATAR", "TITANIC", "TERMINATOR",
                "ALIEN", "PREDATOR", "GODFATHER", "JURASSIC", "CASABLANCA", "SCARFACE",
                "GOODFELLAS", "ROCKY", "STARWARS", "JAWS", "SHINING", "BATMAN",
                "AVENGERS", "DUNE", "HEAT", "FARGO", "SEVEN", "SPEED", "ALIENS", "GLORY",
            ),
        ),

        // ==========================================
        // 2. TECH & GAMING
        // ==========================================

        // 10. Computer Hardware & Tech Components
        WordCategory(
            id = "computer_hardware",
            title = "Computer Hardware",
            icon = "💻",
            group = CategoryGroup.TECH_GAMING,
            description = "CPU, GPU, RAM, Motherboard, SSD, Heatsink...",
            wordPool = listOf(
                "CPU", "GPU", "RAM", "SSD", "CHIPSET", "MOTHERBOARD", "MONITOR",
                "KEYBOARD", "ROUTER", "COOLER", "HEATSINK", "FIRMWARE", "STORAGE",
                "OPTICAL", "POWER", "BUS", "CACHE", "TRANSISTOR", "SOCKET",
                "THERMAL", "FAN", "DISPLAY", "GRAPHICS", "MEMORY", "HARDDRIVE",
                "MODEM", "PORT", "PIXEL", "CLOCK", "DRIVER", "REGISTER",
            ),
        ),

        // 11. Software & Coding
        WordCategory(
            id = "software_coding",
            title = "Software & Coding",
            icon = "⚡",
            group = CategoryGroup.TECH_GAMING,
            description = "Kotlin, Compiler, Function, Algorithm, Coroutine...",
            wordPool = listOf(
                "KOTLIN", "PYTHON", "JAVA", "COMPILER", "DEBUGGER", "DATABASE",
                "FUNCTION", "VARIABLE", "COROUTINE", "ALGORITHM", "RECURSION",
                "SYNTAX", "LIBRARY", "RUNTIME", "POINTER", "THREAD", "BINARY",
                "BOOLEAN", "ARRAY", "OBJECT", "INTERFACE", "BACKEND", "FRONTEND",
                "CLASS", "LOOP", "BUILD", "INDEX", "STACK", "QUEUE",
            ),
        ),

        // 12. Video Game Classics
        WordCategory(
            id = "gaming_classics",
            title = "Video Game Classics",
            icon = "🎮",
            group = CategoryGroup.TECH_GAMING,
            description = "Mario, Zelda, Pokemon, Skyrim, Portal, Doom...",
            wordPool = listOf(
                "MARIO", "ZELDA", "POKEMON", "METROID", "TETRIS", "PACMAN", "DOOM",
                "SONIC", "PORTAL", "FALLOUT", "SKYRIM", "MINECRAFT", "WITCHER",
                "CYBERPUNK", "WARCRAFT", "DIABLO", "HALFLIFE", "STARCRAFT", "RESIDENT",
                "QUAKE", "BIOSHOCK", "TERRARIA", "OVERWATCH", "APEX",
                "CRASH", "SPYRO", "MEGAMAN", "CASTLE", "SIMS",
            ),
        ),

        // 13. PlayStation Hits
        WordCategory(
            id = "playstation_hits",
            title = "PlayStation Hits",
            icon = "🔷",
            group = CategoryGroup.TECH_GAMING,
            description = "God of War, Uncharted, Spiderman, Horizon, Ratchet...",
            wordPool = listOf(
                "GODWAR", "UNCHARTED", "SPIDERMAN", "BLOODBORNE", "HORIZON", "RATCHET",
                "CLANK", "TSUSHIMA", "INFAMOUS", "RETURNAL", "LASTOFUS", "TURISMO",
                "KILLZONE", "DAYSGONE", "RESISTANCE", "SOCOM", "JAK", "DAXTER",
                "ASTRO", "SHADOW", "SLYCOOPER", "KNACK", "GRAN", "MEDIEVIL",
                "WIPEOUT", "RESOGUN", "GHOST",
            ),
        ),

        // 14. Xbox Franchises
        WordCategory(
            id = "xbox_franchises",
            title = "Xbox Franchises",
            icon = "🟩",
            group = CategoryGroup.TECH_GAMING,
            description = "Halo, Forza, Gears, Fable, Starfield, Banjo...",
            wordPool = listOf(
                "HALO", "FORZA", "GEARS", "FABLE", "STARFIELD", "CRACKDOWN",
                "PERFECTDARK", "HELLBLADE", "BANJO", "KAZOOIE", "SEATHIEVES",
                "PSYCHONAUTS", "CONKER", "FUSION", "ORIX", "FALLOUT", "PREY",
                "AVOWED", "CONTABAND", "EVERWILD", "MECH", "QUANTUM", "RECORE",
                "STATE", "DECAY",
            ),
        ),

        // 15. Pokémon Universe
        WordCategory(
            id = "pokemon_world",
            title = "Pokémon Universe",
            icon = "⚡",
            group = CategoryGroup.TECH_GAMING,
            description = "Pikachu, Charizard, Mewtwo, Eevee, Gengar, Snorlax...",
            wordPool = listOf(
                "PIKACHU", "CHARIZARD", "BULBASAUR", "SQUIRTLE", "MEWTWO", "GENGAR",
                "EEVEE", "SNORLAX", "LUCARIO", "GYARADOS", "DRAGONITE", "BLASTOISE",
                "VENUSAUR", "RAYQUAZA", "ARCEUS", "LAPRAS", "ALAKAZAM", "MACHAMP",
                "MEW", "LUGIA", "HOOH", "UMBREON", "ESPEON", "POKEDEX", "POKEBALL",
                "TRAINER", "BADGE", "KANTO", "JOHTO", "HOENN", "SINNOH", "MISTY",
                "BROCK", "ONIX", "PSYDUCK", "TOGEPI", "MUDKIP", "TORCHIC",
            ),
        ),

        // ==========================================
        // 3. POP CULTURE
        // ==========================================

        // 16. Marvel Universe & Avengers
        WordCategory(
            id = "marvel_universe",
            title = "Marvel Universe",
            icon = "🛡️",
            group = CategoryGroup.POP_CULTURE,
            description = "Avengers, Ironman, Spiderman, Thor, Thanos, Wakanda...",
            wordPool = listOf(
                "IRONMAN", "THOR", "HULK", "SPIDERMAN", "NATASHA", "HAWKEYE", "THANOS",
                "LOKI", "WANDA", "VISION", "FALCON", "BUCKY", "ANTMAN", "WASP",
                "STRANGE", "PANTHER", "WAKANDA", "GROOT", "ROCKET", "QUILL", "GAMORA",
                "DRAX", "NEBULA", "MANTIS", "DEADPOOL", "WOLVERINE", "SHIELD",
                "AVENGERS", "ULTRON", "VIBRANIUM", "ASGARD", "MJOLNIR", "INFINITY",
                "JARVIS", "STANLEE", "FURY", "BLADE", "VENOM", "MAGNETO",
            ),
        ),

        // 17. Comic Book Heroes
        WordCategory(
            id = "comic_characters",
            title = "Comic Book Heroes",
            icon = "🦸",
            group = CategoryGroup.POP_CULTURE,
            description = "Batman, Superman, Wolverine, Deadpool, Flash, Joker...",
            wordPool = listOf(
                "BATMAN", "SUPERMAN", "SPIDERMAN", "IRONMAN", "WOLVERINE", "THOR",
                "HULK", "DEADPOOL", "FLASH", "AQUAMAN", "DAREDEVIL", "CYCLOPS",
                "NIGHTWING", "ROBIN", "JOKER", "MAGNETO", "PUNISHER", "SHAZAM",
                "HAWKEYE", "SUPERGIRL", "CATWOMAN", "CARNAGE", "VENOM",
                "BEAST", "ROGUE", "STORM", "GAMBIT", "CABLE", "BISHOP",
            ),
        ),

        // 18. Anime Legends
        WordCategory(
            id = "anime_legends",
            title = "Anime Legends",
            icon = "⛩️",
            group = CategoryGroup.POP_CULTURE,
            description = "Naruto, Goku, Luffy, Zoro, Tanjiro, Gojo, Eren...",
            wordPool = listOf(
                "NARUTO", "SASUKE", "GOKU", "VEGETA", "LUFFY", "ZORO", "TANJIRO",
                "NEZUKO", "EREN", "LEVI", "DEKU", "BAKUGO", "ICHIGO", "SAITAMA",
                "GOJO", "SUKUNA", "GON", "KILLUA", "KAKASHI", "ITACHI", "GOHAN",
                "PICCOLO", "SANJI", "NAMI", "ALLMIGHT", "SHARINGAN", "RASENGAN",
                "TITAN", "SHINOBI", "SAIYAN", "ONEPIECE", "BLEACH", "JUJUTSU",
                "CHIDORI", "BANKAI", "GEASS", "ALCHEMIST", "EDWARD",
            ),
        ),

        // 19. Rock Legends & Bands
        WordCategory(
            id = "music_rock",
            title = "Rock Legends",
            icon = "🎸",
            group = CategoryGroup.POP_CULTURE,
            description = "Beatles, Queen, Nirvana, Metallica, Pink Floyd...",
            wordPool = listOf(
                "BEATLES", "QUEEN", "NIRVANA", "METALLICA", "ACDC", "PINKFLOYD",
                "AEROSMITH", "RADIOHEAD", "COLDPLAY", "BONJOVI", "JOURNEY", "KISS",
                "RAMONES", "SLIPKNOT", "GENESIS", "DEFLEPPARD", "SCORPIONS", "VANHALEN",
                "WHO", "DOORS", "CLASH", "RUSH", "OASIS", "BLUR", "TOOL", "SLAYER",
            ),
        ),

        // 20. Culinary & Food
        WordCategory(
            id = "food_culinary",
            title = "Culinary & Food",
            icon = "🍕",
            group = CategoryGroup.POP_CULTURE,
            description = "Pizza, Sushi, Burger, Ramen, Croissant, Steak...",
            wordPool = listOf(
                "PIZZA", "BURGER", "PASTA", "SUSHI", "TACO", "CROISSANT", "LASAGNA",
                "RAMEN", "RISOTTO", "BURRITO", "CHOCOLATE", "WAFFLE", "FALAFEL",
                "GELATO", "TIRAMISU", "PANCAKE", "BAGEL", "DUMPLING", "PRETZEL",
                "BARBECUE", "ESPRESSO", "STEAK", "BREAD", "CHEESE", "CURRY", "SALAD", "SOUP",
            ),
        ),

        // ==========================================
        // 4. AUTOMOTIVE
        // ==========================================

        // 21. Car Makes & Manufacturers
        WordCategory(
            id = "car_makes",
            title = "Car Makes",
            icon = "🏎️",
            group = CategoryGroup.AUTOMOTIVE,
            description = "Ferrari, Porsche, Toyota, BMW, Audi, McLaren...",
            wordPool = listOf(
                "FERRARI", "PORSCHE", "LAMBORGHINI", "TOYOTA", "BMW", "MERCEDES",
                "AUDI", "HONDA", "NISSAN", "FORD", "CHEVROLET", "MCLAREN",
                "SUBARU", "MAZDA", "VOLKSWAGEN", "HYUNDAI", "BUGATTI", "JAGUAR",
                "BENTLEY", "VOLVO", "MASERATI", "PAGANI", "DODGE", "CORVETTE",
                "ACURA", "INFINITI", "LEXUS", "TESLA", "LOTUS", "JEEP",
            ),
        ),

        // 22. Car Engine & Mechanical Parts
        WordCategory(
            id = "car_engine",
            title = "Car Engine Parts",
            icon = "🔧",
            group = CategoryGroup.AUTOMOTIVE,
            description = "Piston, Crankshaft, Turbo, Radiator, Alternator...",
            wordPool = listOf(
                "PISTON", "CAMSHAFT", "CRANKSHAFT", "CYLINDER", "TURBO", "RADIATOR",
                "ALTERNATOR", "EXHAUST", "MANIFOLD", "SPARKPLUG", "INJECTOR",
                "CLUTCH", "FLYWHEEL", "VALVES", "GASKET", "STARTER", "CARBURETOR",
                "INTERCOOLER", "HEADER", "TIMINGBELT", "OILPUMP", "BLOCK", "HEAD",
                "BELT", "VALVE", "ROD", "PULLEY",
            ),
        ),

        // ==========================================
        // 5. SCIENCE & NATURE
        // ==========================================

        // 23. Science & Quantum Physics
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
                "PARTICLE", "THERMAL", "PLASMA", "COLLIDER", "MASS", "FORCE",
                "VECTOR", "SCALAR", "TORQUE", "JOULE", "NEWTON",
            ),
        ),

        // 24. Deep Space & Cosmos
        WordCategory(
            id = "deep_space",
            title = "Deep Space & Cosmos",
            icon = "🌌",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Galaxy, Nebula, Supernova, Black Hole, Comet...",
            wordPool = listOf(
                "GALAXY", "NEBULA", "SUPERNOVA", "BLACKHOLE", "ASTEROID", "COMET",
                "ANDROMEDA", "PULSAR", "QUASAR", "ORBIT", "ECLIPSE", "PLANET",
                "SATELLITE", "METEOR", "HORIZON", "TELESCOPE", "COSMOS", "AURORA",
                "SOLAR", "VOYAGER", "HUBBLE", "CLUSTER", "LUNAR", "MARS", "VENUS",
                "JUPITER", "SATURN", "TITAN",
            ),
        ),

        // 25. Wildlife & Animals
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
                "JAGUAR", "WALRUS", "GAZELLE", "TIGER", "LION", "ZEBRA", "MONKEY",
                "PANDA", "KOALA",
            ),
        ),

        // 26. World Geography & Landmarks
        WordCategory(
            id = "world_wonders",
            title = "World Geography",
            icon = "🗺️",
            group = CategoryGroup.SCIENCE_NATURE,
            description = "Everest, Amazon, Sahara, Nile, Pacific, Volcano...",
            wordPool = listOf(
                "EVEREST", "AMAZON", "SAHARA", "NILE", "PACIFIC", "COLOSSEUM",
                "PYRAMIDS", "TAJMAHAL", "GLACIER", "VOLCANO", "CANYON", "ISLAND",
                "SAVANNA", "TUNDRA", "RAINFOREST", "ATLANTIC", "HIMALAYA", "DANUBE",
                "VICTORIA", "ALPS", "ANDES", "GOBI", "TAHOE", "FUJI", "DENALI",
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
            "easy" -> Pair(8, 6)
            "hard" -> Pair(10, 13)
            else -> Pair(9, 8) // Medium
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
