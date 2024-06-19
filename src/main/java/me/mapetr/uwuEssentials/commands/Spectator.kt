package me.mapetr.uwuEssentials.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import org.bukkit.GameMode
import org.bukkit.entity.Player

@CommandAlias("c")
class Spectator : BaseCommand() {
    @Default
    @Description("Change gamemode to spectator")
    fun onCommand(player: Player) {
        player.gameMode =
            if (player.gameMode == GameMode.SPECTATOR) GameMode.SURVIVAL else GameMode.SPECTATOR
    }
}
