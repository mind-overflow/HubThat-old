package net.mindoverflow.hubthat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGotoWorld implements CommandExecutor {

	public Main plugin;
	
	public CommandGotoWorld(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if(CommandLabel.equalsIgnoreCase("worldtp")) {
			if(!(sender instanceof Player)) {
				String ONLY_PLAYERS = plugin.getConfig().getString("worldtp.ONLY_PLAYERS").replace("&", "§");
				sender.sendMessage(ONLY_PLAYERS);
				return true;
			}
			Player player = (Player)sender;
			if(sender.hasPermission(new permission().GotoWorld)) {
			if(args.length == 0) {
				String nargs = plugin.getConfig().getString("worldtp.NEEDED_ARGS").replace("&", "§");
				sender.sendMessage(nargs);
				return true;
			} else {
				World ww = Bukkit.getWorld(args[0]);
				if(Bukkit.getWorlds().contains(ww)) {
					Location loc = new Location(ww, ww.getSpawnLocation().getX(), ww.getSpawnLocation().getY(), ww.getSpawnLocation().getZ());
					player.teleport(loc);
					String tported = plugin.getConfig().getString("worldtp.TELEPORTED").replace("%world%", args[0]).replace("&", "§");
					sender.sendMessage(tported);
					return true;
				} else {
					String nworld = plugin.getConfig().getString("worldtp.UNKNOWN_WORLD").replace("&", "§");
					sender.sendMessage(nworld);
					return true;
				}
			}
			
		} else {
			String noperm = plugin.getConfig().getString("worldtp.NO_PERMISSIONS").replace("&", "§");
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
			sender.sendMessage(PREFIX + ChatColor.RED + noperm);
			return true;
		}
	}
		
		return false;
	}

}