package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("warps")
class Warps: BaseCommand() {
    @Default
    @Description("Lists all warps")
    fun onCommand(player: Player) {
        var warps = ""
        for (warp in Data.warps) {
            warps += warp.key + ", "
        }
        if (warps.isNotEmpty()) {
            warps = warps.substring(0, warps.length - 2)
            Message.sendMessage(player, "<green>Warps: <white>$warps")
        } else {
            Message.sendMessage(player, "<green>There are no warps")
        }
    }
}
