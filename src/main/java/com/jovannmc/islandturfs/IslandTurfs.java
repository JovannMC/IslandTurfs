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
    - Add logic to add new maps
    - Add a config option to add new maps
    (basically don't hardcode everything)
     */

    private static IslandTurfs instance;
    public Config config;
    public Config messages;
    public Config maps;

    @Override
    public void onEnable() {
        instance = this;

        this.getLogger().info("Starting up...");

        this.getLogger().info("Loading configs...");
        config = new Config(this, "config.yml", null);
        config.saveDefaultConfig();
        messages = new Config(this, "messages.yml", null);
        messages.saveDefaultConfig();
        maps = new Config(this, "maps.yml", null);
        maps.saveDefaultConfig();

        this.getLogger().info("Registering commands...");
        Bukkit.getPluginCommand("IslandTurfs").setExecutor(new IslandTurfsCommand());
        Bukkit.getPluginCommand("IslandTurfs").setTabCompleter(new IslandTurfsTabCompleter());

        this.getLogger().info("Registering events...");
        Bukkit.getPluginManager().registerEvents(new GameManager(), this);
        Bukkit.getPluginManager().registerEvents(new TeamManager(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Shutting down...");
        TeamManager.redTeam.clear();
        TeamManager.blueTeam.clear();
        TeamManager.spectators.clear();
        TeamManager.ITC_1_redReady = false;
        TeamManager.ITC_1_blueReady = false;
        TeamManager.ITC_2_redReady = false;
        TeamManager.ITC_2_blueReady = false;
        GameManager.ITC_1_gameStarted = false;
        GameManager.ITC_2_gameStarted = false;
    }

    public static IslandTurfs getInstance() {
        return instance;
    }
}
