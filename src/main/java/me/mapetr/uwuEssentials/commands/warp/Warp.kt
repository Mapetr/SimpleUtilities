package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.mapetr.uwuEssentials.Data
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

        player.teleportAsync(warp)
        Message.sendMessage(player, "<green>Teleported to warp <white>$warpName")
    }
}
