package me.mapetr.uwuEssentials.services

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class PlayerListManager(var config: FileConfiguration) {
    fun reloadGlobalPlayerList(msg: MiniMessage) {
        for (player in Bukkit.getServer().onlinePlayers) {
            reloadPlayerList(msg, player)
        }
    }

    fun reloadPlayerList(msg: MiniMessage, player: Player) {
        val footerSerialized = config.getString("footer")
        val headerSerialized = config.getString("header")
        val _tps =
            Placeholder.component("tps", Component.text(round(Bukkit.getServer().tps[0]), NamedTextColor.LIGHT_PURPLE))
        val _mspt = Placeholder.component(
            "mspt",
            Component.text(round(Bukkit.getServer().averageTickTime), NamedTextColor.LIGHT_PURPLE)
        )
        if (footerSerialized != null) {
            val footer = msg.deserialize(
                footerSerialized,
                _tps,
                _mspt
            )
            player.sendPlayerListFooter(footer)
        }
        if (headerSerialized != null) {
            val header = msg.deserialize(
                headerSerialized,
                _tps,
                _mspt
            )
            player.sendPlayerListHeader(header)
        }
    }

    private fun round(value: Double): Float {
        return Math.round(10 * value).toFloat() / 10
    }
}
