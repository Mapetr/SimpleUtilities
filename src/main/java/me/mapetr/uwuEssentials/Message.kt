package me.mapetr.uwuEssentials

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

class Message {
    companion object {
        private val mm = MiniMessage.miniMessage()

        fun sendMessage(player: Player, message: String) {
            player.sendMessage(mm.deserialize(message))
        }
    }
}
