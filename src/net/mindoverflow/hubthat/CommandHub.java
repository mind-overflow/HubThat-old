package net.mindoverflow.hubthat;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandHub implements CommandExecutor, Listener {
	YamlConfiguration s;
	
	private Main plugin;
	
	public CommandHub(Main plugin) {
		this.plugin = plugin;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args){
		if(!(sender instanceof Player)) {
			String onlyplayers = plugin.getConfig().getString("hub.ONLY_PLAYERS").replace("&", "§");
			sender.sendMessage(ChatColor.DARK_RED + onlyplayers);
			return true;
		}
		final Player player = (Player) sender;
		if(!Main.tporting.containsKey(player.getName())) {
			Main.tporting.put(player.getName(), false);
		}
		if(CommandLabel.equalsIgnoreCase("hub")) {
			if(sender.hasPermission(new permission().Hub)){
				
			File hub = new File(plugin.getDataFolder() + File.separator + "hub.yml");
			if(!hub.exists()){
				String not_set = plugin.getConfig().getString("hub.HUB_NOT_SET").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
		        player.sendMessage(PREFIX + ChatColor.RED + not_set);
		        return true;
			} if (!sender.hasPermission(new permission().HubDelayBypass)){
				if(Main.tporting.get(player.getName())){
					String ALREADY_TP = plugin.getConfig().getString("global.ALREADY-TELEPORTING").replace("&", "§");
					String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
					player.sendMessage(PREFIX + ALREADY_TP);
					return true;
				} else {
					Main.tporting.put(player.getName(), true);
				}
			Long hubdelay = plugin.getConfig().getLong("hub.delay") * 20;
			String hubdelaytextwait = plugin.getConfig().getString("hub.DELAY_TEXT_WAIT").replace("%sec%", hubdelay / 20 + "").replace("&", "§");
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
	        player.sendMessage(PREFIX + ChatColor.GREEN + hubdelaytextwait);
	        
			s = YamlConfiguration.loadConfiguration(hub);
			
	        String world = s.getString("hub.world");
			double x = s.getDouble("hub.x");
			double y = s.getDouble("hub.y");
			double z = s.getDouble("hub.z");
			final double yaw = s.getDouble("hub.yaw");
			final double pitch = s.getDouble("hub.pitch");
			  final Location loc = new Location(Bukkit.getWorld(world), x, y, z);
			  
			  Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				  public void run() {
					  if(Main.tporting.get(player.getName())){
				        loc.setYaw((float)yaw);
				        loc.setPitch((float)pitch);
				        player.teleport(loc);
						String hubteleported = plugin.getConfig().getString("hub.TELEPORTED").replace("&", "§");
						String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
						player.sendMessage(PREFIX + ChatColor.GREEN + hubteleported);
						Main.tporting.put(player.getName(), false);
				  
					  }
				  }
				}, hubdelay);
			} else if (sender.hasPermission(new permission().HubDelayBypass)){
				
				s = YamlConfiguration.loadConfiguration(hub);
				
		        String world = s.getString("hub.world");
				double x = this.s.getDouble("hub.x");
				double y = this.s.getDouble("hub.y");
				double z = this.s.getDouble("hub.z");
				final double yaw = this.s.getDouble("hub.yaw");
				final double pitch = this.s.getDouble("hub.pitch");
				final Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				loc.setYaw((float)yaw);
				loc.setPitch((float)pitch);
				player.teleport(loc);
				String hubteleported = plugin.getConfig().getString("hub.TELEPORTED").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
				player.sendMessage(PREFIX + ChatColor.GREEN + hubteleported);
					  
				}
		}else{
			String hubnoperm = plugin.getConfig().getString("hub.NO_PERMISSIONS").replace("&", "§");
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
	          player.sendMessage(PREFIX + ChatColor.RED +  hubnoperm);
		}
		}
		return false;
	}
	
}
