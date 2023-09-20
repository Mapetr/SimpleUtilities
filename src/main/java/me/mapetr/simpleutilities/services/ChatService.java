package me.mapetr.simpleutilities.services;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatService {
    private FileConfiguration config;
    public  ChatService(FileConfiguration _config) {
        config = _config;
    }

    //Should return success
    //true if successful
    //otherwise false
    //success should always mean if new message was successfully sent
    public boolean processMessage(AsyncPlayerChatEvent event, MiniMessage msg) {
        try {
            Component parsed = msg.deserialize(
                    "<player>: <msg>",
                    Placeholder.component("player", Component.text(event.getPlayer().getName(), TextColor.fromCSSHexString("#FF7F00"))),
                    Placeholder.component("msg", Component.text(event.getMessage()))
            );

            Bukkit.getServer().sendMessage(parsed);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
