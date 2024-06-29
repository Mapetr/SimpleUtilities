package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("delwarp")
class DelWarp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @CommandCompletion("@warps")
    @Description("Deletes a warp")
    fun onCommand(player: Player, warpName: String) {
        val warp = Data.warps[warpName]
        if (warp == null) {
            Message.sendMessage(player, "<red>Warp <white>$warpName <red>not found")
            return
        }

        Data.warps.remove(warpName)
        Database.executeAsync("DELETE FROM warps WHERE name = ?", warpName)
        Message.sendMessage(player, "<green>Warp <white>$warpName <green>deleted")
    }
}
