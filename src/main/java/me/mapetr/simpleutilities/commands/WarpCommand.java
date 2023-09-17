package me.mapetr.simpleutilities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandAlias("warp")
public class WarpCommand extends BaseCommand {
    @Default
    @Syntax("<warp>")
    @Description("Teleports you to a warp")
    public void onCommand(Player player, String warp) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM warps WHERE name = ?", warp);
            if (row == null) {
                player.sendMessage("Warp " + warp + " does not exist");
                return;
            }
            Location loc = new Location(Bukkit.getWorld(row.getString("world")),
                    row.getDbl("x"),
                    row.getDbl("y"),
                    row.getDbl("z"),
                    row.getFloat("yaw"),
                    row.getFloat("pitch"));
            player.sendMessage("Teleporting to warp " + warp);
            player.teleport(loc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Subcommand("set")
    @Syntax("<warp>")
    @Description("Sets a warp")
    public void onSet(Player player, String warp) {
        try {
            DbRow row = DB.getFirstRow("SELECT * FROM warps WHERE name = ?", warp);
            if (row != null) {
                player.sendMessage("Warp " + warp + " already exists");
                return;
            }
            Location loc = player.getLocation();
            DB.executeInsert("INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    warp,
                    loc.getWorld().getName(),
                    loc.getX(),
                    loc.getY(),
                    loc.getZ(),
                    loc.getYaw(),
                    loc.getPitch());
            player.sendMessage("Warp " + warp + " set");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
