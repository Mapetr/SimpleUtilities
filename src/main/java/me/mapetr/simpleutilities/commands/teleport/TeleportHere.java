package me.mapetr.simpleutilities.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("tphere")
public class TeleportHere extends BaseCommand {
    @Default
    @Syntax("<player>")
    @CommandCompletion("@players")
    @Description("Teleports a player to you")
    public void onCommand(Player player, OnlinePlayer target) {
        if (target.player == null) {
            player.sendMessage("Player not found");
            return;
        }
        if (target.player == player) {
            player.sendMessage("You can't teleport to yourself");
            return;
        }
        target.player.teleport(player);
    }
}
