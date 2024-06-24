package me.mapetr.uwuEssentials.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

@CommandAlias("back")
class Back : BaseCommand() {
    @Default
    @Description("Teleports you to your last location")
    fun onCommand(player: Player) {
        val row = DB.getFirstRow("SELECT * FROM back WHERE name = ?", player.uniqueId.toString())
        if (row == null) {
            player.sendMessage("You have no last location")
            return
        }
        val loc = Location(
            Bukkit.getWorld(row.getString("world")),
            row.getDbl("x"),
            row.getDbl("y"),
            row.getDbl("z"),
            row.getFloat("yaw"),
            row.getFloat("pitch")
        )
        player.teleportAsync(loc)
        Message.sendMessage(player, "<green>Teleported to your last location")
    }
}
