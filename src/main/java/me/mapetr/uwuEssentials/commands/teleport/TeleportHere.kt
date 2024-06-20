package me.mapetr.uwuEssentials.commands.teleport

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.entity.Player

@CommandAlias("tphere")
class TeleportHere : BaseCommand() {
    @Default
    @Syntax("<player>")
    @CommandCompletion("@players")
    @Description("Teleports a player to you")
    fun onCommand(player: Player, target: OnlinePlayer) {
        if (target.player == null) {
            player.sendMessage("Player not found")
            return
        }
        if (target.player === player) {
            player.sendMessage("You can't teleport to yourself")
            return
        }
        target.player.teleport(player)
    }
}
