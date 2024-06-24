package me.mapetr.uwuEssentials.commands.teleport

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import net.kyori.adventure.text.minimessage.MiniMessage
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

        DB.executeUpdate(
            "INSERT OR REPLACE INTO back (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
            player.uniqueId.toString(),
            player.world.name,
            player.location.x,
            player.location.y,
            player.location.z,
            player.location.yaw,
            player.location.pitch
        )

        player.teleportAsync(target.player.location)
        Message.sendMessage(player, "<green>Teleported to <white>${target.player.name}")
    }
}
