package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
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

    private fun delHome(player: Player, home: String = "home") {
        val row = DB.executeUpdate("DELETE FROM homes WHERE player = ? AND name = ?", player.uniqueId.toString(), home)
        if (row == 0) throw IllegalArgumentException("Home $home not found")
    }
}
