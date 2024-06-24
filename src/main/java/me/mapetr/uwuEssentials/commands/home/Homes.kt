package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("homes")
class Homes: BaseCommand() {
    @Default
    @Description("Lists your homes")
    fun onCommand(player: Player) {
        val rows = DB.getResults("SELECT * FROM homes WHERE player = ?", player.uniqueId.toString())
        var homes = ""
        for (row in rows) {
            homes += row.getString("name") + ", "
        }
        if (homes.isNotEmpty()) {
            homes = homes.substring(0, homes.length - 2)
            Message.sendMessage(player, "<green>Your homes: <white>$homes")
        } else {
            Message.sendMessage(player, "<green>You don't have any homes")
        }
    }
}
