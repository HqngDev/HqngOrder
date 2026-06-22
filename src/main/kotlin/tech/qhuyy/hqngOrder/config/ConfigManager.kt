/*
 * Copyright (c) 2026 hqng05 <hqng05@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package tech.qhuyy.hqngOrder.config

import org.bukkit.configuration.file.FileConfiguration
import tech.qhuyy.hqngOrder.HqngOrder
import java.util.logging.Level

/**
 * Centralized, typed access to plugin configuration.
 *
 * All config values are read through this class so that consumers never
 * touch [FileConfiguration] directly.  Defaults are defined here in one
 * place instead of being scattered across the codebase.
 */
class ConfigManager(private val plugin: HqngOrder) {

    private lateinit var config: FileConfiguration

    // ──────────────────────────────────────────────
    //  Lifecycle
    // ──────────────────────────────────────────────

    fun init() {
        plugin.saveDefaultConfig()
        reload()
        plugin.logger.info("ConfigManager initialized successfully")
    }

    fun reload() {
        plugin.reloadConfig()
        config = plugin.config
        plugin.logger.info("Configuration reloaded from disk")
    }

    // ──────────────────────────────────────────────
    //  Database
    // ──────────────────────────────────────────────

    fun getDatabaseType(): DatabaseType {
        val raw = config.getString("database.type", "sqlite")?.lowercase()?.trim() ?: "sqlite"
        return when (raw) {
            "mysql" -> DatabaseType.MYSQL
            "sqlite" -> DatabaseType.SQLITE
            else -> {
                plugin.logger.warning("Unknown database type '$raw', falling back to SQLite")
                DatabaseType.SQLITE
            }
        }
    }

    fun getMySQLConfig(): MySQLConfig {
        return try {
            MySQLConfig(
                host = config.getString("database.mysql.host", "localhost") ?: "localhost",
                port = config.getInt("database.mysql.port", 3306),
                database = config.getString("database.mysql.database", "hqngorder_database")
                    ?: "hqngorder_database",
                username = config.getString("database.mysql.username", "hqngorder") ?: "hqngorder",
                password = config.getString("database.mysql.password", "password") ?: "password",
                useSSL = config.getBoolean("database.mysql.use-ssl", false),
                maxPoolSize = config.getInt("database.hikari.maximum-pool-size", 10),
                minIdle = config.getInt("database.hikari.minimum-idle", 2),
                connectionTimeout = config.getLong("database.hikari.connection-timeout", 30_000L),
                idleTimeout = config.getLong("database.hikari.idle-timeout", 600_000L),
                maxLifetime = config.getLong("database.hikari.max-lifetime", 1_800_000L)
            )
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to read MySQL config, using defaults", e)
            MySQLConfig()
        }
    }

    fun getSQLiteConfig(): SQLiteConfig {
        return try {
            SQLiteConfig(
                filePath = config.getString("database.sqlite.file", "data.db") ?: "data.db"
            )
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to read SQLite config, using defaults", e)
            SQLiteConfig()
        }
    }

    // ──────────────────────────────────────────────
    //  Data classes
    // ──────────────────────────────────────────────

    enum class DatabaseType(val displayName: String) {
        MYSQL("MySQL"),
        SQLITE("SQLite")
    }

    data class MySQLConfig(
        val host: String = "localhost",
        val port: Int = 3306,
        val database: String = "hqngorder_database",
        val username: String = "hqngorder",
        val password: String = "password",
        val useSSL: Boolean = false,
        val maxPoolSize: Int = 10,
        val minIdle: Int = 2,
        val connectionTimeout: Long = 30_000L,
        val idleTimeout: Long = 600_000L,
        val maxLifetime: Long = 1_800_000L
    )

    data class SQLiteConfig(
        val filePath: String = "data.db"
    )

}
