package com.jovannmc.islandturfs.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class Utils {
	
	public String color(String string) {
		String colored = ChatColor.translateAlternateColorCodes('&', string);
		return colored;
	}
	
	public void noPermission(CommandSender player) { player.sendMessage(color("&4No permission!")); }
	
	public void invalidPlayer(CommandSender player) { player.sendMessage(color("&4That player is not online or doesn't exist!")); }
	
	public void invalidUsage(CommandSender player, Command cmd) {
		if (cmd != null) {
			player.sendMessage(color("&4Invalid Usage! " + cmd.getUsage()));
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "Invalid command set!");
		}
	}

}
