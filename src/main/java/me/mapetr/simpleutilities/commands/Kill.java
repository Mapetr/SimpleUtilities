package me.mapetr.simpleutilities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("kys|die|suicide")
public class Kill extends BaseCommand {
    @Default
    @Description("Kills you")
    public void onCommand(Player player) {
        player.setHealth(0);
    }
}
