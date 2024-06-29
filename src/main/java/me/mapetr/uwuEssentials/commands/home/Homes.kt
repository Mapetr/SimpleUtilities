package me.mapetr.uwuEssentials.commands.home

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.idb.DB
import me.mapetr.uwuEssentials.Data
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("homes")
class Homes: BaseCommand() {
    @Default
    @Description("Lists your homes")
    fun onCommand(player: Player) {
        var homes = ""

        val homeList = Data.homes[player.uniqueId.toString()]
        if (homeList == null) {
            Message.sendMessage(player, "<red>You don't have any homes")
            return
        }

        for (home in homeList) {
            if (home.key == "home") continue
            homes += "${home.key}, "
        }

        homes = homes.substring(0, homes.length - 2)
        Message.sendMessage(player, "<green>Your homes: <white>$homes")
    }
}
