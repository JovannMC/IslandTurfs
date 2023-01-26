package com.jovannmc.islandturfs.commands;

import com.jovannmc.islandturfs.managers.GameManager;
import com.jovannmc.islandturfs.managers.TeamManager;
import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class IslandTurfsCommand implements CommandExecutor {

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    TeamManager TeamManager = new TeamManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(utils.color("&3IslandTurfs &7v" + plugin.getDescription().getVersion()));
        } else if (args.length > 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("islandturfs.command.reload")) {
                    plugin.config.reloadConfig();
                    plugin.messages.reloadConfig();
                    plugin.maps.reloadConfig();
                    Bukkit.getLogger().info("Reloaded the configs!");
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("reloadConfig")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                } else {
                    utils.noPermission(sender);
                }
            /*
                TEAM SUBCOMMANDS
            */
            } else if (args[0].equalsIgnoreCase("team")) {
                /*
                    JOIN COMMAND
                */
                if (args[1].equalsIgnoreCase("join")) {
                    if (args.length == 4) {
                        if (args[2].equalsIgnoreCase("red")) {
                            TeamManager.redTeam.put(((Player) sender).getUniqueId(), args[3]);
                            sender.sendMessage(utils.color(
                                    plugin.messages.getConfiguration().getString("teamSelected")
                                            .replace("%team%", "red")
                                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            Bukkit.getLogger().info("Added " + ((Player) sender).getName() + " to the red team!");
                            Bukkit.getLogger().info("Red team size: " + TeamManager.redTeam.size());
                            Bukkit.getLogger().info("Red team: " + TeamManager.redTeam);
                        } else if (args[2].equalsIgnoreCase("blue")) {
                            TeamManager.blueTeam.put(((Player) sender).getUniqueId(), args[3]);
                            sender.sendMessage(utils.color(
                                    plugin.messages.getConfiguration().getString("teamSelected")
                                            .replace("%team%", "blue")
                                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            Bukkit.getLogger().info("Added " + ((Player) sender).getName() + " to the blue team!");
                            Bukkit.getLogger().info("Blue team size: " + TeamManager.blueTeam.size());
                            Bukkit.getLogger().info("Blue team: " + TeamManager.blueTeam);
                        } else {
                            utils.invalidUsage(sender, cmd);
                        }
                    } else {
                        utils.invalidUsage(sender, cmd);
                    }
                /*
                    READY COMMAND
                */
                } else if (args[1].equalsIgnoreCase("ready")) {
                    if (args.length != 4) {
                        utils.invalidUsage(sender, cmd);
                        return false;
                    }
                    if (args[2].equalsIgnoreCase("red")) {
                        // If no players in red team
                        if (TeamManager.redTeam.size() == 0) {
                            sender.sendMessage("There are no players in the red team!");
                            return false;
                        }
                        // If red team is ready
                        if (TeamManager.redReady) {
                            // Unready team
                            TeamManager.redReady = false;
                            for (UUID uuid : TeamManager.redTeam.keySet()) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamUnready")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                            Bukkit.getLogger().info("Red team is no longer ready!");
                            return false;
                        }
                        // If red team is not ready
                        TeamManager.redReady = true;
                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equals(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }
                        Bukkit.getLogger().info("Red team is ready!");

                        if (TeamManager.redReady && TeamManager.blueReady) {
                            sender.sendMessage("Both teams are ready! Starting game...");
                            StartGame(args[3]);
                        }
                        return false;
                    } else if (args[2].equalsIgnoreCase("blue")) {
                        // If no players in blue team
                        if (TeamManager.blueTeam.size() == 0) {
                            sender.sendMessage("There are no players in the blue team!");
                            return false;
                        }
                        // If blue team is ready
                        if (TeamManager.blueReady) {
                            // Unready team
                            TeamManager.blueReady = false;
                            // For each player in blue team
                            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.blueTeam.get(uuid).equals(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "blue")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
                            return false;
                        }
                        // If blue team is not ready
                        TeamManager.blueReady = true;
                        // For each player in blue team
                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                    plugin.messages.getConfiguration().getString("teamReady")
                                            .replace("%team%", "blue")
                                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        }

                        if (TeamManager.redReady && TeamManager.blueReady) {
                            StartGame(args[3]);
                        }
                    }

                /*
                    LEAVE COMMAND
                */
                } else if (args[1].equalsIgnoreCase("leave")) {
                    if (TeamManager.redTeam.containsKey(((Player) sender).getUniqueId())) {
                        TeamManager.redTeam.remove(((Player) sender).getUniqueId());
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamLeft")
                                        .replace("%team%", "red")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    } else if (TeamManager.blueTeam.containsKey(((Player) sender).getUniqueId())) {
                        TeamManager.blueTeam.remove(((Player) sender).getUniqueId());
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamLeft")
                                        .replace("%team%", "blue")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    } else {
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration()
                                        .getString("notInTeam")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }

                } else {
                    utils.invalidUsage(sender, cmd);
                }

            /*
                GAME SUBCOMMANDS
            */
            } else if (args[0].equalsIgnoreCase("game")) {
                /*
                    START COMMAND
                */
                if (args[1].equalsIgnoreCase("start")) {
                    if (args.length == 3) {
                        GameManager gameManager = new GameManager();
                        gameManager.startGame(args[2]);
                    } else {
                        utils.invalidUsage(sender, cmd);
                    }
                /*
                    END COMMAND
                */
                } else if (args[1].equalsIgnoreCase("end")) {
                    if (args.length == 4) {
                        GameManager gameManager = new GameManager();
                        gameManager.endGame(args[2], args[3]);
                    } else {
                        utils.invalidUsage(sender, cmd);
                    }
                } else {
                    utils.invalidUsage(sender, cmd);
                }
            } else {
                utils.invalidUsage(sender, cmd);
            }
        } else {
            utils.invalidUsage(sender, cmd);
        }

        return false;
    }

    private void StartGame(String map) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i < plugin.config.getConfiguration().getInt("timeBeforeStart")) {
                    // For each player in red team
                    for (UUID uuid : TeamManager.redTeam.keySet()) {
                        // If player's value matches map
                        if (TeamManager.redTeam.get(uuid).equals(map)) {
                            Player player = Bukkit.getPlayer(uuid);
                            player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("gameStarting")
                                    .replace("%time%", String.valueOf(plugin.config.getConfiguration().getInt("timeBeforeStart") - i))
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));

                        }
                    }
                    // For each player in blue team
                    for (UUID uuid : TeamManager.blueTeam.keySet()) {
                        // If player's value matches map
                        if (TeamManager.blueTeam.get(uuid).equals(map)) {
                            Player player = Bukkit.getPlayer(uuid);
                            player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("gameStarting")
                                    .replace("%time%", String.valueOf(plugin.config.getConfiguration().getInt("timeBeforeStart") - i))
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));

                        }
                    }
                    i++;
                    return;
                }
                cancel();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "islandturfs game start " + map);
                TeamManager.redReady = false;
                TeamManager.blueReady = false;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
