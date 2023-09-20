package me.mapetr.simpleutilities.services;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class ChatService {
    private FileConfiguration config;
    public  ChatService(FileConfiguration _config) {
        config = _config;
    }

    final Component mainTitle = Component.text("Zmínka", NamedTextColor.WHITE);
    final Component subtitle = Component.text("Byl jsi zmíněn v chatu :3", NamedTextColor.GRAY);
    final Title title = Title.title(mainTitle, subtitle);
    Sound pingSound = Sound.sound(Key.key("ambient.cave"), Sound.Source.AMBIENT, 1f, 1f);



    //Should return success
    //true if successful
    //otherwise false
    //success should always mean if new message was successfully sent
    public boolean processMessage(AsyncPlayerChatEvent event, MiniMessage msg) {
        try {
            String msgContent = event.getMessage();
            Set<String> foundPlayers = new HashSet<String>();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                String completeString = "@"+player.getName();
                if (msgContent.contains(completeString)) {
                    foundPlayers.add(completeString);
                    msgContent = msgContent.replaceAll(completeString, completeString);
                    player.showTitle(title);
                    player.playSound(pingSound);
                }
            }
            Component parsed = msg.deserialize(
                    config.getString("chat.format"),
                    Placeholder.component("player", Component.text(event.getPlayer().getName(), TextColor.fromCSSHexString(config.getString("chat.colors.name")))),
                    Placeholder.component("msg", Component.text(msgContent, TextColor.fromCSSHexString(config.getString("chat.colors.msg"))))
            );
            if (foundPlayers.size() != 0) {
                for (String s : foundPlayers) {
                    TextReplacementConfig conf = TextReplacementConfig.builder().match(s).replacement(Component.text(s, TextColor.fromCSSHexString("#FF7F00"))).build();
                    parsed = parsed.replaceText(conf);
                }
            }

            Bukkit.getServer().sendMessage(parsed);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
