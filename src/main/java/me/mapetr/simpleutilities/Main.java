package me.mapetr.simpleutilities;

import co.aikar.commands.PaperCommandManager;
import me.mapetr.simpleutilities.commands.KillCommand;
import me.mapetr.simpleutilities.commands.SpectatorCommand;
import me.mapetr.simpleutilities.commands.TeleportCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new SpectatorCommand());
        manager.registerCommand(new KillCommand());
        manager.registerCommand(new TeleportCommand());
        manager.enableUnstableAPI("help");
    }

    @Override
    public void onDisable() {
    }
}
