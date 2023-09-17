package me.mapetr.simpleutilities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.awt.print.Paper;

@CommandAlias("kys|die|suicide")
public class KillCommand extends BaseCommand {
    @Default
    @Description("Kills you")
    public void onCommand(Player player) {
        player.setHealth(0);
    }
}
