package me.mapetr.simpleutilities.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandAlias("warp")
public class Warp extends BaseCommand {
    @Default
    @Syntax("<warp>")
    @CommandCompletion("@warps")
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
}
