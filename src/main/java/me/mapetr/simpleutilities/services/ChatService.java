package me.mapetr.simpleutilities.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatService {
    private FileConfiguration config;
    public  ChatService(FileConfiguration _config) {
        config = _config;
    }

    final Component mainTitle = Component.text("Zmínka", NamedTextColor.WHITE);
    final Component subtitle = Component.text("Byl jsi zmíněn v chatu :3", NamedTextColor.GRAY);
    final Title title = Title.title(mainTitle, subtitle);


    //Should return success
    //true if successful
    //otherwise false
    //success should always mean if new message was successfully sent
    public boolean processMessage(AsyncPlayerChatEvent event, MiniMessage msg) {
        try {
            String msgContent = event.getMessage();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                String completeString = "@"+player.getName();
                if (msgContent.contains(completeString)) {
                    msgContent = msgContent.replaceAll(completeString, "!"+completeString);
                    player.showTitle(title);
                }
            }
            Component parsed = msg.deserialize(
                    config.getString("chat.format"),
                    Placeholder.component("player", Component.text(event.getPlayer().getName(), TextColor.fromCSSHexString(config.getString("chat.colors.name")))),
                    Placeholder.component("msg", Component.text(msgContent, TextColor.fromCSSHexString(config.getString("chat.colors.msg"))))
            );

            Bukkit.getServer().sendMessage(parsed);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
