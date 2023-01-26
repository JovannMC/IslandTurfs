package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import de.tr7zw.nbtapi.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GameManager implements Listener {

    /*
        TODO: ability to instantiate GameManager and TeamManager classes to prevent issues with multiple games running at once
        - or at least make the code work with multiple games running at once without rewriting the code for both classes to support it
        - Add username to commands to work with ServerSigns
    */

    IslandTurfs plugin = IslandTurfs.getInstance();

    Utils utils = new Utils();

    // TODO: implement gameStarted to code
    private boolean gameStarted = false;

    public void startGame(String mapName) {
        Configuration maps = plugin.maps.getConfiguration();

        Bukkit.getLogger().info("Starting game on " + mapName);

        Bukkit.getLogger().info("Teleporting players to their team spawns");
        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.redTeam.get(uuid).equals(mapName)) {
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
            if (TeamManager.blueTeam.get(uuid).equals(mapName)) {
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

        // give items to players
        giveItems(mapName);

        // set gameStarted to true
        gameStarted = true;
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
                if (TeamManager.redTeam.get(uuid).equals(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), 12, 66, 9);
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
                if (TeamManager.blueTeam.get(uuid).equals(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), 12, 66, 9);
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
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
                if (TeamManager.redTeam.get(uuid).equals(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), 12, 66, 9);
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
                if (TeamManager.blueTeam.get(uuid).equals(mapName)) {
                    // teleport player to spawn
                    Player player = Bukkit.getPlayer(uuid);
                    Location loc = new Location(player.getWorld(), 12, 66, 9);
                    player.teleport(loc);
                    player.sendMessage(utils.color(
                            plugin.messages.getConfiguration().getString("winner")
                                    .replace("%team%", winningTeam)
                                    .replace("%prefix%", plugin.config.getConfiguration().getString("prefix"))));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }

            // reset map
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill 42 67 9 43 67 9 minecraft:redstone_block");
        }
        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if redTeam contains player
            if (TeamManager.redTeam.containsKey(uuid)) {
                // remove player from redTeam
                TeamManager.redTeam.remove(uuid);
            }
        }
        // for every player in blueTeam
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // if blueTeam contains player
            if (TeamManager.blueTeam.containsKey(uuid)) {
                // remove player from blueTeam
                TeamManager.blueTeam.remove(uuid);
            }
        }

        Bukkit.getLogger().info("Game ended on " + mapName + ", the winners were " + winningTeam + "!");

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

    private void giveItems(String mapName) {
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

        Bukkit.getLogger().info("Giving items to red team players...");
        // For each player in the red team
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // Check if player's key matches mapName
            if (TeamManager.redTeam.get(uuid).equals(mapName)) {
                Player player = Bukkit.getPlayer(uuid);

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
            }
        }

        Bukkit.getLogger().info("Giving items to blue team players...");
        // For each player in the blue team
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // Check if player's key matches mapName
            if (TeamManager.blueTeam.get(uuid).equals(mapName)) {
                Player player = Bukkit.getPlayer(uuid);

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
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e) {
        // Check if the entity that died was a chicken
        if (e.getEntity() instanceof Chicken) {
            // Check if the chicken was a chicken from ITC_1
            if (e.getEntity().getCustomName().equals("ITC_1_BLUE")) {
                // End game for ITC_1, winners are red team
                Bukkit.getLogger().info("ITC_1 blue chicken died");
                endGame("ITC_1", "Red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equals("ITC_1_RED")) {
                // End game for ITC_1, winners are blue team
                Bukkit.getLogger().info("ITC_1 red chicken died");
                endGame("ITC_1", "Blue");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equals("ITC_2_BLUE")) {
                // End game for ITC_2, winners are red team
                Bukkit.getLogger().info("ITC_2 blue chicken died");
                endGame("ITC_2", "Red");
                // Check if the chicken was a chicken from ITC_2
            } else if (e.getEntity().getCustomName().equals("ITC_2_RED")) {
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
                if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals("ITC_1")) {
                    Bukkit.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 blue spawn
                    e.getPlayer().teleport(ITC_1_BLUE_SPAWN);
                // If the player is in ITC_2
                } else if (TeamManager.blueTeam.get(e.getPlayer().getUniqueId()).equals("ITC_2")) {
                    Bukkit.getLogger().info("Player is in ITC_2");
                    // Teleport the player to ITC_2 blue spawn
                    e.getPlayer().teleport(ITC_2_BLUE_SPAWN);
                }
            // If the player is in the red team
            } else if (TeamManager.redTeam.containsKey(e.getPlayer().getUniqueId())) {
                Bukkit.getLogger().info("Player is in red team");
                // If the player is in ITC_1
                if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals("ITC_1")) {
                    Bukkit.getLogger().info("Player is in ITC_1");
                    // Teleport the player to ITC_1 red spawn
                    e.getPlayer().teleport(ITC_1_RED_SPAWN);
                // If the player is in ITC_2
                } else if (TeamManager.redTeam.get(e.getPlayer().getUniqueId()).equals("ITC_2")) {
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

    // Create "can" item
    private static ItemStack forgeCanItem(ItemStack item, String can, String[] blocks) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        NBTItem nbt = new NBTItem(item);
        NBTList<String> canNbt = nbt.getStringList(can);
        for (int i = 0; i < blocks.length; i ++) {
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
