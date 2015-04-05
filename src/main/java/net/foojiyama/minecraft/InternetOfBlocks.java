package net.foojiyama.minecraft;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class InternetOfBlocks extends JavaPlugin {

    private final Logger log;

    public InternetOfBlocks() {
        log = getLogger();
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        log.info("onDisable()");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
