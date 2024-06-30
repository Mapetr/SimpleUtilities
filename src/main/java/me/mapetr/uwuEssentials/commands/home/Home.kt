package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
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
        val playerData = Data.homes[player.uniqueId.toString()]
        if (playerData == null) {
            throw IllegalArgumentException("Player data not found")
        }

        val loc = playerData[home]
        if (loc == null) {
            throw IllegalArgumentException("Home $home not found")
        }

        Data.back[player.uniqueId.toString()] = player.location
        Database.executeAsync("UPDATE back SET x = ${player.location.x}, y = ${player.location.y}, z = ${player.location.z}, yaw = ${player.location.yaw}, pitch = ${player.location.pitch}, world = '${player.world.name}' WHERE name = '${player.uniqueId.toString()}'")

        player.teleportAsync(loc)
    }
}
