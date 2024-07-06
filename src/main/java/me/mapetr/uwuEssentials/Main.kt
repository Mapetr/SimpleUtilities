package me.mapetr.uwuEssentials

import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.PaperCommandManager
import me.mapetr.uwuEssentials.commands.Back
import me.mapetr.uwuEssentials.commands.Chunks
import me.mapetr.uwuEssentials.commands.Kill
import me.mapetr.uwuEssentials.commands.Spectator
import me.mapetr.uwuEssentials.commands.home.DelHome
import me.mapetr.uwuEssentials.commands.home.Home
import me.mapetr.uwuEssentials.commands.home.Homes
import me.mapetr.uwuEssentials.commands.home.SetHome
import me.mapetr.uwuEssentials.commands.teleport.Teleport
import me.mapetr.uwuEssentials.commands.teleport.TeleportHere
import me.mapetr.uwuEssentials.commands.warp.DelWarp
import me.mapetr.uwuEssentials.commands.warp.SetWarp
import me.mapetr.uwuEssentials.commands.warp.Warp
import me.mapetr.uwuEssentials.commands.warp.Warps
import me.mapetr.uwuEssentials.services.ChatService
import me.mapetr.uwuEssentials.services.PlayerListManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException

class Main : JavaPlugin(), Listener {
    var _playerListManager: PlayerListManager = PlayerListManager(config)
    var _chatService: ChatService = ChatService(config)

    companion object {
        lateinit var instance: Main
    }

    override fun onEnable() {
        instance = this

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

        manager.registerCommand(Chunks())
        manager.enableUnstableAPI("help")

        val completions = manager.commandCompletions
        completions.registerAsyncCompletion("warps") { c: BukkitCommandCompletionContext? ->
            try {
                return@registerAsyncCompletion Data.warps.keys.toList()
            } catch (e: SQLException) {
                throw RuntimeException(e)
            }
        }
        completions.registerAsyncCompletion("homes") { c: BukkitCommandCompletionContext? ->
            try {
                val player = c?.player?.uniqueId.toString()
                val homes = Data.homes[player]?.keys?.toMutableList() ?: mutableListOf()

                homes.remove("home")

                return@registerAsyncCompletion homes
            } catch (e: SQLException) {
                throw RuntimeException(e)
            }
        }

        try {
            val connection = Database.dataSource.connection
            val statement = connection.createStatement()
            val warpRow = statement.executeQuery("SELECT * FROM warps")
            while (warpRow.next()) {
                val loc = Location(
                    Bukkit.getWorld(warpRow.getString("world")),
                    warpRow.getDouble("x"),
                    warpRow.getDouble("y"),
                    warpRow.getDouble("z"),
                    warpRow.getFloat("yaw"),
                    warpRow.getFloat("pitch")
                )

                Data.warps[warpRow.getString("name")] = loc
            }
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        this.saveDefaultConfig()
        server.pluginManager.registerEvents(Main(), this)
        val msg = MiniMessage.miniMessage()
        // TODO: Move this to the default config.yml file
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

        try {
            val connection = Database.dataSource.connection
            val statement = connection.createStatement()
            val homeRow =
                statement.executeQuery("SELECT * FROM homes WHERE player = '${event.player.uniqueId}'")
            Data.homes[event.player.uniqueId.toString()] = HashMap()
            while (homeRow.next()) {
                val loc = Location(
                    Bukkit.getWorld(homeRow.getString("world")),
                    homeRow.getDouble("x"),
                    homeRow.getDouble("y"),
                    homeRow.getDouble("z"),
                    homeRow.getFloat("yaw"),
                    homeRow.getFloat("pitch")
                )

                Data.homes[event.player.uniqueId.toString()]?.set(homeRow.getString("name"), loc)
            }

            val backRow =
                statement.executeQuery("SELECT * FROM back WHERE name = '${event.player.uniqueId}'")
            while (backRow.next()) {
                val loc = Location(
                    Bukkit.getWorld(backRow.getString("world")),
                    backRow.getDouble("x"),
                    backRow.getDouble("y"),
                    backRow.getDouble("z"),
                    backRow.getFloat("yaw"),
                    backRow.getFloat("pitch")
                )

                Data.back[event.player.uniqueId.toString()] = loc
            }

            statement.close()
            connection.close()

        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Data.homes.remove(event.player.uniqueId.toString())
        Data.back.remove(event.player.uniqueId.toString())
    }

    @EventHandler
    fun onMessageSent(event: AsyncPlayerChatEvent) {
        event.isCancelled = _chatService.processMessage(event, MiniMessage.miniMessage())
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent){
        try {
            Database.executeAsync("UPDATE back SET x = ${event.entity.location.x}, y = ${event.entity.location.y}, z = ${event.entity.location.z}, yaw = ${event.entity.location.yaw}, pitch = ${event.entity.location.pitch}, world = ${event.entity.world.name} WHERE name = '${event.entity.uniqueId.toString()}'")
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}
