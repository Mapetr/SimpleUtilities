package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import org.bukkit.entity.Player

@CommandAlias("warps")
class Warps: BaseCommand() {
    @Default
    @Description("Lists all warps")
    fun onCommand(player: Player) {
        val rows = DB.getResults("SELECT * FROM warps")
        var warps = ""
        for (row in rows) {
            warps += row.getString("name") + ", "
        }
        if (warps.isNotEmpty()) {
            warps = warps.substring(0, warps.length - 2)
            player.sendMessage("Warps: $warps")
        } else {
            player.sendMessage("There are no warps")
        }
    }
}
