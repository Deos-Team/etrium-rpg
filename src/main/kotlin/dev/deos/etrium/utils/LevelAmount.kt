package dev.deos.etrium.utils

object LevelAmount {

    const val MAX_LEVEL = 12

    val levelList: MutableMap<Int, Int> = mutableMapOf(
        1 to 128,
        2 to 270,
        3 to 420,
        4 to 560,
        5 to 670,
        6 to 820,
        7 to 950,
        8 to 1100,
        9 to 1310,
        10 to 1720,
        11 to 1930,
        12 to 2240
    )

    val entityLevels: MutableMap<String, IntRange> = mutableMapOf(
        "cow" to (1..3),
        "pig" to (1..2),
        "chicken" to (1..2),
        "sheep" to (1..3),
        "zombie" to (3..8),
        "creeper" to (6..12),
        "skeleton" to (4..10)
    )

}