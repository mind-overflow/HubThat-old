package net.mindoverflow.hubthat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandSetSpawn implements CommandExecutor{
	public Main plugin;
	YamlConfiguration s;

	
	public CommandSetSpawn(Main plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args){
		if(!(sender instanceof Player)){
			String ONLY_PLAYERS = plugin.getConfig().getString("setspawn.ONLY_PLAYERS").replace("&", "§");
			sender.sendMessage(ChatColor.DARK_RED + ONLY_PLAYERS);
			return true;
		}
		Player player = (Player) sender;
		if(CommandLabel.equalsIgnoreCase("setspawn")){
			if(sender.hasPermission(new permission().SetSpawn)){
				if(args.length == 0) {
			File spawn = new File(plugin.getDataFolder() +File.separator + "spawn.yml");
	        if (!spawn.exists()) {
	          try {
	            spawn.createNewFile();
	          }
	          catch (Exception e)
	          {
	            e.printStackTrace();

				String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
	            player.sendMessage(PREFIX + ChatColor.RED + SET_ERROR);
	            return true;
	          }
	        }

	        s = YamlConfiguration.loadConfiguration(spawn);
	        
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
	        s.set("spawn.world." + player.getWorld().getName(), player.getWorld().getName());
	        s.set("spawn.x." + player.getWorld().getName(), player.getLocation().getX());
	        s.set("spawn.y." + player.getWorld().getName(), player.getLocation().getY());
	        s.set("spawn.z." + player.getWorld().getName(), player.getLocation().getZ());
	        s.set("spawn.yaw." + player.getWorld().getName(), Float.valueOf(player.getLocation().getYaw()));
	        s.set("spawn.pitch." + player.getWorld().getName(), Float.valueOf(player.getLocation().getPitch()));
	        s.set("spawn.version." + player.getWorld().getName(), Main.spawnversion);
	        File nome = new File(plugin.getDataFolder() +File.separator + "spawns" + File.separator + player.getWorld().getName());
	        File dir = new File(plugin.getDataFolder() +File.separator + "spawns");
	        if(!dir.exists())
	        {
	        	dir.mkdir();
	        }
	        if (!nome.exists()) {
	        	try {
					nome.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
			          player.sendMessage(ChatColor.RED + SET_ERROR);
			          return true;
				}
	        }
	        try {
	          s.save(spawn);
	        } catch (Exception e) {
				String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
	          player.sendMessage(ChatColor.RED + SET_ERROR);
	          return true;
	        }
	        player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), 
	        player.getLocation().getBlockY(), player.getLocation().getBlockZ());

			String SPAWN_SUCCESS_1 = plugin.getConfig().getString("setspawn.SPAWN_SUCCESS_1").replace("%world%", player.getLocation().getWorld().getName()).replace("&", "§");
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
	        player.sendMessage(PREFIX + ChatColor.GREEN  + SPAWN_SUCCESS_1);
			} else {
				ArrayList<String> worlds = new ArrayList<String>();
				for (World ww : Bukkit.getWorlds()) {
					worlds.add(ww.getName());
				}
				
				if(worlds.contains(args[0])) {
					File spawn = new File(plugin.getDataFolder() +File.separator + "spawn.yml");
			        if (!spawn.exists()) {
			          try {
			            spawn.createNewFile();
			          }
			          catch (Exception e)
			          {
			            e.printStackTrace();

						String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
						String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
			            player.sendMessage(PREFIX + ChatColor.RED + SET_ERROR);
			            return true;
			          }
			        }

			        s = YamlConfiguration.loadConfiguration(spawn);
			        
			        s.set("spawn.world." + args[0], player.getWorld().getName());
			        s.set("spawn.x." + args[0], player.getLocation().getX());
			        s.set("spawn.y." + args[0], player.getLocation().getY());
			        s.set("spawn.z." + args[0], player.getLocation().getZ());
			        s.set("spawn.yaw." + args[0], Float.valueOf(player.getLocation().getYaw()));
			        s.set("spawn.pitch." + args[0], Float.valueOf(player.getLocation().getPitch()));
			        s.set("spawn.version." + args[0], Main.spawnversion);
			        File nome = new File(plugin.getDataFolder() +File.separator + "spawns" + File.separator + args[0]);
			        if (!nome.exists()) {
			        	try {
							nome.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
							String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
					          player.sendMessage(ChatColor.RED + SET_ERROR);
					          return true;
						}
			        }
			        try {
			          s.save(spawn);
			        } catch (Exception e) {
						String SET_ERROR = plugin.getConfig().getString("setspawn.SET_ERROR").replace("&", "§");
			          player.sendMessage(ChatColor.RED + SET_ERROR);
			          return true;
			        }
			        Bukkit.getWorld(args[0]).setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());

					String SPAWN_SUCCESS_1 = plugin.getConfig().getString("setspawn.SPAWN_SUCCESS_1").replace("%world%", args[0]).replace("&", "§");
					String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
			        player.sendMessage(PREFIX + ChatColor.GREEN  + SPAWN_SUCCESS_1);
				}
				worlds.clear();
			}
			}else{

				String NO_PERMISSIONS = plugin.getConfig().getString("setspawn.NO_PERMISSIONS").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
		          player.sendMessage(PREFIX + ChatColor.RED +  NO_PERMISSIONS);
			}
		}
		return false;
	}
}
