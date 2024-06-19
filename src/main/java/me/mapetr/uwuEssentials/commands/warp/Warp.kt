package me.mapetr.uwuEssentials.commands.warp

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.idb.DB
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
        try {
            val row = DB.getFirstRow("SELECT * FROM warps WHERE name = ?", warp)
            if (row == null) {
                player.sendMessage("Warp $warp does not exist")
                return
            }
            val loc = Location(
                Bukkit.getWorld(row.getString("world")),
                row.getDbl("x"),
                row.getDbl("y"),
                row.getDbl("z"),
                row.getFloat("yaw"),
                row.getFloat("pitch")
            )
            player.sendMessage("Teleporting to warp $warp")
            player.teleport(loc)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}
