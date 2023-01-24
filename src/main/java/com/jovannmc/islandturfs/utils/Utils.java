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
	private Configuration messages = plugin.messages.getConfiguration();
	
	public String color(String string) {
		String colored = ChatColor.translateAlternateColorCodes('&', string);
		return colored;
	}
	
	public void noPermission(CommandSender player) { player.sendMessage(color(messages.getString("noPermission"))); }

	public void noPlayer(CommandSender player) { player.sendMessage(color(messages.getString("noPlayer"))); }

	public void playerOnly(CommandSender player) { player.sendMessage(color(messages.getString("playerOnly"))); }
	
	public void invalidUsage(CommandSender player, Command cmd) {
		if (cmd != null) {
			player.sendMessage(color(messages.getString("invalidUsage").replace("%usage%", cmd.getUsage())));
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "Invalid command set!");
		}
	}

}
