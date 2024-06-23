package me.mapetr.uwuEssentials.commands.teleport

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import co.aikar.idb.DB
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

        DB.executeUpdate(
            "INSERT OR REPLACE INTO back (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
            target.player.uniqueId.toString(),
            target.player.world.name,
            target.player.location.x,
            target.player.location.y,
            target.player.location.z,
            target.player.location.yaw,
            target.player.location.pitch
        )

        target.player.teleportAsync(player.location)
    }
}
