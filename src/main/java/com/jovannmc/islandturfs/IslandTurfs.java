package com.jovannmc.islandturfs;

import com.jovannmc.islandturfs.commands.IslandTurfsCommand;
import com.jovannmc.islandturfs.managers.GameManager;
import com.jovannmc.islandturfs.managers.TeamManager;
import com.jovannmc.islandturfs.tabcompletes.IslandTurfsTabCompleter;
import com.jovannmc.islandturfs.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IslandTurfs extends JavaPlugin {

    /*
    TODO:
    - Actually create the game logic
    - Add logic to add new maps
    - Add a config option to add new maps
     */

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
        Bukkit.getPluginCommand("IslandTurfs").setTabCompleter(new IslandTurfsTabCompleter());

        Bukkit.getPluginManager().registerEvents(new GameManager(), this);
        Bukkit.getPluginManager().registerEvents(new TeamManager(), this);
    }

    @Override
    public void onDisable() {
        TeamManager.getInstance().redTeam.clear();
        TeamManager.getInstance().blueTeam.clear();
        TeamManager.getInstance().redReady = false;
        TeamManager.getInstance().blueReady = false;
    }

    public static IslandTurfs getInstance() {
        return instance;
    }
}
