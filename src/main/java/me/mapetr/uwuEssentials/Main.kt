package me.mapetr.uwuEssentials

import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.PaperCommandManager
import co.aikar.idb.DB
import co.aikar.idb.Database
import co.aikar.idb.DatabaseOptions
import co.aikar.idb.PooledDatabaseOptions
import me.mapetr.uwuEssentials.commands.Kill
import me.mapetr.uwuEssentials.commands.Spectator
import me.mapetr.uwuEssentials.commands.teleport.Teleport
import me.mapetr.uwuEssentials.commands.teleport.TeleportHere
import me.mapetr.uwuEssentials.commands.warp.Warp
import me.mapetr.uwuEssentials.commands.warp.WarpSet
import me.mapetr.uwuEssentials.services.ChatService
import me.mapetr.uwuEssentials.services.PlayerListManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException

class Main : JavaPlugin(), Listener {
    var _playerListManager: PlayerListManager = PlayerListManager(config)
    var _chatService: ChatService = ChatService(config)
    override fun onEnable() {
        val options = DatabaseOptions.builder().sqlite("plugins/simpleutilities/simpleutilities.db").build()
        val db: Database = PooledDatabaseOptions.builder().options(options).createHikariDatabase()
        DB.setGlobalDatabase(db)

        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(255) PRIMARY KEY, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255))")
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        val manager = PaperCommandManager(this)
        manager.registerCommand(Spectator())
        manager.registerCommand(Kill())
        manager.registerCommand(Teleport())
        manager.registerCommand(TeleportHere())
        manager.registerCommand(Warp())
        manager.registerCommand(WarpSet())
        manager.enableUnstableAPI("help")

        val completions = manager.commandCompletions
        completions.registerAsyncCompletion("warps") { c: BukkitCommandCompletionContext? ->
            try {
                return@registerAsyncCompletion DB.getFirstColumnResults<String>("SELECT name FROM warps")
            } catch (e: SQLException) {
                throw RuntimeException(e)
            }
        }

        this.saveDefaultConfig()
        server.pluginManager.registerEvents(Main(), this)
        val msg = MiniMessage.miniMessage()
        config.addDefault("footer", "I love foot <tps>")
        config.addDefault("header", "I love head <tps>")
        config.addDefault("delay", 100)
        config.addDefault("frequency", 200)
        config.addDefault("chat.format", "<color:#99AAB5><player>: <msg>")
        config.addDefault("chat.colors.name", "#7289DA")
        config.addDefault("chat.colors.msg", "#FFFFFF")
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            _playerListManager.reloadGlobalPlayerList(msg)
        }, config.getLong("delay"), config.getLong("frequency"))
        config.options().copyDefaults(true)
        saveConfig()
    }

    override fun onDisable() {
        DB.close()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        _playerListManager.reloadPlayerList(MiniMessage.miniMessage(), event.player)
    }

    @EventHandler
    fun onMessageSent(event: AsyncPlayerChatEvent) {
        event.isCancelled = _chatService.processMessage(event, MiniMessage.miniMessage())
    }
}
