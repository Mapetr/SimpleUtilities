package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Syntax
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("sethome")
class SetHome: BaseCommand() {
    @Default
    @Syntax("<home>")
    @Description("Sets your home")
    fun onCommand(player: Player, home: String) {
        val row = DB.getFirstRow("SELECT * FROM homes WHERE player = ? AND name = ?", player.uniqueId.toString(), home)
        if (row != null) {
            player.sendMessage("Home $home already exists")
            return
        }

        val loc = player.location
        DB.executeInsert(
            "INSERT INTO homes (player, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            player.uniqueId.toString(),
            home,
            loc.world.name,
            loc.x,
            loc.y,
            loc.z,
            loc.yaw,
            loc.pitch
        )
        Message.sendMessage(player, "<green>Home <white>$home <green>set")
    }
}
