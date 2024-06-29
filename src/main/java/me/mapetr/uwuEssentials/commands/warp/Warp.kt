package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("warp")
class Warp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @CommandCompletion("@warps")
    @Description("Teleports you to a warp")
    fun onCommand(player: Player, warpName: String) {
        val warp = Data.warps[warpName]
        if (warp == null) {
            Message.sendMessage(player, "<red>Warp <white>$warpName <red>not found")
            return
        }

        Data.back[player.uniqueId.toString()] = player.location
        Database.executeAsync("UPDATE back WHERE name = ${player.uniqueId.toString()} SET x = ${player.location.x}, y = ${player.location.y}, z = ${player.location.z}, yaw = ${player.location.yaw}, pitch = ${player.location.pitch}, world = ${player.world.name}")

        player.teleportAsync(warp)
        Message.sendMessage(player, "<green>Teleported to warp <white>$warpName")
    }
}
