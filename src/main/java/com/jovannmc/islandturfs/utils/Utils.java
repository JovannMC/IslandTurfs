package com.jovannmc.islandturfs.utils;

import com.jovannmc.islandturfs.IslandTurfs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import java.util.logging.Level;

public class Utils {

	private IslandTurfs plugin = IslandTurfs.getInstance();
	private Configuration config = plugin.config.getConfiguration();
	
	public String color(String string) {
		String colored = ChatColor.translateAlternateColorCodes('&', string);
		return colored;
	}
	
	public void invalidPermission(CommandSender player) { player.sendMessage(color(plugin.messages.getConfiguration().getString("invalidPermission"))); }

	public void invalidUsage(CommandSender player, String usage) { player.sendMessage(color(plugin.messages.getConfiguration().getString("invalidUsage").replace("%prefix%", plugin.config.getConfiguration().getString("prefix")).replace("%usage%", usage))); }

}
