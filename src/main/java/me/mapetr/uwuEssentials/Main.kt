package me.mapetr.uwuEssentials

import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.PaperCommandManager
import me.mapetr.uwuEssentials.commands.Back
import me.mapetr.uwuEssentials.commands.Kill
import me.mapetr.uwuEssentials.commands.Spectator
import me.mapetr.uwuEssentials.commands.home.DelHome
import me.mapetr.uwuEssentials.commands.home.Home
import me.mapetr.uwuEssentials.commands.home.Homes
import me.mapetr.uwuEssentials.commands.home.SetHome
import me.mapetr.uwuEssentials.commands.teleport.Teleport
import me.mapetr.uwuEssentials.commands.teleport.TeleportHere
import me.mapetr.uwuEssentials.commands.warp.DelWarp
import me.mapetr.uwuEssentials.commands.warp.Warp
import me.mapetr.uwuEssentials.commands.warp.SetWarp
import me.mapetr.uwuEssentials.commands.warp.Warps
import me.mapetr.uwuEssentials.services.ChatService
import me.mapetr.uwuEssentials.services.PlayerListManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException

class Main : JavaPlugin(), Listener {
    var _playerListManager: PlayerListManager = PlayerListManager(config)
    var _chatService: ChatService = ChatService(config)
    override fun onEnable() {
        saveDefaultConfig()

        Database.connect()

        try {
            val connection = Database.dataSource.connection
            val statement = connection.createStatement()
            statement.addBatch("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(255) PRIMARY KEY, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255))")
            statement.addBatch("CREATE TABLE IF NOT EXISTS back (name VARCHAR(255) PRIMARY KEY, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255))")
            statement.addBatch("CREATE TABLE IF NOT EXISTS homes (player VARCHAR(255), name VARCHAR(255), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255), PRIMARY KEY (player, name))")
            statement.executeBatch()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        val manager = PaperCommandManager(this)
        manager.registerCommand(Spectator())
        manager.registerCommand(Kill())

        manager.registerCommand(Teleport())
        manager.registerCommand(TeleportHere())

        manager.registerCommand(Warps())
        manager.registerCommand(Warp())
        manager.registerCommand(SetWarp())
        manager.registerCommand(DelWarp())

        manager.registerCommand(Homes())
        manager.registerCommand(Home())
        manager.registerCommand(SetHome())
        manager.registerCommand(DelHome())

        manager.registerCommand(Back())
        manager.enableUnstableAPI("help")

        val completions = manager.commandCompletions
//        completions.registerAsyncCompletion("warps") { c: BukkitCommandCompletionContext? ->
//            try {
//                return@registerAsyncCompletion DB.getFirstColumnResults<String>("SELECT name FROM warps")
//            } catch (e: SQLException) {
//                throw RuntimeException(e)
//            }
//        }
//        completions.registerAsyncCompletion("homes") { c: BukkitCommandCompletionContext? ->
//            try {
//                return@registerAsyncCompletion DB.getFirstColumnResults<String>("SELECT name FROM homes WHERE player = ?", c?.player?.uniqueId.toString())
//            } catch (e: SQLException) {
//                throw RuntimeException(e)
//            }
//        }

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
        }, config.getLong("tab_refresh"), config.getLong("tab_refresh"))
        config.options().copyDefaults(true)
        saveConfig()
    }

    override fun onDisable() {
        Database.dataSource.close()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        _playerListManager.reloadPlayerList(MiniMessage.miniMessage(), event.player)
    }

    @EventHandler
    fun onMessageSent(event: AsyncPlayerChatEvent) {
        event.isCancelled = _chatService.processMessage(event, MiniMessage.miniMessage())
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent){
        try {
            val connection = Database.dataSource.connection
            val statement = connection.createStatement()
            statement.executeUpdate("INSERT OR REPLACE INTO back (name, x, y, z , yaw, pitch, world) VALUES (${event.entity.uniqueId}, ${event.entity.location.x}, ${event.entity.location.y}, ${event.entity.location.z}, ${event.entity.location.yaw}, ${event.entity.location.pitch}, ${event.entity.location.world.name})")
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}
