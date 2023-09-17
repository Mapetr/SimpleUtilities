package me.mapetr.simpleutilities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("c")
public class SpectatorCommand extends BaseCommand {
    @Default
    @Description("Change gamemode to spectator")
    public void onCommand(Player player) {
        player.setGameMode(player.getGameMode() == org.bukkit.GameMode.SPECTATOR ? org.bukkit.GameMode.SURVIVAL : org.bukkit.GameMode.SPECTATOR);
    }
}
