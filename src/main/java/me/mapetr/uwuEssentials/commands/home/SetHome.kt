package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("sethome")
class SetHome : BaseCommand() {
    @Default
    @Syntax("<home>")
    @Description("Sets your home")
    fun onCommand(player: Player, home: String) {
        try {
            setHome(player, home)
            Message.sendMessage(player, "<green>Home <white>$home <green>set")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Home <white>$home <red>already exists")
        }
    }

    @Default
    @Description("Sets your default home")
    fun onCommand(player: Player) {
        try {
            setHome(player)
            Message.sendMessage(player, "<green>Home set")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Home already exists")
        }
    }

    private fun setHome(player: Player, homeName: String = "home") {
        val playerData = Data.homes[player.uniqueId.toString()]
        if (playerData == null) {
            throw IllegalArgumentException("Player data not found")
        }

        val home = playerData[homeName]
        if (home != null) {
            throw IllegalArgumentException("Home $homeName already exists")
        }

        Data.homes[player.uniqueId.toString()]?.put(homeName, player.location)
        Database.executeAsync(
            "INSERT INTO homes (player, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            player.uniqueId.toString(),
            homeName,
            player.world.name,
            player.location.x,
            player.location.y,
            player.location.z,
            player.location.yaw,
            player.location.pitch
        )
    }
}
