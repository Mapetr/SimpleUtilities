package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

@CommandAlias("home")
class Home: BaseCommand() {
    @Default
    @CommandCompletion("@homes")
    @Syntax("<home>")
    @Description("Teleports you to your home")
    fun onCommand(player: Player, home: String) {
        val row = DB.getFirstRow("SELECT * FROM homes WHERE player = ? AND name = ?", player.uniqueId.toString(), home)
        if (row == null) {
            player.sendMessage("Home $home not found")
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
        player.sendMessage("Teleported to home $home")
    }
}