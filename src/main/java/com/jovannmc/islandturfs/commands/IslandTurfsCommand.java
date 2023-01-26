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
            // Send info about the plugin
            sender.sendMessage(utils.color("&3IslandTurfs &7v" + plugin.getDescription().getVersion() + " &3by &7JovannMC"));
        } else if (args.length > 1) {
            /*
                RELOAD COMMAND
            */
            if (args[0].equalsIgnoreCase("reload")) {
                // Check if sender has permission
                if (sender.hasPermission("islandturfs.command.reload")) {
                    // Reload the configs and send messages
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
                    // If args length is not 5
                    if (args.length != 5) {
                        utils.invalidUsage(sender, "/islandturfs team join <team> <map> <player>");
                        return false;
                        // If specified player does not exist
                    } else if (Bukkit.getPlayer(args[4]) == null) {
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("playerNotFound")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }

                    Player target = Bukkit.getPlayer(args[4]);
                    if (args[2].equalsIgnoreCase("red")) {
                        // Place player in red team and send messages
                        TeamManager.redTeam.put(target.getUniqueId(), args[3]);
                        target.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamSelected")
                                        .replace("%team%", "red")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        Bukkit.getLogger().info("Added " + target.getName() + " to the red team!");
                        Bukkit.getLogger().info("Red team size: " + TeamManager.redTeam.size());
                        Bukkit.getLogger().info("Red team: " + TeamManager.redTeam);
                    } else if (args[2].equalsIgnoreCase("blue")) {
                        // Place player in the blue team and send messages
                        TeamManager.blueTeam.put(target.getUniqueId(), args[3]);
                        target.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamSelected")
                                        .replace("%team%", "blue")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        Bukkit.getLogger().info("Added " + target.getName() + " to the blue team!");
                        Bukkit.getLogger().info("Blue team size: " + TeamManager.blueTeam.size());
                        Bukkit.getLogger().info("Blue team: " + TeamManager.blueTeam);
                    } else {
                        utils.invalidUsage(sender, "/islandturfs team join <team> <map> <player>");
                    }
                /*
                    READY COMMAND
                */
                } else if (args[1].equalsIgnoreCase("ready")) {
                    // If args length is not 5
                    if (args.length != 4) {
                        utils.invalidUsage(sender, "/islandturfs team ready <team> <map>");
                        return false;
                    }

                    if (args[2].equalsIgnoreCase("red")) {
                        // If no players in red team
                        if (TeamManager.redTeam.size() == 0) {
                            sender.sendMessage(utils.color(
                                    plugin.messages.getConfiguration().getString("noPlayersInTeam")
                                            .replace("%team%", "red")
                                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            return false;
                        }
                        // If red team is ready
                        if (TeamManager.redReady) {
                            // Unready team
                            TeamManager.redReady = false;
                            for (UUID uuid : TeamManager.redTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.redTeam.get(uuid).equals(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "red")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
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

                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equals(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.redReady && TeamManager.blueReady) {
                            sender.sendMessage("Both teams are ready! Starting game...");
                            startCountdown(args[3]);
                        }
                        return false;
                    } else if (args[2].equalsIgnoreCase("blue")) {
                        // If no players in blue team
                        if (TeamManager.blueTeam.size() == 0) {
                            sender.sendMessage(utils.color(
                                    plugin.messages.getConfiguration().getString("noPlayersInTeam")
                                            .replace("%team%", "blue")
                                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
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
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equals(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equals(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.redReady && TeamManager.blueReady) {
                            startCountdown(args[3]);
                        }
                    }

                /*
                    LEAVE COMMAND
                */
                } else if (args[1].equalsIgnoreCase("leave")) {
                    // If args length is not 3
                    if (args.length != 3) {
                        utils.invalidUsage(sender, "/islandturfs team leave <player>");
                        return false;
                        // If specified player does not exist
                    } else if (Bukkit.getPlayer(args[2]) == null) {
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("playerNotFound")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    // If player is in red team
                    if (TeamManager.redTeam.containsKey(target.getUniqueId())) {
                        // Remove player from red team and send message
                        TeamManager.redTeam.remove(target.getUniqueId());
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamLeft")
                                        .replace("%team%", "red")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        // If player is in blue team
                    } else if (TeamManager.blueTeam.containsKey(target.getUniqueId())) {
                        // Remove player from blue team and send message
                        TeamManager.blueTeam.remove(target.getUniqueId());
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamLeft")
                                        .replace("%team%", "blue")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        // If player is not in a team
                    } else {
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration()
                                        .getString("notInTeam")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }
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
                        utils.invalidUsage(sender, "/islandturfs game start <map>");
                    }
                /*
                    END COMMAND
                */
                } else if (args[1].equalsIgnoreCase("end")) {
                    if (args.length == 4) {
                        GameManager gameManager = new GameManager();
                        gameManager.endGame(args[2], args[3]);
                    } else {
                        utils.invalidUsage(sender, "/islandturfs game end <map> <winningTeam>");
                    }
                /*
                    SPECTATE COMMAND
                */
                } else if (args[1].equalsIgnoreCase("spectate")) {
                    if (args.length == 4) {
                        GameManager gameManager = new GameManager();
                        gameManager.spectateGame(args[2], args[3]);
                    } else {
                        utils.invalidUsage(sender, "/islandturfs game spectate <map> <player>");
                    }
                } else {
                    utils.invalidUsage(sender, "/islandturfs game <start/end/spectate>");
                }
            } else {
                utils.invalidUsage(sender, "/islandturfs <team/game>");
            }
        } else {
            utils.invalidUsage(sender, "/islandturfs <team/game>");
        }

        return false;
    }

    private void startCountdown(String map) {
        // Start countdown
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
                // If countdown is over
                cancel();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "islandturfs game start " + map);
                TeamManager.redReady = false;
                TeamManager.blueReady = false;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
