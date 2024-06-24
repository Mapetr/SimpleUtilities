package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.sql.SQLException

@CommandAlias("warp")
class Warp : BaseCommand() {
    @Default
    @Syntax("<warp>")
    @CommandCompletion("@warps")
    @Description("Teleports you to a warp")
    fun onCommand(player: Player, warp: String) {
        val row = DB.getFirstRow("SELECT * FROM warps WHERE name = ?", warp)
        if (row == null) {
            player.sendMessage("Warp $warp does not exist")
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

        val loc = Location(
            Bukkit.getWorld(row.getString("world")),
            row.getDbl("x"),
            row.getDbl("y"),
            row.getDbl("z"),
            row.getFloat("yaw"),
            row.getFloat("pitch")
        )
        player.teleportAsync(loc)
        Message.sendMessage(player, "<green>Teleported to warp <white>$warp")
    }
}
