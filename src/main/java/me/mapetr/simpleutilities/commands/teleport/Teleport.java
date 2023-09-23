package me.mapetr.simpleutilities.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("tp")
public class Teleport extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    @Description("Teleports you to a player")
    public void onCommand(Player player, OnlinePlayer target) {
        if (target.player == null) {
            player.sendMessage("Player not found");
            return;
        }
        if (target.player == player) {
            player.sendMessage("You can't teleport to yourself");
            return;
        }
        player.teleport(target.player);
    }
}
