package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
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
        try {
            goHome(player, home)
            Message.sendMessage(player, "<green>Teleported to home <white>$home")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Home <white>$home <red>not found")
            return
        }
    }

    @Default
    @Description("Teleports you to your default home")
    fun onCommand(player: Player) {
        try {
            goHome(player)
            Message.sendMessage(player, "<green>Teleported to default home")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Default home not found")
        }
    }

    private fun goHome(player: Player, home: String = "home") {
        val row = DB.getFirstRow("SELECT * FROM homes WHERE player = ? AND name = ?", player.uniqueId.toString(), home)
        if (row == null) throw IllegalArgumentException("Home $home not found")

        DB.executeUpdate("INSERT OR REPLACE INTO back (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
            player.uniqueId.toString(),
            player.world.name,
            player.location.x,
            player.location.y,
            player.location.z,
            player.location.yaw,
            player.location.pitch)

        val loc = Location(
            Bukkit.getWorld(row.getString("world")),
            row.getDbl("x"),
            row.getDbl("y"),
            row.getDbl("z"),
            row.getFloat("yaw"),
            row.getFloat("pitch")
        )
        player.teleportAsync(loc)
    }
}
