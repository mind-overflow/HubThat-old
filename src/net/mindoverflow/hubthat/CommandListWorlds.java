package net.mindoverflow.hubthat;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandListWorlds implements CommandExecutor {

	public Main plugin;
	
	public CommandListWorlds (Main plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if(CommandLabel.equalsIgnoreCase("worldlist")) {
			if(sender.hasPermission(new permission().ListWorlds)) {
				sender.sendMessage("§3Worlds List:");
				sender.sendMessage("§7---------");
				int wn = 0;
				for (World ww : Bukkit.getWorlds()) {
					++wn;
					sender.sendMessage("§3" + wn + "§7: §6" + ww.getName() + "§7, §8" + ww.getWorldType().toString().toLowerCase());
					}
				wn = 0;
				sender.sendMessage("§7---------");
				return true;
			} else {
				String noperm = plugin.getConfig().getString("worldlist.NO_PERMISSIONS").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
				sender.sendMessage(PREFIX + ChatColor.RED + noperm);
				
				return true;
			}
			
		}
		return false;
	}

}
