package me.mapetr.simpleutilities;

import co.aikar.commands.PaperCommandManager;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import me.mapetr.simpleutilities.commands.KillCommand;
import me.mapetr.simpleutilities.commands.SpectatorCommand;
import me.mapetr.simpleutilities.commands.TeleportCommand;
import me.mapetr.simpleutilities.commands.WarpCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        DatabaseOptions options = DatabaseOptions.builder().sqlite("simpleutilities.db").build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);

        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(255) PRIMARY KEY, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new SpectatorCommand());
        manager.registerCommand(new KillCommand());
        manager.registerCommand(new TeleportCommand());
        manager.registerCommand(new WarpCommand());
        manager.enableUnstableAPI("help");
    }

    @Override
    public void onDisable() {
    }
}
