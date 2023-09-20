package me.mapetr.simpleutilities.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerListManager {

    FileConfiguration config;

    public  PlayerListManager(FileConfiguration _config) {
        config = _config;
    }
    public void reloadGlobalPlayerList(MiniMessage msg){
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            reloadPlayerList(msg, player);
        };
    }
    public void reloadPlayerList(MiniMessage msg, Player player){
        TagResolver.Single _tps = Placeholder.component("tps", Component.text(Math.round(Bukkit.getServer().getTPS()[0]), NamedTextColor.LIGHT_PURPLE));
        Component footer = msg.deserialize(
                config.getString("footer"),
                _tps
        );
        Component header = msg.deserialize(
                config.getString("header"),
                _tps
        );
        player.sendPlayerListFooter(footer);
        player.sendPlayerListHeader(header);
    }
}
