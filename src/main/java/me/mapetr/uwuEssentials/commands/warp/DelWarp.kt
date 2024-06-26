package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("delwarp")
class DelWarp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @CommandCompletion("@warps")
    @Description("Deletes a warp")
    fun onCommand(player: Player, warp: String) {
        val row = DB.executeUpdate("DELETE FROM warps WHERE name = ?", warp)
        if (row == 0) {
            Message.sendMessage(player, "<red>Warp <white>$warp <red>not found")
        } else {
            Message.sendMessage(player, "<green>Warp <white>$warp <green>deleted")
        }
    }
}
