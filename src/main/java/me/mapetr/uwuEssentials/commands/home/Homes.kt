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
        if (rows.isEmpty()) {
            Message.sendMessage(player, "<green>You don't have any homes")
            return
        }

        for (row in rows) {
            val name = row.getString("name")
            if (name == "home") continue
            homes += "$name, "
        }

        homes = homes.substring(0, homes.length - 2)
        Message.sendMessage(player, "<green>Your homes: <white>$homes")
    }
}
