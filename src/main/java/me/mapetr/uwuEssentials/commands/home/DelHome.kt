package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("delhome")
class DelHome: BaseCommand() {
    @Default
    @CommandCompletion("@homes")
    @Syntax("<home>")
    @Description("Deletes a home")
    fun onCommand(player: Player, home: String) {
        try {
            delHome(player, home)
            Message.sendMessage(player, "<green>Home <white>$home <green>deleted")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Home <white>$home <red>not found")
        }
    }

    @Default
    @Description("Deletes your default home")
    fun onCommand(player: Player) {
        try {
            delHome(player)
            Message.sendMessage(player, "<green>Default home deleted")
        } catch (e: IllegalArgumentException) {
            Message.sendMessage(player, "<red>Default home not found")
        }
    }

    private fun delHome(player: Player, homeName: String = "home") {
        val playerData = Data.homes[player.uniqueId.toString()]
        if (playerData == null) {
            throw IllegalArgumentException("Player data not found")
        }

        val home = playerData[homeName]
        if (home == null) {
            throw IllegalArgumentException("Home $home not found")
        }

        Database.executeAsync("DELETE FROM homes WHERE player = ? AND name = ?", player.uniqueId.toString(), homeName)
        Data.homes[player.uniqueId.toString()]?.remove(homeName)
    }
}
