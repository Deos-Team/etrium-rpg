package dev.deos.etrium.config

import dev.deos.etrium.Etrium
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths

object ConfigManager {
    private val json = Json { encodeDefaults = true; prettyPrint = true; ignoreUnknownKeys = true }
    private val dir = Paths.get("", "config", Etrium.MI).toFile()
    private val cfg = File(dir, "config.json")

    init {
        if (!dir.exists()) dir.mkdirs()
        if (!cfg.exists()) cfg.writeText(json.encodeToString(Config()))
    }

    fun readCfg(): Config = json.decodeFromString(cfg.readText())

}