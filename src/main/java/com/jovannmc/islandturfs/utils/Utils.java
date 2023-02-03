package com.jovannmc.islandturfs.utils;

import com.jovannmc.islandturfs.IslandTurfs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

	private IslandTurfs plugin = IslandTurfs.getInstance();
	
	public String color(String string) {
		String colored = ChatColor.translateAlternateColorCodes('&', string);
		return colored;
	}
	
	public void invalidPermission(CommandSender player) { player.sendMessage(color(plugin.messages.getConfiguration().getString("invalidPermission"))); }

	public void invalidUsage(CommandSender player, String usage) { player.sendMessage(color(plugin.messages.getConfiguration().getString("invalidUsage").replace("%prefix%", plugin.config.getConfiguration().getString("prefix")).replace("%usage%", usage))); }

}
