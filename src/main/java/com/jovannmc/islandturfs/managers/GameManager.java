package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GameManager implements Listener {

    /*
        TODO: ability to instantiate GameManager and TeamManager classes to prevent issues with multiple games running at once
        - or at least make the code work with multiple games running at once without rewriting the code for both classes to support it
    */

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    // TODO: implement gameStarted to code
    private static boolean gameStarted = false;

    public void startGame(String mapName) {
        Configuration maps = plugin.maps.getConfiguration();

        Bukkit.getLogger().info("Starting game on " + mapName);

        Bukkit.getLogger().info("Teleporting players to their team spawns");
        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(mapName)) {
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
            if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(mapName)) {
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
        gameStarted = true;
    }

    public void spectateGame(String map, String player) {
        Bukkit.getLogger().info("Spectating game on " + map + " for player " + player);
        if (gameStarted) {
            Bukkit.getLogger().info("Game has started");
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
                Bukkit.getLogger().info("Map is ITC_1");
                // Set player to be spectator
                Location loc = new Location(p.getWorld(), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.x"), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.y"), plugin.maps.getConfiguration().getDouble("ITC_1.spectate.z"), (float) plugin.maps.getConfiguration().getDouble("ITC_1.spectate.yaw"), (float) plugin.maps.getConfiguration().getDouble("ITC_1.spectate.pitch"));
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(loc);
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectating")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                .replace("%map%", "ITC_1")));
                TeamManager.spectators.put(p.getUniqueId(), "ITC_1");
                // If map is ITC_2
            } else if (map.equalsIgnoreCase("ITC_2")) {
                Bukkit.getLogger().info("Map is ITC_2");
                // Set player to be spectator
                Location loc = new Location(p.getWorld(), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.x"), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.y"), plugin.maps.getConfiguration().getDouble("ITC_2.spectate.z"), (float) plugin.maps.getConfiguration().getDouble("ITC_2.spectate.yaw"), (float) plugin.maps.getConfiguration().getDouble("ITC_2.spectate.pitch"));
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(loc);
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("spectating")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))
                                .replace("%map%", "ITC_2")));
                TeamManager.spectators.put(p.getUniqueId(), "ITC_2");
            } else {
                p.sendMessage(utils.color(
                        plugin.messages.getConfiguration().getString("invalidMap")
                                .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
            }
        } else {
            ;
            Bukkit.getPlayer(player).sendMessage(utils.color(
                    plugin.messages.getConfiguration().getString("spectatingNoGame")
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
                if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in blueTeam
            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in spectators
            for (UUID uuid : TeamManager.spectators.keySet()) {
                // if value of player matches mapName
                if (TeamManager.spectators.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
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
                if (TeamManager.redTeam.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in blueTeam
            for (UUID uuid : TeamManager.blueTeam.keySet()) {
                // if value of player matches mapName
                if (TeamManager.blueTeam.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // for every player in spectators
            for (UUID uuid : TeamManager.spectators.keySet()) {
                // if value of player matches mapName
                if (TeamManager.spectators.get(uuid).equalsIgnoreCase(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), plugin.maps.getConfiguration().getDouble("spawn.x"), plugin.maps.getConfiguration().getDouble("spawn.y"), plugin.maps.getConfiguration().getDouble("spawn.z"));
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                    // Set player's gamemode to adventure
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }

            // reset map
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill 42 67 9 43 67 9 minecraft:redstone_block");
        }
        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if redTeam contains player
            if (TeamManager.redTeam.containsKey(uuid) && TeamManager.redTeam.get(uuid).equalsIgnoreCase(mapName)) {
                // remove player from redTeam
                TeamManager.redTeam.remove(uuid);
            }
        }
        // for every player in blueTeam
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // if blueTeam contains player
            if (TeamManager.blueTeam.containsKey(uuid) && TeamManager.blueTeam.get(uuid).equalsIgnoreCase(mapName)) {
                // remove player from blueTeam
                TeamManager.blueTeam.remove(uuid);
            }
        }

        // set gameStarted to false
        gameStarted = false;
    }

    /*
        Game logic
    */

    private void spawnChickens(String mapName) {
        Configuration maps = plugin.maps.getConfiguration();

        Bukkit.getLogger().info("Spawning chickens for " + mapName + "...");
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
                Bukkit.getLogger().info("ITC_1 blue chicken died");
                endGame("ITC_1", "Red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_1_RED")) {
                // End game for ITC_1, winners are blue team
                Bukkit.getLogger().info("ITC_1 red chicken died");
                endGame("ITC_1", "Blue");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_2_BLUE")) {
                // End game for ITC_2, winners are red team
                Bukkit.getLogger().info("ITC_2 blue chicken died");
                endGame("ITC_2", "Red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equalsIgnoreCase("ITC_2_RED")) {
                // End game for ITC_2, winners are blue team
                Bukkit.getLogger().info("ITC_2 red chicken died");
                endGame("ITC_2", "Blue");
            }
        }
    }

    @EventHandler
    public void onVoidFall(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getY() < plugin.config.getConfiguration().getDouble("teleportY")) {
            Bukkit.getLogger().info(e.getPlayer().getName() + " fell into the void");

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
                Bukkit.getLogger().info("Player is in blue team");
                // If the player is in ITC_1
                if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equalsIgnoreCase("ITC_1")) {
                    Bukkit.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 blue spawn
                    e.getPlayer().teleport(ITC_1_BLUE_SPAWN);
                    // If the player is in ITC_2
                } else if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equalsIgnoreCase("ITC_2")) {
                    Bukkit.getLogger().info("Player is in ITC_2");
                    // Teleport the player to ITC_2 blue spawn
                    e.getPlayer().teleport(ITC_2_BLUE_SPAWN);
                }
                // If the player is in the red team
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                Bukkit.getLogger().info("Player is in red team");
                // If the player is in ITC_1
                if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equalsIgnoreCase("ITC_1")) {
                    Bukkit.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 red spawn
                    e.getPlayer().teleport(ITC_1_RED_SPAWN);
                    // If the player is in ITC_2
                } else if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equalsIgnoreCase("ITC_2")) {
                    Bukkit.getLogger().info("Player is in ITC_2");
                    // Teleport the player to ITC_2 red spawn
                    e.getPlayer().teleport(ITC_2_RED_SPAWN);
                }
                // If the player is not in a team
            } else {
                Bukkit.getLogger().info("Player is not in a team");
                e.getPlayer().teleport(spawn);
            }
        }
    }

}
