package me.mapetr.uwuEssentials.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import org.bukkit.entity.Player

@CommandAlias("kys|die|suicide")
class Kill : BaseCommand() {
    @Default
    @Description("Kills you")
    fun onCommand(player: Player) {
        player.health = 0.0
    }
}
