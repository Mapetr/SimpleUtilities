package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player
import java.sql.SQLException

@CommandAlias("setwarp")
class SetWarp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @Description("Sets a warp")
    fun onSet(player: Player, warp: String) {
        val row = DB.getFirstRow("SELECT * FROM warps WHERE name = ?", warp)
        if (row != null) {
            player.sendMessage("Warp $warp already exists")
            return
        }
        val loc = player.location
        DB.executeInsert(
            "INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
            warp,
            loc.world.name,
            loc.x,
            loc.y,
            loc.z,
            loc.yaw,
            loc.pitch
        )

        Message.sendMessage(player, "<green>Warp <white>$warp <green>set")
    }
}
