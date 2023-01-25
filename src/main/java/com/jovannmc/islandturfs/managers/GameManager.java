package com.jovannmc.islandturfs.managers;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.logging.Level;

public class GameManager implements Listener {

    /*
        TODO:
        - Rely on main IslandTurfsCommand class for GameManager logic
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

    // TODO: implement gameStarted to code
    private boolean gameStarted = false;

    public void startGame(String mapName) {
        Bukkit.getLogger().log(Level.INFO, "Starting game on " + mapName);

        // for every player in redTeam
        for (UUID uuid : TeamManager.redTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.redTeam.get(uuid).equals(mapName)) {
                Player player = Bukkit.getPlayer(uuid);
                float x = (float) plugin.maps.getConfiguration().getDouble(mapName + ".red.spawn.x");
                float y = (float) plugin.maps.getConfiguration().getDouble(mapName + ".red.spawn.y");
                float z = (float) plugin.maps.getConfiguration().getDouble(mapName + ".red.spawn.z");
                float yaw = (float) plugin.maps.getConfiguration().getDouble(mapName + ".red.spawn.yaw");
                float pitch = (float) plugin.maps.getConfiguration().getDouble(mapName + ".red.spawn.pitch");
                Location loc = new Location(player.getWorld(), x, y, z, yaw, pitch);
                player.teleport(loc);
            }
        }
        // for every player in blueTeam
        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            // if value of player matches mapName
            if (TeamManager.blueTeam.get(uuid).equals(mapName)) {
                Player player = Bukkit.getPlayer(uuid);
                float x = (float) plugin.maps.getConfiguration().getDouble(mapName + ".blue.spawn.x");
                float y = (float) plugin.maps.getConfiguration().getDouble(mapName + ".blue.spawn.y");
                float z = (float) plugin.maps.getConfiguration().getDouble(mapName + ".blue.spawn.z");
                float yaw = (float) plugin.maps.getConfiguration().getDouble(mapName + ".blue.spawn.yaw");
                float pitch = (float) plugin.maps.getConfiguration().getDouble(mapName + ".blue.spawn.pitch");
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
        if (mapName.equalsIgnoreCase("newMap")) {
            Bukkit.getLogger().log(Level.INFO, "Ending game on newMap");

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
                    player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("winner").replace("%team%", winningTeam)));
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
                    player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("winner").replace("%team%", winningTeam)));
                    // Clear the player's inventory
                    player.getInventory().clear();
                }
            }
            // reset map
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fill -18 67 9 -19 67 9 minecraft:redstone_block");
        } else if (mapName.equalsIgnoreCase("oldMap")) {
            Bukkit.getLogger().log(Level.INFO, "Ending game on oldMap");

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
                    player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("winner").replace("%team%", winningTeam)));
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
                    player.sendMessage(utils.color(plugin.messages.getConfiguration().getString("winner").replace("%team%", winningTeam)));
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

        // set gameStarted to false
        gameStarted = false;
    }

    /*
        Game logic
    */

    private void spawnChickens(String mapName) {
        if (mapName.equalsIgnoreCase("newMap")) {
            double xBlue = plugin.maps.getConfiguration().getDouble("newMap.blue.chicken.x");
            double yBlue = plugin.maps.getConfiguration().getDouble("newMap.blue.chicken.y");
            double zBlue = plugin.maps.getConfiguration().getDouble("newMap.blue.chicken.z");
            double xRed = plugin.maps.getConfiguration().getDouble("newMap.red.chicken.x");
            double yRed = plugin.maps.getConfiguration().getDouble("newMap.red.chicken.y");
            double zRed = plugin.maps.getConfiguration().getDouble("newMap.red.chicken.z");

            Chicken blueChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xBlue, yBlue, zBlue), org.bukkit.entity.EntityType.CHICKEN);
            blueChicken.setCustomName("ITC_2_BLUE");
            blueChicken.setCustomNameVisible(false);
            blueChicken.setAI(false);
            blueChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));

            Chicken redChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xRed, yRed, zRed), org.bukkit.entity.EntityType.CHICKEN);
            redChicken.setCustomName("ITC_2_RED");
            redChicken.setCustomNameVisible(false);
            redChicken.setAI(false);
            redChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
        } else if (mapName.equalsIgnoreCase("oldMap")) {
            double xBlue = plugin.maps.getConfiguration().getDouble("oldMap.blue.chicken.x");
            double yBlue = plugin.maps.getConfiguration().getDouble("oldMap.blue.chicken.y");
            double zBlue = plugin.maps.getConfiguration().getDouble("oldMap.blue.chicken.z");
            double xRed = plugin.maps.getConfiguration().getDouble("oldMap.red.chicken.x");
            double yRed = plugin.maps.getConfiguration().getDouble("oldMap.red.chicken.y");
            double zRed = plugin.maps.getConfiguration().getDouble("oldMap.red.chicken.z");

            Chicken blueChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xBlue, yBlue, zBlue), org.bukkit.entity.EntityType.CHICKEN);
            blueChicken.setCustomName("ITC_1_BLUE");
            blueChicken.setCustomNameVisible(false);
            blueChicken.setAI(false);
            blueChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));
            Bukkit.getLogger().info(blueChicken.toString());

            Chicken redChicken = (Chicken) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), xRed, yRed, zRed), org.bukkit.entity.EntityType.CHICKEN);
            redChicken.setCustomName("ITC_1_RED");
            redChicken.setCustomNameVisible(false);
            redChicken.setAI(false);
            redChicken.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999999, 100, false, false));

            Bukkit.getLogger().info(redChicken.toString());
        }
    }

    private void giveItems(String mapName) {

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

        for (UUID uuid : TeamManager.redTeam.keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            // Create the full set of red leather armor
            ItemStack redLeatherHelmet = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta redLeatherHelmetMeta = (LeatherArmorMeta) redLeatherHelmet.getItemMeta();
            redLeatherHelmetMeta.setColor(Color.RED);
            redLeatherHelmet.setItemMeta(redLeatherHelmetMeta);

            ItemStack redLeatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta redLeatherChestplateMeta = (LeatherArmorMeta) redLeatherChestplate.getItemMeta();
            redLeatherChestplateMeta.setColor(Color.RED);
            redLeatherChestplate.setItemMeta(redLeatherChestplateMeta);

            ItemStack redLeatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta redLeatherLeggingsMeta = (LeatherArmorMeta) redLeatherLeggings.getItemMeta();
            redLeatherLeggingsMeta.setColor(Color.RED);
            redLeatherLeggings.setItemMeta(redLeatherLeggingsMeta);

            ItemStack redLeatherBoots = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta redLeatherBootsMeta = (LeatherArmorMeta) redLeatherBoots.getItemMeta();
            redLeatherBootsMeta.setColor(Color.RED);
            redLeatherBoots.setItemMeta(redLeatherBootsMeta);

            // Clear the player's inventory
            player.getInventory().clear();

            // Give the player the items
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(bow);
            player.getInventory().addItem(new ItemStack(Material.SHEARS));
            player.getInventory().addItem(new ItemStack(Material.ARROW));
            player.getInventory().addItem(new ItemStack(Material.RED_WOOL, 64));
            player.getInventory().addItem(new ItemStack(Material.RED_WOOL, 64));
            player.getInventory().setHelmet(redLeatherHelmet);
            player.getInventory().setChestplate(redLeatherChestplate);
            player.getInventory().setLeggings(redLeatherLeggings);
            player.getInventory().setBoots(redLeatherBoots);
            player.getInventory().setItemInOffHand(new ItemStack(Material.RED_WOOL, 64));
        }

        for (UUID uuid : TeamManager.blueTeam.keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            // Create the full set of blue leather armor
            ItemStack blueLeatherHelmet = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta blueLeatherHelmetMeta = (LeatherArmorMeta) blueLeatherHelmet.getItemMeta();
            blueLeatherHelmetMeta.setColor(Color.BLUE);
            blueLeatherHelmet.setItemMeta(blueLeatherHelmetMeta);
            
            ItemStack blueLeatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta blueLeatherChestplateMeta = (LeatherArmorMeta) blueLeatherChestplate.getItemMeta();
            blueLeatherChestplateMeta.setColor(Color.BLUE);
            blueLeatherChestplate.setItemMeta(blueLeatherChestplateMeta);
            
            ItemStack blueLeatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta blueLeatherLeggingsMeta = (LeatherArmorMeta) blueLeatherLeggings.getItemMeta();
            blueLeatherLeggingsMeta.setColor(Color.BLUE);
            blueLeatherLeggings.setItemMeta(blueLeatherLeggingsMeta);
            
            ItemStack blueLeatherBoots = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta blueLeatherBootsMeta = (LeatherArmorMeta) blueLeatherBoots.getItemMeta();
            blueLeatherBootsMeta.setColor(Color.BLUE);
            blueLeatherBoots.setItemMeta(blueLeatherBootsMeta);
            
            // Clear the player's inventory
            player.getInventory().clear();

            // Give the player the items
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.getInventory().addItem(bow);
            player.getInventory().addItem(new ItemStack(Material.SHEARS));
            player.getInventory().addItem(new ItemStack(Material.ARROW));
            player.getInventory().addItem(new ItemStack(Material.BLUE_WOOL, 64));
            player.getInventory().addItem(new ItemStack(Material.BLUE_WOOL, 64));
            player.getInventory().setHelmet(blueLeatherHelmet);
            player.getInventory().setChestplate(blueLeatherChestplate);
            player.getInventory().setLeggings(blueLeatherLeggings);
            player.getInventory().setBoots(blueLeatherBoots);
            player.getInventory().setItemInOffHand(new ItemStack(Material.BLUE_WOOL, 64));
        }
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e) {
        // Old map
        if (e.getEntity() instanceof Chicken) {
            if (e.getEntity().getCustomName().equals("ITC_1_BLUE")) {
                Bukkit.getLogger().info("ITC_1 blue chicken died");
                endGame("oldMap", "Red");
            } else if (e.getEntity().getCustomName().equals("ITC_1_RED")) {
                Bukkit.getLogger().info("ITC_1 red chicken died");
                endGame("oldMap", "Blue");
            } else if (e.getEntity().getCustomName().equals("ITC_2_BLUE")) {
                Bukkit.getLogger().info("ITC_2 blue chicken died");
                endGame("newMap", "Red");
            } else if (e.getEntity().getCustomName().equals("ITC_2_RED")) {
                Bukkit.getLogger().info("ITC_2 red chicken died");
                endGame("newMap", "Blue");
            }
        }
    }

}
