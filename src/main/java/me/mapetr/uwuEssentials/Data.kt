package me.mapetr.uwuEssentials

import me.mapetr.uwuEssentials.commands.warp.Warp
import org.bukkit.Location

class Data {
    companion object {
        var homes = HashMap<String, HashMap<String, Location>>()
        var warps = HashMap<String, Location>()
        var back = HashMap<String, Location>()
    }
}
