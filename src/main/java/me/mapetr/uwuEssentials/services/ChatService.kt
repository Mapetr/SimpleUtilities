package me.mapetr.uwuEssentials.services

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatService(private val config: FileConfiguration) {
    val mainTitle: Component = Component.text("Zmínka", NamedTextColor.WHITE)
    val subtitle: Component = Component.text("Byl jsi zmíněn v chatu :3", NamedTextColor.GRAY)
    val title: Title = Title.title(mainTitle, subtitle)
    var pingSound: Sound = Sound.sound(Key.key("ambient.cave"), Sound.Source.AMBIENT, 1f, 1f)


    //Should return success
    //true if successful
    //otherwise false
    //success should always mean if new message was successfully sent
    fun processMessage(event: AsyncPlayerChatEvent, msg: MiniMessage): Boolean {
        try {
            var msgContent = event.message
            val foundPlayers: MutableSet<String> = HashSet()
            for (player in Bukkit.getServer().onlinePlayers) {
                val completeString = "@" + player.name
                if (msgContent.contains(completeString)) {
                    foundPlayers.add(completeString)
                    msgContent = msgContent.replace(completeString.toRegex(), completeString)
                    player.showTitle(title)
                    player.playSound(pingSound)
                }
            }
            var parsed = msg.deserialize(
                config.getString("chat.format")!!,
                Placeholder.component(
                    "player", Component.text(
                        event.player.name, TextColor.fromCSSHexString(
                            config.getString("chat.colors.name")!!
                        )
                    )
                ),
                Placeholder.component(
                    "msg", Component.text(
                        msgContent, TextColor.fromCSSHexString(
                            config.getString("chat.colors.msg")!!
                        )
                    )
                )
            )
            if (foundPlayers.size != 0) {
                for (s in foundPlayers) {
                    val conf = TextReplacementConfig.builder().match(s).replacement(
                        Component.text(
                            s, TextColor.fromCSSHexString("#FF7F00")
                        )
                    ).build()
                    parsed = parsed.replaceText(conf)
                }
            }

            Bukkit.getServer().sendMessage(parsed)
            return true
        } catch (ex: Exception) {
            return false
        }
    }
}
