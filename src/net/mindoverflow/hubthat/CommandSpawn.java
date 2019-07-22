package net.mindoverflow.hubthat;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor{
	public static Main plugin;
	@SuppressWarnings("static-access")
	public CommandSpawn(Main plugin){
		this.plugin = plugin;
	}
	YamlConfiguration s;
	static int id = -1;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args){
		if(!(sender instanceof Player)){
			String ONLY_PLAYERS = plugin.getConfig().getString("spawn.ONLY_PLAYERS").replace("&", "§");
			sender.sendMessage(ChatColor.DARK_RED + ONLY_PLAYERS);
			return true;
		}
		File spawn = new File(plugin.getDataFolder() + File.separator + "spawn.yml");
		final Player player = (Player) sender;
		if (args.length == 1) {
			if(Bukkit.getWorld(args[0]) == null) {
				sender.sendMessage("§cError! World does not exist or is not loaded.");
			} else {
				s = YamlConfiguration.loadConfiguration(spawn);
				File nome = new File (plugin.getDataFolder() + File.separator + "spawns" + File.separator + args[0]);
				if(!(nome.exists())){
					String NOTSET = plugin.getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
					String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
					player.sendMessage(PREFIX + ChatColor.GREEN + NOTSET);
				} else if (nome.exists()){
				  	final double yaw = s.getDouble("spawn.yaw." + args[0]);
			        final double pitch = s.getDouble("spawn.pitch." + args[0]);
			        World world = Bukkit.getWorld(s.getString("spawn.world." + args[0]));
					double x = s.getDouble("spawn.x." + args[0]);
					double y = s.getDouble("spawn.y." + args[0]);
					double z = s.getDouble("spawn.z." + args[0]);
			        final Location loc = new Location(world, x, y, z);
			        loc.setYaw((float)yaw);
			        loc.setPitch((float)pitch);
			        player.teleport(loc);
							String TELEPORTED = plugin.getConfig().getString("spawn.TELEPORTED");
							String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
							player.sendMessage(PREFIX + ChatColor.GREEN + TELEPORTED.replace("&", "§"));
			        
				}
			}
		} else {
		if(!Main.tporting.containsKey(player.getName())) {
			Main.tporting.put(player.getName(), false);
		}
		if(CommandLabel.equalsIgnoreCase("spawn")){
			if(sender.hasPermission(new permission().Spawn)){
			
			if(!spawn.exists()){
				String SPAWN_NOT_SET = plugin.getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
		        player.sendMessage(PREFIX + ChatColor.RED + SPAWN_NOT_SET);
		        return true;
			}
			if(!sender.hasPermission(new permission().SpawnDelayBypass)){
				
				s = YamlConfiguration.loadConfiguration(spawn);
				
				File nome = new File (plugin.getDataFolder() + File.separator + "spawns" + File.separator + player.getWorld().getName());
				if(!(nome.exists())){
					
					String NOTSET = plugin.getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
					String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
					player.sendMessage(PREFIX + ChatColor.GREEN + NOTSET);
				} else if (nome.exists()){
				
			Long spawndelay = plugin.getConfig().getLong("spawn.delay") * 20;
			String DELAY_TEXT_WAIT = plugin.getConfig().getString("spawn.DELAY_TEXT_WAIT").replace("%sec%", spawndelay / 20 + "").replace("&", "§");
			if(Main.tporting.get(player.getName())){
				String ALREADY_TP = plugin.getConfig().getString("global.ALREADY-TELEPORTING").replace("&", "§");
				String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
				player.sendMessage(PREFIX + ALREADY_TP);
				return true;
			} else {
				Main.tporting.put(player.getName(), true);
			}
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
			player.sendMessage(PREFIX + ChatColor.GREEN + DELAY_TEXT_WAIT);
			id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				  public void run() {
				        if(Main.tporting.get(player.getName())){
					  	final double yaw = s.getDouble("spawn.yaw." + player.getWorld().getName());
				        final double pitch = s.getDouble("spawn.pitch." + player.getWorld().getName());
				        World world = Bukkit.getWorld(s.getString("spawn.world." + player.getWorld().getName()));
						double x = s.getDouble("spawn.x." + player.getWorld().getName());
						double y = s.getDouble("spawn.y." + player.getWorld().getName());
						double z = s.getDouble("spawn.z." + player.getWorld().getName());
				        final Location loc = new Location(world, x, y, z);
				        loc.setYaw((float)yaw);
				        loc.setPitch((float)pitch);
				        player.teleport(loc);
				        
						String TELEPORTED = plugin.getConfig().getString("spawn.TELEPORTED");
						String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
						player.sendMessage(PREFIX + ChatColor.GREEN + TELEPORTED.replace("&", "§"));
						Main.tporting.put(player.getName(), false);
				        }
				  }
				}, spawndelay);
			}
			} else if(sender.hasPermission(new permission().SpawnDelayBypass)){
				
				s = YamlConfiguration.loadConfiguration(spawn);
				File nome = new File (plugin.getDataFolder() + File.separator + "spawns" + File.separator + player.getWorld().getName());
				if(!(nome.exists())){
					
					String NOTSET = plugin.getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
					String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
					player.sendMessage(PREFIX + ChatColor.GREEN + NOTSET);
				} else if (nome.exists()){
				  	final double yaw = s.getDouble("spawn.yaw." + player.getWorld().getName());
			        final double pitch = s.getDouble("spawn.pitch." + player.getWorld().getName());
			        World world = Bukkit.getWorld(s.getString("spawn.world." + player.getWorld().getName()));
					double x = s.getDouble("spawn.x." + player.getWorld().getName());
					double y = s.getDouble("spawn.y." + player.getWorld().getName());
					double z = s.getDouble("spawn.z." + player.getWorld().getName());
			        final Location loc = new Location(world, x, y, z);
			        loc.setYaw((float)yaw);
			        loc.setPitch((float)pitch);
			        player.teleport(loc);
							String TELEPORTED = plugin.getConfig().getString("spawn.TELEPORTED");
							String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
							player.sendMessage(PREFIX + ChatColor.GREEN + TELEPORTED.replace("&", "§"));
			        
				}
			}
		}else{

			String NO_PERMISSIONS = plugin.getConfig().getString("spawn.NO_PERMISSIONS").replace("&", "§");
			String PREFIX = plugin.getConfig().getString("global.PREFIX").replace("&", "§");
	          player.sendMessage(PREFIX + ChatColor.RED +  NO_PERMISSIONS);
		}
		}
	}
		return false;
	}
	
	
	public static void cancelSpawnTask() {
		if(id != -1) {
			plugin.getServer().getScheduler().cancelTask(id);
		}
	}
	
}
