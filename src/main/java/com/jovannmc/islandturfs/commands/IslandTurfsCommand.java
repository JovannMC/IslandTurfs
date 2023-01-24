package com.jovannmc.islandturfs.commands;

import com.jovannmc.islandturfs.IslandTurfs;
import com.jovannmc.islandturfs.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IslandTurfsCommand implements CommandExecutor {

    IslandTurfs main = IslandTurfs.getInstance();
    Utils utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("islandturfs.command.islandturfs")) {
            if (args.length == 0) {
                sender.sendMessage(utils.color("&6Island Turfs v" + main.getDescription().getVersion()));
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                main.config.reloadConfig();
                main.messages.reloadConfig();
                sender.sendMessage(utils.color(main.getConfig().getString("prefix") + " &6Reloaded the config!"));
            } else {
                utils.invalidUsage(sender, cmd);
            }
        } else {
            utils.noPermission(sender);
        }

        return false;
    }
}
