package tech.qhuyy.hqngOrder.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import tech.qhuyy.hqngOrder.HqngOrder

class TempCommand(private val plugin: HqngOrder) : CommandExecutor {
    private val i = plugin.pluginBuildInfo
    private val mm = MiniMessage.miniMessage()
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        listOf(
            "<white><color:#FFC0CB>${plugin.pluginBuildInfo.getPluginName(true)} ${i.buildVersion}</color> <white>ʙʏ</white> <gray>${
                plugin.pluginMeta.authors.joinToString(
                    " and "
                )
            }</gray>",
            "<gray>┌─</gray><white>ʙʀᴀɴᴄʜ:</white> <#76D7C4>${i.branch}</#76D7C4>",
            "<gray>├─</gray><white>ᴄᴏᴍᴍɪᴛ:</white> <#48C9B0>${i.commitIdAbbrev}</#48C9B0>",
            "<gray>├─</gray><white>ᴍᴇssᴀɢᴇ:</white> <#A3E4D7>${i.commitMessage}</#A3E4D7>",
            "<gray>├─</gray><white>ʙᴜɪʟᴛ:</white> <#85C1E9>${i.buildTime}</#85C1E9>",
            "<gray>└─</gray><white>ᴅɪʀᴛʏ:</white> <${if (i.isDirty) "red" else "green"}>${if (i.isDirty) "ᴛʀᴜᴇ" else "ғᴀʟsᴇ"}</${if (i.isDirty) "red" else "green"}>"
        ).forEach {
            sender.sendMessage(
                mm.deserialize(it)
            )
        }
        return true
    }
}