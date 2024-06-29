package me.mapetr.uwuEssentials

import me.mapetr.uwuEssentials.commands.warp.Warp
import org.bukkit.Location

class Data {
    companion object {
        var homes = HashMap<String, HashMap<String, Location>>()
        var warps = HashMap<String, Location>()
        var back = HashMap<String, Location>()

        fun getHome(player: String, home: String): Location? {
            return homes[player]?.get(home)
        }

        fun setHome(player: String, home: String, loc: Location) {
            homes[player]?.set(home, loc)
            Database.executeAsync("INSERT OR REPLACE INTO homes (player, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                player,
                home,
                loc.world.name,
                loc.x,
                loc.y,
                loc.z,
                loc.yaw,
                loc.pitch)
        }

        fun delHome(player: String, home: String) {
            homes[player]?.remove(home)
            Database.executeAsync("DELETE FROM homes WHERE player = ? AND name = ?", player, home)
        }

        fun getWarp(warp: String): Location? {
            return warps[warp]
        }

        fun setWarp(warp: String, loc: Location) {
            warps[warp] = loc
            Database.executeAsync("INSERT OR REPLACE INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
                warp,
                loc.world.name,
                loc.x,
                loc.y,
                loc.z,
                loc.yaw,
                loc.pitch)
        }

        fun delWarp(warp: String) {
            warps.remove(warp)
            Database.executeAsync("DELETE FROM warps WHERE name = ?", warp)
        }

        fun getBack(player: String): Location? {
            return back[player]
        }

        fun setBack(player: String, loc: Location) {
            back[player] = loc
            Database.executeAsync("INSERT OR REPLACE INTO back (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
                player,
                loc.world.name,
                loc.x,
                loc.y,
                loc.z,
                loc.yaw,
                loc.pitch)
        }
    }
}
