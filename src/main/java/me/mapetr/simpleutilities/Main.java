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
import me.mapetr.simpleutilities.services.ChatService;
import me.mapetr.simpleutilities.services.PlayerListManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin implements Listener {
    FileConfiguration config = this.getConfig();
    PlayerListManager _playerListManager = new PlayerListManager(config);
    ChatService _chatService = new ChatService(config);
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
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new Main(), this);
        MiniMessage msg = MiniMessage.miniMessage();
        config.addDefault("footer", "I love foot <tps>");
        config.addDefault("header", "I love head <tps>");
        config.addDefault("delay", 100);
        config.addDefault("frequency", 200);
        config.addDefault("chat.format", "<color:#99AAB5><player>: <msg>");
        config.addDefault("chat.colors.name", "#7289DA");
        config.addDefault("chat.colors.msg", "#FFFFFF");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,() -> {
            _playerListManager.reloadGlobalPlayerList(msg);
        },config.getLong("delay"),config.getLong("frequency"));
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        _playerListManager.reloadPlayerList(MiniMessage.miniMessage(), event.getPlayer());
    }
    @EventHandler
    public void onMessageSent(AsyncPlayerChatEvent event) {
        event.setCancelled(_chatService.processMessage(event, MiniMessage.miniMessage()));
    }
}
