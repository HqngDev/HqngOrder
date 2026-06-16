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

package tech.qhuyy.hqngOrder

import com.tcoded.folialib.FoliaLib
import org.bukkit.plugin.java.JavaPlugin
import tech.qhuyy.hqngOrder.model.Software

class HqngOrder : JavaPlugin() {
    lateinit var foliaLib: FoliaLib private set
    lateinit var software: Software private set

    override fun onEnable() {
        // Server Software
        foliaLib = FoliaLib(this)
        detectingServerSoftware()
        checkingIfSpigot()

        logSchedulingStatus()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun detectingServerSoftware() { software = Software.detectServerSoftware(foliaLib) }
    private fun checkingIfSpigot() {
        if (software == Software.SPIGOT || software == Software.UNKNOWN) {
            logger.severe("═══════════════════════════════════════════════════════════════")
            logger.severe("HqngOrder requires Paper or Folia to run ( including forks ).")
            logger.severe("Spigot, non-bukkit and other server software are not supported.")
            logger.severe("Please upgrade to Paper: https://papermc.io/downloads/paper")
            logger.severe("═══════════════════════════════════════════════════════════════")
            server.pluginManager.disablePlugin(this)
            return
        }
    }

    private fun logSchedulingStatus() {
        if (software == Software.PAPER) {
            logger.info("Running on Folia - region-safe scheduling enabled")
        } else {
            logger.info("Running on Paper - standard scheduling enabled")
        }
    }
}
