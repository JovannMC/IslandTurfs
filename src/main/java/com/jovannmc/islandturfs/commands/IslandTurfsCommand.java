package com.jovannmc.islandturfs.commands;

import com.jovannmc.islandturfs.managers.GameManager;
import com.jovannmc.islandturfs.managers.TeamManager;
import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class IslandTurfsCommand implements CommandExecutor {

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    TeamManager TeamManager = new TeamManager();

    Configuration messages = plugin.messages.getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(utils.color("&6IslandTurfs v" + plugin.getDescription().getVersion()));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("islandturfs.command.reload")) {
                plugin.config.reloadConfig();
                plugin.messages.reloadConfig();
                plugin.maps.reloadConfig();
                Bukkit.getLogger().info("Reloaded the configs!");
                sender.sendMessage(utils.color(plugin.getConfig().getString("prefix") + " &6Reloaded the configs!"));
            } else {
                utils.noPermission(sender);
            }
            /*
                TEAM SUBCOMMANDS
            */
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("team")) {
                /*
                    JOIN COMMAND
                */
            if (args[1].equalsIgnoreCase("join")) {
                if (args.length == 4) {
                    if (args[2].equalsIgnoreCase("red")) {
                        TeamManager.redTeam.put(((Player) sender).getUniqueId(), args[3]);
                        sender.sendMessage(utils.color(messages.getString("teamSelected").replace("%team%", "red")));
                        Bukkit.getLogger().info("Added " + ((Player) sender).getName() + " to the red team!");
                        Bukkit.getLogger().info("Red team size: " + TeamManager.redTeam.size());
                    } else if (args[2].equalsIgnoreCase("blue")) {
                        TeamManager.blueTeam.put(((Player) sender).getUniqueId(), args[3]);
                        sender.sendMessage(utils.color(messages.getString("teamSelected").replace("%team%", "blue")));
                        Bukkit.getLogger().info("Added " + ((Player) sender).getName() + " to the blue team!");
                        Bukkit.getLogger().info("Blue team size: " + TeamManager.blueTeam.size());
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
                            Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamUnready").replace("%team%", "red")));
                        }
                        Bukkit.getLogger().info("Red team is no longer ready!");
                        return false;
                    }
                    // If red team is not ready
                    TeamManager.redReady = true;
                    for (UUID uuid : TeamManager.redTeam.keySet()) {
                        Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamReady").replace("%team%", "red")));
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
                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamUnready").replace("%team%", "blue")));
                        }
                        return false;
                    }
                    // If blue team is not ready
                    TeamManager.blueReady = true;
                    for (UUID uuid : TeamManager.blueTeam.keySet()) {
                        Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamReady").replace("%team%", "blue")));
                    }

                    if (TeamManager.redReady && TeamManager.blueReady) { StartGame(args[3]); }
                }

                /*
                    LEAVE COMMAND
                */
            } else if (args[1].equalsIgnoreCase("leave")) {
                if (TeamManager.redTeam.containsKey(((Player) sender).getUniqueId())) {
                    TeamManager.redTeam.remove(((Player) sender).getUniqueId());
                    sender.sendMessage(utils.color(messages.getString("teamLeft").replace("%team%", "red")));
                } else if (TeamManager.blueTeam.containsKey(((Player) sender).getUniqueId())) {
                    TeamManager.blueTeam.remove(((Player) sender).getUniqueId());
                    sender.sendMessage(utils.color(messages.getString("teamLeft").replace("%team%", "blue")));
                } else {
                    sender.sendMessage(utils.color(messages.getString("notInTeam")));
                }

            } else {
                utils.invalidUsage(sender, cmd);
            }

            /*
                GAME SUBCOMMANDS
            */
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("game")) {
            // TODO: game logic
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

        return false;
    }

    private void StartGame(String map)
    {
        // for every player in red team
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if player's value matches map
            if (TeamManager.redTeam.get(uuid).equals(map)) {
                // send message to player
                Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("gameStarting")));
            }

        }
        // for every player in blue team
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // if player's value matches map
            if (TeamManager.blueTeam.get(uuid).equals(map)) {
                Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("gameStarting")));
            }
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "islandturfs game start " + map);
        TeamManager.redReady = false;
        TeamManager.blueReady = false;
    }

}
