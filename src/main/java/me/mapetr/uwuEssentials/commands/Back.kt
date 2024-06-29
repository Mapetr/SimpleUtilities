package me.mapetr.uwuEssentials.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Message
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

@CommandAlias("back")
class Back : BaseCommand() {
    @Default
    @Description("Teleports you to your last location")
    fun onCommand(player: Player) {
        val loc = Data.back[player.uniqueId.toString()]
        if (loc == null) {
            Message.sendMessage(player, "<red>No location to teleport back to")
            return
        }

        player.teleportAsync(loc)
        Message.sendMessage(player, "<green>Teleported to your last location")
    }
}
