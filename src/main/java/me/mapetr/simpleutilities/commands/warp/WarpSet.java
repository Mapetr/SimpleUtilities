package me.mapetr.simpleutilities.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandAlias("setwarp")
public class WarpSet extends BaseCommand {
    @Default
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
