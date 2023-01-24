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

    /*
        TODO:
        - Team logic
            - Join teams
            - Leave teams
            - Ready up
        - Game logic
            - Start game
            - End game
            - Reset game
     */

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    TeamManager teamManager = new TeamManager();

    Configuration messages = plugin.messages.getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(utils.color("&6IslandTurfs v" + plugin.getDescription().getVersion()));
            Bukkit.getLogger().info("red team: " + teamManager.redReady + " blue team: " + teamManager.blueReady);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("islandturfs.command.reload")) {
                plugin.config.reloadConfig();
                plugin.messages.reloadConfig();
                sender.sendMessage(utils.color(plugin.getConfig().getString("prefix") + " &6Reloaded the config!"));
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
                        teamManager.redTeam.put(((Player) sender).getUniqueId(), args[3]);
                        sender.sendMessage(utils.color(messages.getString("teamSelected").replace("%team%", "red")));
                    } else if (args[2].equalsIgnoreCase("blue")) {
                        teamManager.blueTeam.put(((Player) sender).getUniqueId(), args[3]);
                        sender.sendMessage(utils.color(messages.getString("teamSelected").replace("%team%", "blue")));
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
                if (sender instanceof Player) {
                    utils.consoleOnly(sender);
                    return false;
                }
                if (args[2].equalsIgnoreCase("red")) {
                    // If no players in red team
                    if (teamManager.redTeam.size() == 0) {
                        Bukkit.getLogger().log(Level.WARNING, "from red: no players in red team");
                        return false;
                    }
                    // If red team is ready
                    if (teamManager.redReady) {
                        // Unready team
                        teamManager.redReady = false;
                        for (UUID uuid : teamManager.redTeam.keySet()) {
                            Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamUnready").replace("%team%", "red")));
                        }
                        Bukkit.getLogger().log(Level.INFO, "Red team: " + teamManager.redReady + " Blue team: " + teamManager.blueReady);
                        return false;
                    }
                    // If red team is not ready
                    teamManager.redReady = true;
                    for (UUID uuid : teamManager.redTeam.keySet()) {
                        Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamReady").replace("%team%", "red")));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Red team: " + teamManager.redReady + " Blue team: " + teamManager.blueReady);

                    if (teamManager.redReady && teamManager.blueReady) {
                        Bukkit.getLogger().info("from red: both teams are ready");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "islandturfs game start " + args[3]);
                        teamManager.redReady = false;
                        teamManager.blueReady = false;
                    }
                    return false;
                } else if (args[2].equalsIgnoreCase("blue")) {
                    // If no players in blue team
                    if (teamManager.blueTeam.size() == 0) {
                        Bukkit.getLogger().log(Level.WARNING, "from blue: no players in blue team");
                        return false;
                    }
                    // If blue team is ready
                    if (teamManager.blueReady) {
                        // Unready team
                        teamManager.blueReady = false;
                        for (UUID uuid : teamManager.blueTeam.keySet()) {
                            Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamUnready").replace("%team%", "blue")));
                        }
                        Bukkit.getLogger().log(Level.INFO, "Red team: " + teamManager.redReady + " Blue team: " + teamManager.blueReady);
                        return false;
                    }
                    // If blue team is not ready
                    teamManager.blueReady = true;
                    for (UUID uuid : teamManager.blueTeam.keySet()) {
                        Bukkit.getPlayer(uuid).sendMessage(utils.color(messages.getString("teamReady").replace("%team%", "blue")));
                    }
                    Bukkit.getLogger().log(Level.INFO, "Red team: " + teamManager.redReady + " Blue team: " + teamManager.blueReady);

                    if (teamManager.redReady && teamManager.blueReady) {
                        Bukkit.getLogger().info("from blue: both teams are ready");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "islandturfs game start " + args[3]);
                        teamManager.redReady = false;
                        teamManager.blueReady = false;
                    }
                }

                /*
                    LEAVE COMMAND
                */
            } else if (args[1].equalsIgnoreCase("leave")) {
                if (teamManager.redTeam.containsKey(((Player) sender).getUniqueId())) {
                    teamManager.redTeam.remove(((Player) sender).getUniqueId());
                    sender.sendMessage(utils.color(messages.getString("teamLeft").replace("%team%", "red")));
                } else if (teamManager.blueTeam.containsKey(((Player) sender).getUniqueId())) {
                    teamManager.blueTeam.remove(((Player) sender).getUniqueId());
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
                if (args.length == 3) {
                    GameManager gameManager = new GameManager();
                    gameManager.endGame(args[2]);
                } else {
                    utils.invalidUsage(sender, cmd);
                }
                /*
                    RESET COMMAND
                */
            } else if (args[1].equalsIgnoreCase("reset")) {
                if (args.length == 3) {
                    GameManager gameManager = new GameManager();
                    gameManager.resetGame(args[2]);
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
}
