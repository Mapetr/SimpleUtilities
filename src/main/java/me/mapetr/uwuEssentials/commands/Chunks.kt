package me.mapetr.uwuEssentials.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import me.mapetr.uwuEssentials.Message
import org.bukkit.entity.Player

@CommandAlias("chunks")
class Chunks: BaseCommand() {
    @Subcommand("list")
    fun listChunks(player: Player) {
        val loadedChunks = player.world.forceLoadedChunks
        val loadedChunksString = loadedChunks.joinToString(", ") { "(${it.x},${it.z})" }
        Message.sendMessage(player, "<green>Loaded chunks (${loadedChunks.size}): <white>$loadedChunksString")
    }

    @Subcommand("list")
    @Syntax("<world>")
    @CommandCompletion("@worlds")
    fun listChunks(player: Player, world: String) {
        val loadedChunks = player.server.getWorld(world)?.forceLoadedChunks
        if (loadedChunks == null) {
            Message.sendMessage(player, "<red>World not found")
            return
        }
        val loadedChunksString = loadedChunks.joinToString(", ") { "(${it.x},${it.z})" }
        Message.sendMessage(player, "<green>Loaded chunks (${loadedChunks.size}): <white>$loadedChunksString")
    }

    @Subcommand("add")
    fun addChunk(player: Player) {
        player.world.setChunkForceLoaded(player.location.chunk.x, player.location.chunk.z, true)
        Message.sendMessage(
            player,
            "<green>Chunk at <white>${player.location.chunk.x} ${player.location.chunk.z}</white> added"
        )
    }

    @Subcommand("add")
    @Syntax("<x> <z>")
    fun addChunk(player: Player, x: Int, z: Int) {
        player.world.setChunkForceLoaded(x, z, true)
        Message.sendMessage(player, "<green>Chunk at <white>$x $z</white> added")
    }

    @Subcommand("remove")
    fun removeChunk(player: Player) {
        player.world.setChunkForceLoaded(player.location.chunk.x, player.location.chunk.z, false)
        Message.sendMessage(
            player,
            "<green>Chunk at <white>${player.location.chunk.x} ${player.location.chunk.z}</white> removed"
        )
    }

    @Subcommand("remove")
    @Syntax("<x> <z>")
    fun removeChunk(player: Player, x: Int, z: Int) {
        player.world.setChunkForceLoaded(x, z, false)
        Message.sendMessage(player, "<green>Chunk at <white>$x $z</white> removed")
    }
}
