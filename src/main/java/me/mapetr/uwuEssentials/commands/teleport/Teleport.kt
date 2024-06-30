package me.mapetr.uwuEssentials.commands.teleport

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("tp")
class Teleport : BaseCommand() {
    @Default
    @CommandCompletion("@players")
    @Description("Teleports you to a player")
    fun onCommand(player: Player, target: OnlinePlayer) {
        if (target.player == null) {
            player.sendMessage("Player not found")
            return
        }
        if (target.player === player) {
            player.sendMessage("You can't teleport to yourself")
            return
        }

        Data.back[player.uniqueId.toString()] = player.location
        Database.executeAsync("UPDATE back SET x = ${player.location.x}, y = ${player.location.y}, z = ${player.location.z}, yaw = ${player.location.yaw}, pitch = ${player.location.pitch}, world = '${player.world.name}' WHERE name = '${player.uniqueId.toString()}'")

        player.teleportAsync(target.player.location)
        Message.sendMessage(player, "<green>Teleported to <white>${target.player.name}")
    }
}
