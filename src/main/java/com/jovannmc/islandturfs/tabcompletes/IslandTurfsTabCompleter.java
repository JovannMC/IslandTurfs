package com.jovannmc.islandturfs.tabcompletes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class IslandTurfsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("islandturfs")) {
            if (args.length == 1) {
                List<String> commands = new ArrayList<>();
                commands.add("team");
                commands.add("game");
                if (sender.hasPermission("islandturfs.command.reload")) {
                    commands.add("reload");
                }
                return commands;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("team")) {
                    List<String> teamCommands = new ArrayList<>();
                    teamCommands.add("ready");
                    teamCommands.add("join");
                    teamCommands.add("leave");
                    return teamCommands;
                } else if (args[0].equalsIgnoreCase("game")) {
                    List<String> gameCommands = new ArrayList<>();
                    gameCommands.add("start");
                    gameCommands.add("end");
                    gameCommands.add("spectate");
                    return gameCommands;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("team")) {
                    if (args[1].equalsIgnoreCase("join")) {
                        List<String> teamColors = new ArrayList<>();
                        teamColors.add("red");
                        teamColors.add("blue");
                        return teamColors;
                    } else if (args[1].equalsIgnoreCase("ready")) {
                        List<String> teams = new ArrayList<>();
                        teams.add("red");
                        teams.add("blue");
                        return teams;
                    }
                } else if (args[0].equalsIgnoreCase("game")) {
                    if (args[1].equalsIgnoreCase("start")) {
                        List<String> maps = new ArrayList<>();
                        maps.add("ITC_1");
                        maps.add("ITC_2");
                        return maps;
                    }
                    if (args[1].equalsIgnoreCase("end")) {
                        List<String> maps = new ArrayList<>();
                        maps.add("ITC_1");
                        maps.add("ITC_2");
                        return maps;
                    }
                    if (args[1].equalsIgnoreCase("spectate")) {
                        List<String> maps = new ArrayList<>();
                        maps.add("ITC_1");
                        maps.add("ITC_2");
                        return maps;
                    }
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("team")) {
                    if (args[1].equalsIgnoreCase("ready")) {
                        List<String> maps = new ArrayList<>();
                        maps.add("ITC_1");
                        maps.add("ITC_2");
                        return maps;
                    } else if (args[1].equalsIgnoreCase("join")) {
                        List<String> maps = new ArrayList<>();
                        maps.add("ITC_1");
                        maps.add("ITC_2");
                        return maps;
                    } else if (args[1].equalsIgnoreCase("end")) {
                        List<String> teams = new ArrayList<>();
                        teams.add("red");
                        teams.add("blue");
                        return teams;
                    }
                } else if (args[0].equalsIgnoreCase("game")) {
                    if (args[1].equalsIgnoreCase("end")) {
                        List<String> teams = new ArrayList<>();
                        teams.add("red");
                        teams.add("blue");
                        return teams;
                    }
                }
            }
        }
        return null;
    }

}






