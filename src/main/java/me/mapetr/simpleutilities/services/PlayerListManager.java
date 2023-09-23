package me.mapetr.simpleutilities.services;

import me.mapetr.simpleutilities.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
public class PlayerListManager {

    FileConfiguration config;

    public  PlayerListManager(FileConfiguration _config) {
        config = _config;
    }
    public void reloadGlobalPlayerList(MiniMessage msg){
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            reloadPlayerList(msg, player);
        }
    }
    public void reloadPlayerList(MiniMessage msg, Player player){
        String footerSerialized, headerSerialized;
        footerSerialized = config.getString("footer");
        headerSerialized =  config.getString("header");
        TagResolver.Single _tps = Placeholder.component("tps", Component.text(round(Bukkit.getServer().getTPS()[0]), NamedTextColor.LIGHT_PURPLE));
        TagResolver.Single _mspt = Placeholder.component("mspt", Component.text(round(Bukkit.getServer().getAverageTickTime()), NamedTextColor.LIGHT_PURPLE));
        if (footerSerialized != null) {
            Component footer = msg.deserialize(
                    footerSerialized,
                    _tps,
                    _mspt
            );
            player.sendPlayerListFooter(footer);
        }
        if (headerSerialized != null) {
            Component header = msg.deserialize(
                    headerSerialized,
                    _tps,
                    _mspt
            );
            player.sendPlayerListHeader(header);
        }
    }

    private float round(double value) {
        return (float) Math.round(10 * value) / 10;
    }
}
