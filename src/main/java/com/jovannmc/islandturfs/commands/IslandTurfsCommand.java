package com.jovannmc.islandturfs.commands;

import com.jovannmc.islandturfs.managers.GameManager;
import com.jovannmc.islandturfs.managers.TeamManager;
import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
            sender.sendMessage(utils.color("&3IslandTurfs &7v" + plugin.getDescription().getVersion() + " &3by &7" + plugin.getDescription().getAuthors()));
            sender.sendMessage(utils.color("&7&o" + plugin.getDescription().getDescription()));
            return true;
        }
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
                utils.invalidPermission(sender);
            }
        /*
            TEAM SUBCOMMANDS
        */
        } else if (args[0].equalsIgnoreCase("team")) {
            if (args.length == 1) {
                utils.invalidUsage(sender, "/islandturfs team <join/leave/ready>");
                return false;
            }

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
                            plugin.messages.getConfiguration().getString("invalidPlayer")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // If specified map does not exist
                } else if (!args[3].equalsIgnoreCase("ITC_1") && !args[3].equalsIgnoreCase("ITC_2")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidMap")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                    // If specified team does not exist
                } else if (!args[2].equalsIgnoreCase("red") && !args[2].equalsIgnoreCase("blue")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidTeam")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                }

                Player target = Bukkit.getPlayer(args[4]);
                if (args[2].equalsIgnoreCase("red")) {
                    // If player is already in the blue team
                    if (TeamManager.blueTeam.containsKey(target.getUniqueId())) {
                        TeamManager.blueTeam.remove(target.getUniqueId());
                        target.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamChanged")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                        .replace("%originalteam%", "blue")
                                        .replace("%newteam%", "red")));
                        TeamManager.redTeam.put(target.getUniqueId(), args[3]);
                        giveItems(target, "red", args[3]);
                        return true;
                    }
                    // Place player in red team and send messages
                    TeamManager.redTeam.put(target.getUniqueId(), args[3]);
                    target.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("teamSelected")
                                    .replace("%team%", "red")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    giveItems(target, "red", args[3]);
                    Bukkit.getLogger().info("Added " + target.getName() + " to the red team!");
                    Bukkit.getLogger().info("Red team size: " + TeamManager.redTeam.size());
                    Bukkit.getLogger().info("Red team: " + TeamManager.redTeam);
                } else if (args[2].equalsIgnoreCase("blue")) {
                    // Check if player is already in the red team
                    if (TeamManager.redTeam.containsKey(target.getUniqueId())) {
                        TeamManager.redTeam.remove(target.getUniqueId());
                        target.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("teamChanged")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                        .replace("%originalteam%", "red")
                                        .replace("%newteam%", "blue")));
                        TeamManager.blueTeam.put(target.getUniqueId(), args[3]);
                        giveItems(target, "blue", args[3]);
                        return true;
                    }
                    // Place player in the blue team and send messages
                    TeamManager.blueTeam.put(target.getUniqueId(), args[3]);
                    target.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("teamSelected")
                                    .replace("%team%", "blue")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    giveItems(target, "blue", args[3]);
                    Bukkit.getLogger().info("Added " + target.getName() + " to the blue team!");
                    Bukkit.getLogger().info("Blue team size: " + TeamManager.blueTeam.size());
                    Bukkit.getLogger().info("Blue team: " + TeamManager.blueTeam);
                } else {
                    utils.invalidUsage(sender, "/islandturfs team join <team> <map> <player>");
                    return false;
                }
            /*
                READY COMMAND
            */
            } else if (args[1].equalsIgnoreCase("ready")) {
                // If args length is not 4
                if (args.length != 4) {
                    utils.invalidUsage(sender, "/islandturfs team ready <team> <map>");
                    return false;
                    // If specified map does not exist
                } else if (!args[3].equalsIgnoreCase("ITC_1") && !args[3].equalsIgnoreCase("ITC_2")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidMap")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                    // If specified team does not exist
                } else if (!args[2].equalsIgnoreCase("red") && !args[2].equalsIgnoreCase("blue")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidTeam")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
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

                    if (args[3].equalsIgnoreCase("ITC_1")) {
                        // If red team is ready
                        if (TeamManager.ITC_1_redReady) {
                            // Unready team
                            TeamManager.ITC_1_redReady = false;
                            for (UUID uuid : TeamManager.redTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "red")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
                        }
                        // If red team is not ready
                        TeamManager.ITC_1_redReady = true;
                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.ITC_1_redReady && TeamManager.ITC_1_blueReady) {
                            sender.sendMessage("Both teams are ready! Starting game on ITC_1...");
                            startCountdown(sender, args[3]);
                        }
                        return false;
                    } else if (args[3].equalsIgnoreCase("ITC_2")) {
                        // If red team is ready
                        if (TeamManager.ITC_2_redReady) {
                            // Unready team
                            TeamManager.ITC_2_redReady = false;
                            for (UUID uuid : TeamManager.redTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "red")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
                        }
                        // If red team is not ready
                        TeamManager.ITC_2_redReady = true;
                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "red")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.ITC_2_redReady && TeamManager.ITC_2_blueReady) {
                            sender.sendMessage("Both teams are ready! Starting game on ITC_2...");
                            startCountdown(sender, args[3]);
                        }
                        return false;
                    }
                } else if (args[2].equalsIgnoreCase("blue")) {
                    // If no players in blue team
                    if (TeamManager.blueTeam.size() == 0) {
                        sender.sendMessage(utils.color(
                                plugin.messages.getConfiguration().getString("noPlayersInTeam")
                                        .replace("%team%", "blue")
                                        .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        return false;
                    }

                    if (args[3].equalsIgnoreCase("ITC_1")) {
                        // If blue team is ready
                        if (TeamManager.ITC_1_blueReady) {
                            // Unready team
                            TeamManager.ITC_1_blueReady = false;
                            // For each player in blue team
                            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "blue")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
                            return false;
                        }
                        // If blue team is not ready
                        TeamManager.ITC_1_blueReady = true;
                        // For each player in blue team
                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.ITC_1_redReady && TeamManager.ITC_1_blueReady) {
                            startCountdown(sender, args[3]);
                        }
                    } else if (args[3].equalsIgnoreCase("ITC_2")) {
                        // If blue team is ready
                        if (TeamManager.ITC_2_blueReady) {
                            // Unready team
                            TeamManager.ITC_2_blueReady = false;
                            // For each player in blue team
                            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                                // Check if player's key matches map name
                                if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                    Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                            plugin.messages.getConfiguration().getString("teamUnready")
                                                    .replace("%team%", "blue")
                                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                                }
                            }
                            return false;
                        }
                        // If blue team is not ready
                        TeamManager.ITC_2_blueReady = true;
                        // For each player in blue team
                        for (UUID uuid : TeamManager.blueTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("teamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        // For each player in red team
                        for (UUID uuid : TeamManager.redTeam.keySet()) {
                            // Check if player's key matches map name
                            if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(args[3])) {
                                Bukkit.getPlayer(uuid).sendMessage(utils.color(
                                        plugin.messages.getConfiguration().getString("otherTeamReady")
                                                .replace("%team%", "blue")
                                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                            }
                        }

                        if (TeamManager.ITC_2_redReady && TeamManager.ITC_2_blueReady) {
                            startCountdown(sender, args[3]);
                        }
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
                            plugin.messages.getConfiguration().getString("invalidPlayer")
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
                    target.getInventory().clear();
                    // If player is in blue team
                } else if (TeamManager.blueTeam.containsKey(target.getUniqueId())) {
                    // Remove player from blue team and send message
                    TeamManager.blueTeam.remove(target.getUniqueId());
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("teamLeft")
                                    .replace("%team%", "blue")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    target.getInventory().clear();
                    // If player is not in a team
                } else {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration()
                                    .getString("noTeam")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                }
            } else {
                utils.invalidUsage(sender, "/islandturfs team <join/leave/ready>");
                return false;
            }
        /*
            GAME SUBCOMMANDS
        */
        } else if (args[0].equalsIgnoreCase("game")) {
            if (args.length == 1) {
                utils.invalidUsage(sender, "/islandturfs game <start/end/spectate>");
                return false;
            }

            /*
                START COMMAND
            */
            if (args[1].equalsIgnoreCase("start")) {
                if (args.length == 3) {
                    GameManager gameManager = new GameManager();
                    gameManager.startGame(args[2]);
                } else {
                    utils.invalidUsage(sender, "/islandturfs game start <map>");
                    return false;
                }

                // If specified map does not exist
                if (!args[2].equalsIgnoreCase("ITC_1") && !args[2].equalsIgnoreCase("ITC_2")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidMap")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                }
            /*
                END COMMAND
            */
            } else if (args[1].equalsIgnoreCase("end")) {
                if (args.length == 4) {
                    GameManager gameManager = new GameManager();
                    gameManager.endGame(args[2].toLowerCase(), args[3]);
                } else {
                    utils.invalidUsage(sender, "/islandturfs game end <map> <winningTeam>");
                    return false;
                }

                // If specified map does not exist
                if (!args[2].equalsIgnoreCase("ITC_1") && !args[2].equalsIgnoreCase("ITC_2")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidMap")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                    // If specified team does not exist
                } else if (!args[3].equalsIgnoreCase("red") && !args[3].equalsIgnoreCase("blue")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidTeam")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
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
                    return false;
                }

                // If specified map does not exist
                if (!args[2].equalsIgnoreCase("ITC_1") && !args[2].equalsIgnoreCase("ITC_2")) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidMap")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                    // If specified player does not exist
                } else if (Bukkit.getPlayer(args[3]) == null) {
                    sender.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("invalidPlayer")
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    return false;
                }
            } else {
                utils.invalidUsage(sender, "/islandturfs game <start/end/spectate>");
                return false;
            }
        } else {
            utils.invalidUsage(sender, "/islandturfs <team/game/reload>");
            return false;
        }
        return false;
    }

    private void giveItems(Player player, String team, String mapName) {
        Bukkit.getLogger().info("Creating items for " + mapName + "...");

        // Create an unbreakable iron sword
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemMeta ironSwordMeta = ironSword.getItemMeta();
        ironSwordMeta.setUnbreakable(true);
        ironSword.setItemMeta(ironSwordMeta);

        // Create unbreakable shears
        ItemStack shears = new ItemStack(Material.SHEARS);
        ItemMeta shearsMeta = shears.getItemMeta();
        shearsMeta.setUnbreakable(true);
        shears.setItemMeta(shearsMeta);

        // Create the unbreakable bow with punch 2 and infinity
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setUnbreakable(true);
        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
        bow.setItemMeta(bowMeta);

        // If the player is on the blue team
        if (team.equalsIgnoreCase("red")) {
            Bukkit.getLogger().info(player.getName() + " is on the red team, giving them red items...");
            // Create the full set of unbreakable red leather armor
            ItemStack redLeatherHelmet = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta redLeatherHelmetMeta = (LeatherArmorMeta) redLeatherHelmet.getItemMeta();
            redLeatherHelmetMeta.setColor(Color.RED);
            redLeatherHelmet.setItemMeta(redLeatherHelmetMeta);
            redLeatherHelmetMeta.setUnbreakable(true);

            ItemStack redLeatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta redLeatherChestplateMeta = (LeatherArmorMeta) redLeatherChestplate.getItemMeta();
            redLeatherChestplateMeta.setColor(Color.RED);
            redLeatherChestplate.setItemMeta(redLeatherChestplateMeta);
            redLeatherChestplateMeta.setUnbreakable(true);

            ItemStack redLeatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta redLeatherLeggingsMeta = (LeatherArmorMeta) redLeatherLeggings.getItemMeta();
            redLeatherLeggingsMeta.setColor(Color.RED);
            redLeatherLeggings.setItemMeta(redLeatherLeggingsMeta);
            redLeatherLeggingsMeta.setUnbreakable(true);

            ItemStack redLeatherBoots = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta redLeatherBootsMeta = (LeatherArmorMeta) redLeatherBoots.getItemMeta();
            redLeatherBootsMeta.setColor(Color.RED);
            redLeatherBoots.setItemMeta(redLeatherBootsMeta);
            redLeatherBootsMeta.setUnbreakable(true);

            // Clear the player's inventory
            player.getInventory().clear();

            // Give the player the items
            player.getInventory().setItem(0, ironSword);
            player.getInventory().setItem(1, bow);
            player.getInventory().setItem(2, forgeCanDestroyItem(shears, "minecraft:red_wool", "minecraft:blue_wool"));
            player.getInventory().setItem(3, forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
            player.getInventory().setItem(4, forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
            player.getInventory().setItem(6, new ItemStack(Material.ARROW));
            player.getInventory().setHelmet(redLeatherHelmet);
            player.getInventory().setChestplate(redLeatherChestplate);
            player.getInventory().setLeggings(redLeatherLeggings);
            player.getInventory().setBoots(redLeatherBoots);
            player.getInventory().setItemInOffHand(forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));

            // If the player is in the blue team
        } else if (team.equalsIgnoreCase("blue")) {

            // Create the full set of unbreakable blue leather armor
            ItemStack blueLeatherHelmet = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta blueLeatherHelmetMeta = (LeatherArmorMeta) blueLeatherHelmet.getItemMeta();
            blueLeatherHelmetMeta.setColor(Color.BLUE);
            blueLeatherHelmet.setItemMeta(blueLeatherHelmetMeta);
            blueLeatherHelmetMeta.setUnbreakable(true);

            ItemStack blueLeatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta blueLeatherChestplateMeta = (LeatherArmorMeta) blueLeatherChestplate.getItemMeta();
            blueLeatherChestplateMeta.setColor(Color.BLUE);
            blueLeatherChestplate.setItemMeta(blueLeatherChestplateMeta);
            blueLeatherChestplateMeta.setUnbreakable(true);

            ItemStack blueLeatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta blueLeatherLeggingsMeta = (LeatherArmorMeta) blueLeatherLeggings.getItemMeta();
            blueLeatherLeggingsMeta.setColor(Color.BLUE);
            blueLeatherLeggings.setItemMeta(blueLeatherLeggingsMeta);
            blueLeatherLeggingsMeta.setUnbreakable(true);

            ItemStack blueLeatherBoots = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta blueLeatherBootsMeta = (LeatherArmorMeta) blueLeatherBoots.getItemMeta();
            blueLeatherBootsMeta.setColor(Color.BLUE);
            blueLeatherBoots.setItemMeta(blueLeatherBootsMeta);
            blueLeatherBootsMeta.setUnbreakable(true);

            // Clear the player's inventory
            player.getInventory().clear();

            // Give the player the items
            player.getInventory().setItem(0, ironSword);
            player.getInventory().setItem(1, bow);
            player.getInventory().setItem(2, forgeCanDestroyItem(shears, "minecraft:red_wool", "minecraft:blue_wool"));
            player.getInventory().setItem(3, forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
            player.getInventory().setItem(4, forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
            player.getInventory().setItem(6, new ItemStack(Material.ARROW));
            player.getInventory().setHelmet(blueLeatherHelmet);
            player.getInventory().setChestplate(blueLeatherChestplate);
            player.getInventory().setLeggings(blueLeatherLeggings);
            player.getInventory().setBoots(blueLeatherBoots);
            player.getInventory().setItemInOffHand(forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));

        }
    }

    // Create "can" item
    private static ItemStack forgeCanItem(ItemStack item, String can, String[] blocks) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        NBTItem nbt = new NBTItem(item);
        NBTList<String> canNbt = nbt.getStringList(can);
        for (int i = 0; i < blocks.length; i++) {
            canNbt.add(blocks[i]);
        }
        return nbt.getItem();
    }

    // Create "canPlaceOn" item
    public static ItemStack forgeCanBePlacedOnItem(ItemStack item, String... blocks) {
        return forgeCanItem(item, "CanPlaceOn", blocks);
    }

    // Create "canDestroy" item
    public static ItemStack forgeCanDestroyItem(ItemStack item, String... blocks) {
        return forgeCanItem(item, "CanDestroy", blocks);
    }

    private void startCountdown(CommandSender sender, String map) {
        if (!map.equalsIgnoreCase("ITC_1") && !map.equalsIgnoreCase("ITC_2")) {
            sender.sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("invalidMap")
                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            return;
        }

        // Start countdown
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i < plugin.config.getConfiguration().getInt("timeBeforeStart")) {
                    // For each player in red team
                    for (UUID uuid : TeamManager.redTeam.keySet()) {
                        // If player's value matches map
                        if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(map)) {
                            Player player = Bukkit.getPlayer(uuid);
                            player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("gameStarting")
                                    .replace("%time%", String.valueOf(plugin.config.getConfiguration().getInt("timeBeforeStart") - i))
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                        }
                    }
                    // For each player in blue team
                    for (UUID uuid : TeamManager.blueTeam.keySet()) {
                        // If player's value matches map
                        if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(map)) {
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
                if (map.equalsIgnoreCase("ITC_1")) {
                    TeamManager.ITC_1_redReady = false;
                    TeamManager.ITC_1_blueReady = false;
                } else if (map.equalsIgnoreCase("ITC_2")) {
                    TeamManager.ITC_2_redReady = false;
                    TeamManager.ITC_2_blueReady = false;
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
