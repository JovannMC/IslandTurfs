package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import org.bukkit.Material;
import org.javatuples.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GameManager implements Listener {

    /*
        TODO: ability to instantiate GameManager and TeamManager classes to prevent issues with multiple games running at once
        - or at least make the code work with multiple games running at once without rewriting the code for both classes to support it
        - currently can only run two games at once, both ITC_1 and ITC_2 maps
    */

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    public static boolean ITC_1_gameStarted = false;
    public static boolean ITC_2_gameStarted = false;

    public void startGame(CommandSender sender, String mapName) {
        if (mapName.equalsIgnoreCase("ITC_1") && ITC_1_gameStarted) {
            sender.sendMessage(utils.color(plugin.messages.getConfiguration().getString("gameAlreadyStarted")
                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            return;
        } else if (mapName.equalsIgnoreCase("ITC_2") && ITC_2_gameStarted) {
            sender.sendMessage(utils.color(plugin.messages.getConfiguration().getString("gameAlreadyStarted")
                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            return;
        }

        Configuration maps = plugin.maps.getConfiguration();

        plugin.getLogger().info("Starting game on " + mapName);

        plugin.getLogger().info("Teleporting players to their team spawns");
        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.redTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                Player player = Bukkit.getPlayer(uuid);
                float x = (float) maps.getDouble(mapName + ".red.spawn.x");
                float y = (float) maps.getDouble(mapName + ".red.spawn.y");
                float z = (float) maps.getDouble(mapName + ".red.spawn.z");
                float yaw = (float) maps.getDouble(mapName + ".red.spawn.yaw");
                float pitch = (float) maps.getDouble(mapName + ".red.spawn.pitch");
                Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);
                player.teleport(loc);
            }
        }
        // for every player in blueTeam
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.blueTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                Player player = Bukkit.getPlayer(uuid);
                float x = (float) maps.getDouble(mapName + ".blue.spawn.x");
                float y = (float) maps.getDouble(mapName + ".blue.spawn.y");
                float z = (float) maps.getDouble(mapName + ".blue.spawn.z");
                float yaw = (float) maps.getDouble(mapName + ".blue.spawn.yaw");
                float pitch = (float) maps.getDouble(mapName + ".blue.spawn.pitch");
                Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);
                player.teleport(loc);
            }
        }

        // spawn chickens on both sides
        spawnChickens(mapName);

        // set gameStarted to true
        if (mapName.equalsIgnoreCase("ITC_1")) {
            ITC_1_gameStarted = true;
        } else if (mapName.equalsIgnoreCase("ITC_2")) {
            ITC_2_gameStarted = true;
        }
    }

    public void spectateGame(String map, String player) {
        plugin.getLogger().info("Spectating game on " + map + " for player " + player);
        plugin.getLogger().info("Game has started");
        // If specified player does not exist
        if (Bukkit.getPlayer(player) == null) {
            Bukkit.getPlayer(player).sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("invalidPlayer")
                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            return;
        }

        Player p = Bukkit.getPlayer(player);
        // If map is ITC_1
        if (map.equalsIgnoreCase("ITC_1")) {
            if (ITC_1_gameStarted) {
                plugin.getLogger().info("Map is ITC_1");
                // Set player to be spectator
                Location loc = new Location(p.getWorld(), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.x"), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.y"), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.z"), (float) plugin.maps.getConfiguration().getDouble("ITC_1.spectate.yaw"), (float) plugin.maps.getConfiguration().getDouble("ITC_1.spectate.pitch"));
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(loc);
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectating")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                .replace("%map%", "ITC_1")));
                TeamManager.spectators.put(p.getUniqueId(), new Pair<>(false, "ITC_1"));
            } else {
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectatingNoGame")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            }

            // If map is ITC_2
        } else if (map.equalsIgnoreCase("ITC_2")) {
            if (ITC_2_gameStarted) {
                plugin.getLogger().info("Map is ITC_2");
                // Set player to be spectator
                Location loc = new Location(p.getWorld(), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.x"), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.y"), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.z"), (float) plugin.maps.getConfiguration().getDouble("ITC_2.spectate.yaw"), (float) plugin.maps.getConfiguration().getDouble("ITC_2.spectate.pitch"));
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(loc);
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectating")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                .replace("%map%", "ITC_2")));
                TeamManager.spectators.put(p.getUniqueId(), new Pair<>(false, "ITC_2"));
            } else {
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectatingNoGame")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            }

        } else {
            p.sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("invalidMap")
                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
        }
    }

    public void endGame(String mapName, String winningTeam) {
        if (mapName.equalsIgnoreCase("ITC_2")) {
            // For every entity in the world
            Bukkit.getWorld("world").getEntities().forEach(entity -> {
                // If entity is a chicken and has a custom name that contains ITC_2
                if (entity instanceof Chicken && entity.getCustomName().contains("ITC_2")) {
                    // Remove the entity
                    entity.remove();
                }
            });

            // for every player in redTeam
            for (UUID uuid : TeamManager.redTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.redTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    announceWinner(player, winningTeam);
                    TeamManager.redTeam.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in blueTeam
            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.blueTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    announceWinner(player, winningTeam);
                    TeamManager.blueTeam.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in spectators
            for (UUID uuid : TeamManager.spectators.keySet()) {
                // if value of player matches mapName
                if (TeamManager.spectators.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    announceWinner(player, winningTeam);
                    TeamManager.spectators.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                    // Set player's gamemode to adventure
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }
            // reset map
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill -18 67 9 -19 67 9 minecraft:redstone_block");
        } else if (mapName.equalsIgnoreCase("ITC_1")) {
            // For every entity in the world
            Bukkit.getWorld("world").getEntities().forEach(entity -> {
                // If entity is a chicken and has a custom name that contains ITC_1
                if (entity instanceof Chicken && entity.getCustomName().contains("ITC_1")) {
                    // Remove the entity
                    entity.remove();
                }
            });

            // for every player in redTeam
            for (UUID uuid : TeamManager.redTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.redTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    TeamManager.redTeam.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in blueTeam
            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.blueTeam.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    TeamManager.blueTeam.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in spectators
            for (UUID uuid : TeamManager.spectators.keySet()) {
                // if value of player matches mapName
                if (TeamManager.spectators.get(uuid).equals(new Pair<>(false, mapName))) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    TeamManager.spectators.remove(uuid);
                    // Clear the player's inventory
                    player.getInventory().clear();
                    // Set player's gamemode to adventure
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }

            // reset map
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill 42 67 9 43 67 9 minecraft:redstone_block");
        }
        // set gameStarted to false
        if (mapName.equalsIgnoreCase("ITC_1")) {
            ITC_1_gameStarted = false;
        } else if (mapName.equalsIgnoreCase("ITC_2")) {
            ITC_2_gameStarted = false;
        }
    }

    /*
        Game logic
    */

    private void announceWinner(Player player, String winningTeam) {
        if (plugin.config.getConfiguration().getString("winnerAnnouncement").equalsIgnoreCase("chat")) {
            player.sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("winner")
                            .replace("%team%", winningTeam)
                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
        } else if (plugin.config.getConfiguration().getString("winnerAnnouncement").equalsIgnoreCase("title")) {
            player.sendTitle(utils.color(
                            plugin.messages.getConfiguration().getString("winnerTitle")
                                    .replace("%team%", winningTeam))
                    , null
                    , plugin.messages.getConfiguration().getInt("winnerFadeIn")
                    , plugin.messages.getConfiguration().getInt("winnerStay")
                    , plugin.messages.getConfiguration().getInt("winnerFadeOut"));
        } else if (plugin.config.getConfiguration().getString("winnerAnnouncement").equalsIgnoreCase("both")) {
            player.sendTitle(utils.color(
                            plugin.messages.getConfiguration().getString("winnerTitle")
                                    .replace("%team%", winningTeam))
                    , null
                    , plugin.messages.getConfiguration().getInt("winnerFadeIn")
                    , plugin.messages.getConfiguration().getInt("winnerStay")
                    , plugin.messages.getConfiguration().getInt("winnerFadeOut"));
            player.sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("winner")
                            .replace("%team%", winningTeam)
                            .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
        }
    }

    private void spawnChickens(String mapName) {
        Configuration maps = plugin.maps.getConfiguration();

        plugin.getLogger().info("Spawning chickens for " + mapName + "...");
        if (mapName.equalsIgnoreCase("ITC_2")) {
            // get coordinates from config
            double xBlue = maps.getDouble("ITC_2.blue.chicken.x");
            double yBlue = maps.getDouble("ITC_2.blue.chicken.y");
            double zBlue = maps.getDouble("ITC_2.blue.chicken.z");
            double xRed = maps.getDouble("ITC_2.red.chicken.x");
            double yRed = maps.getDouble("ITC_2.red.chicken.y");
            double zRed = maps.getDouble("ITC_2.red.chicken.z");

            // spawn blue chicken
            Chicken blueChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xBlue, yBlue, zBlue), org.bukkit.entity.EntityType.CHICKEN);
            blueChicken.setCustomName("ITC_2_BLUE");
            blueChicken.setCustomNameVisible(false);
            blueChicken.setAI(false);
            blueChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
            blueChicken.setHealth(plugin.config.getConfiguration().getDouble("chickenHealth"));

            // spawn red chicken
            Chicken redChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xRed, yRed, zRed), org.bukkit.entity.EntityType.CHICKEN);
            redChicken.setCustomName("ITC_2_RED");
            redChicken.setCustomNameVisible(false);
            redChicken.setAI(false);
            redChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
            redChicken.setHealth(plugin.config.getConfiguration().getDouble("chickenHealth"));
        } else if (mapName.equalsIgnoreCase("ITC_1")) {

            // get coordinates from config
            double xBlue = maps.getDouble("ITC_1.blue.chicken.x");
            double yBlue = maps.getDouble("ITC_1.blue.chicken.y");
            double zBlue = maps.getDouble("ITC_1.blue.chicken.z");
            double xRed = maps.getDouble("ITC_1.red.chicken.x");
            double yRed = maps.getDouble("ITC_1.red.chicken.y");
            double zRed = maps.getDouble("ITC_1.red.chicken.z");

            // spawn blue chicken
            Chicken blueChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xBlue, yBlue, zBlue), org.bukkit.entity.EntityType.CHICKEN);
            blueChicken.setCustomName("ITC_1_BLUE");
            blueChicken.setCustomNameVisible(false);
            blueChicken.setAI(false);
            blueChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
            blueChicken.setHealth(plugin.config.getConfiguration().getDouble("chickenHealth"));

            // spawn red chicken
            Chicken redChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xRed, yRed, zRed), org.bukkit.entity.EntityType.CHICKEN);
            redChicken.setCustomName("ITC_1_RED");
            redChicken.setCustomNameVisible(false);
            redChicken.setAI(false);
            redChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
            redChicken.setHealth(plugin.config.getConfiguration().getDouble("chickenHealth"));
        }
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e) {
        // Check if the entity that died was a chicken
        if (e.getEntity() instanceof Chicken) {
            // Check if the chicken was a chicken from ITC_1
            if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_1_BLUE")) {
                // End game for ITC_1, winners are red team
                plugin.getLogger().info("ITC_1 blue chicken died");
                endGame("ITC_1", "red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_1_RED")) {
                // End game for ITC_1, winners are blue team
                plugin.getLogger().info("ITC_1 red chicken died");
                endGame("ITC_1", "blue");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_2_BLUE")) {
                // End game for ITC_2, winners are red team
                plugin.getLogger().info("ITC_2 blue chicken died");
                endGame("ITC_2", "red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_2_RED")) {
                // End game for ITC_2, winners are blue team
                plugin.getLogger().info("ITC_2 red chicken died");
                endGame("ITC_2", "blue");
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (ITC_1_gameStarted) {
            if (TeamManager.blueTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info(e.getPlayer().getName() + " used a command during ITC_1");
                for (String command : plugin.config.getConfiguration().getStringList("commandsDisabled")) {
                    if (e.getMessage().contains(command)) {
                        plugin.getLogger().info(e.getPlayer().getName() + " used a disabled command during ITC_1");
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(utils.color(plugin.messages.getConfiguration().getString("commandDisabled")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }
                }
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info(e.getPlayer().getName() + " used a command during ITC_1");
                for (String command : plugin.config.getConfiguration().getStringList("commandsDisabled")) {
                    if (e.getMessage().contains(command)) {
                        plugin.getLogger().info(e.getPlayer().getName() + " used a disabled command during ITC_1");
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(utils.color(plugin.messages.getConfiguration().getString("commandDisabled")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }
                }
            }
        } else if (ITC_2_gameStarted) {
            if (TeamManager.blueTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info(e.getPlayer().getName() + " used a command during ITC_2");
                for (String command : plugin.config.getConfiguration().getStringList("commandsDisabled")) {
                    if (e.getMessage().contains(command)) {
                        plugin.getLogger().info(e.getPlayer().getName() + " used a disabled command during ITC_2");
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(utils.color(plugin.messages.getConfiguration().getString("commandDisabled")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }
                }
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info(e.getPlayer().getName() + " used a command during ITC_2");
                for (String command : plugin.config.getConfiguration().getStringList("commandsDisabled")) {
                    if (e.getMessage().contains(command)) {
                        plugin.getLogger().info(e.getPlayer().getName() + " used a disabled command during ITC_2");
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(utils.color(plugin.messages.getConfiguration().getString("commandDisabled")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onVoidFall(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getY() < plugin.config.getConfiguration().getDouble("teleportY")) {
            plugin.getLogger().info(e.getPlayer().getName() + " fell into the void");

            // Maps config
            Configuration maps = plugin.maps.getConfiguration();

            // Grab spawns from maps config
            Location spawn = new Location(Bukkit.getWorld("world"), maps.getDouble("spawn.x"), maps.getDouble("spawn.y"), maps.getDouble("spawn.z"), (float) maps.getDouble("spawn.yaw"), (float) maps.getDouble("spawn.pitch"));
            Location ITC_1_BLUE_SPAWN = new Location(Bukkit.getWorld("world"), maps.getDouble("ITC_1.blue.spawn.x"), maps.getDouble("ITC_1.blue.spawn.y"), maps.getDouble("ITC_1.blue.spawn.z"), (float) maps.getDouble("ITC_1.blue.spawn.yaw"), (float) maps.getDouble("ITC_1.blue.spawn.pitch"));
            Location ITC_1_RED_SPAWN = new Location(Bukkit.getWorld("world"), maps.getDouble("ITC_1.red.spawn.x"), maps.getDouble("ITC_1.red.spawn.y"), maps.getDouble("ITC_1.red.spawn.z"), (float) maps.getDouble("ITC_1.red.spawn.yaw"), (float) maps.getDouble("ITC_1.red.spawn.pitch"));
            Location ITC_2_BLUE_SPAWN = new Location(Bukkit.getWorld("world"), maps.getDouble("ITC_2.blue.spawn.x"), maps.getDouble("ITC_2.blue.spawn.y"), maps.getDouble("ITC_2.blue.spawn.z"), (float) maps.getDouble("ITC_2.blue.spawn.yaw"), (float) maps.getDouble("ITC_2.blue.spawn.pitch"));
            Location ITC_2_RED_SPAWN = new Location(Bukkit.getWorld("world"), maps.getDouble("ITC_2.red.spawn.x"), maps.getDouble("ITC_2.red.spawn.y"), maps.getDouble("ITC_2.red.spawn.z"), (float) maps.getDouble("ITC_2.red.spawn.yaw"), (float) maps.getDouble("ITC_2.red.spawn.pitch"));

            // If the player is in the blue team
            if (TeamManager.blueTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info("Player is in blue team");
                // If the player is in ITC_1
                if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_1"))) {
                    plugin.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 blue spawn
                    e.getPlayer().teleport(ITC_1_BLUE_SPAWN);
                    // Give the player the blocks
                    giveBlocks(e.getPlayer(), "blue");
                    // If the player is in ITC_2
                } else if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_2"))) {
                    plugin.getLogger().info("Player is in ITC_2");
                    // Teleport the player to ITC_2 blue spawn
                    e.getPlayer().teleport(ITC_2_BLUE_SPAWN);
                    // Give the player the blocks
                    giveBlocks(e.getPlayer(), "blue");
                }
                // If the player is in the red team
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                plugin.getLogger().info("Player is in red team");
                // If the player is in ITC_1
                if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_1"))) {
                    plugin.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 red spawn
                    e.getPlayer().teleport(ITC_1_RED_SPAWN);
                    // Give the player the blocks
                    giveBlocks(e.getPlayer(), "red");
                    // If the player is in ITC_2
                } else if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_2"))) {
                    plugin.getLogger().info("Player is in ITC_2");
                    // Teleport the player to ITC_2 red spawn
                    e.getPlayer().teleport(ITC_2_RED_SPAWN);
                    // Give the player the blocks
                    giveBlocks(e.getPlayer(), "red");
                }
                // If the player is not in a team
            } else {
                plugin.getLogger().info("Player is not in a team");
                e.getPlayer().teleport(spawn);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (ITC_1_gameStarted || ITC_2_gameStarted) {
            Player p = e.getPlayer();

            // Stores the last map the player was in before removing them from the team
            String lastMap = null;

            // If the player is in the blue team
            if (TeamManager.blueTeam.containsKey(e.getPlayer().getUniqueId())) {
                // If the player is in ITC_1
                if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_1"))) {
                    lastMap = "ITC_1";
                    // Remove the player from the blue team
                    TeamManager.blueTeam.remove(e.getPlayer().getUniqueId());
                    p.getInventory().clear();
                    // If the player is in ITC_2
                } else if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_2"))) {
                    lastMap = "ITC_2";
                    // Remove the player from the blue team
                    TeamManager.blueTeam.remove(e.getPlayer().getUniqueId());
                    p.getInventory().clear();
                }
                // If the player is in the red team
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                // If the player is in ITC_1
                if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_1"))) {
                    lastMap = "ITC_1";
                    // Remove the player from the red team
                    TeamManager.redTeam.remove(e.getPlayer().getUniqueId());
                    p.getInventory().clear();
                    // If the player is in ITC_2
                } else if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals(new Pair<>(false, "ITC_2"))) {
                    lastMap = "ITC_2";
                    // Remove the player from the red team
                    TeamManager.redTeam.remove(e.getPlayer().getUniqueId());
                    p.getInventory().clear();
                }
            }

            // If lastMap is null, return
            if (lastMap == null) {
                return;
            }

            // If the blue team is empty
            if (TeamManager.blueTeam.isEmpty()) {
                // End the game
                endGame(lastMap, "red");
                // If the red team is empty
            } else if (TeamManager.redTeam.isEmpty()) {
                // End the game
                endGame(lastMap, "blue");
            }

        }
    }

    private void giveBlocks(Player player, String team) {
        plugin.getLogger().info("Giving blocks for " + player.getName() + "...");

        // If the player is on the red team
        if (team.equalsIgnoreCase("red")) {
            plugin.getLogger().info(player.getName() + " is on the red team, giving them red blocks...");
            // Create the full set of unbreakable red leather armor

            if (!player.getInventory().contains(Material.RED_WOOL)) {
                plugin.getLogger().info("Player does not have red wool, giving them red wool...");
                player.getInventory().addItem(forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                player.getInventory().addItem(forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                player.getInventory().setItemInOffHand(forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                return;
            }

            // For every item in the player's inventory
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    // If the item is red wool
                    if (player.getInventory().getItem(i).getType().equals(Material.RED_WOOL)) {
                        // Replace the wool
                        player.getInventory().setItem(i, forgeCanBePlacedOnItem(new ItemStack(Material.RED_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                    }
                }
            }

            // If the player is in the blue team
        } else if (team.equalsIgnoreCase("blue")) {
            plugin.getLogger().info(player.getName() + " is on the blue team, giving them blue blocks...");

            if (!player.getInventory().contains(Material.BLUE_WOOL)) {
                plugin.getLogger().info("Player does not have blue wool, giving them blue wool...");
                player.getInventory().addItem(forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                player.getInventory().addItem(forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                player.getInventory().setItemInOffHand(forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                return;
            }

            // For every item in player's inventory
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    // If the item is blue wool
                    if (player.getInventory().getItem(i).getType().equals(Material.BLUE_WOOL)) {
                        // Replace the wool
                        player.getInventory().setItem(i, forgeCanBePlacedOnItem(new ItemStack(Material.BLUE_WOOL, 64), "minecraft:red_wool", "minecraft:blue_wool", "minecraft:red_concrete", "minecraft:blue_concrete"));
                    }
                }
            }

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


}
