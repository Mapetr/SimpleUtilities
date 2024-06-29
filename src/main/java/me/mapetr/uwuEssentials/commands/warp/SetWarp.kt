package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Database
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("setwarp")
class SetWarp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @Description("Sets a warp")
    fun onSet(player: Player, warpName: String) {
        val warp = Data.warps[warpName]
        if (warp != null) {
            Message.sendMessage(player, "<red>Warp <white>$warpName <red>already exists")
            return
        }

        Data.warps[warpName] = player.location
        Database.executeAsync(
            "INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
            warpName,
            player.world.name,
            player.location.x,
            player.location.y,
            player.location.z,
            player.location.yaw,
            player.location.pitch
        )

        Message.sendMessage(player, "<green>Warp <white>$warpName <green>set")
    }
}
