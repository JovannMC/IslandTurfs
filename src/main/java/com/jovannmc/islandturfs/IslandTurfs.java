package com.jovannmc.islandturfs;

import com.jovannmc.islandturfs.commands.IslandTurfsCommand;
import com.jovannmc.islandturfs.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IslandTurfs extends JavaPlugin {

    private static IslandTurfs instance;

    public Config config;
    public Config messages;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config(this, "config.yml", null);
        config.saveDefaultConfig();
        messages = new Config(this, "messages.yml", null);
        messages.saveDefaultConfig();

        Bukkit.getPluginCommand("IslandTurfs").setExecutor(new IslandTurfsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static IslandTurfs getInstance() {
        return instance;
    }
}
