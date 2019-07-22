package net.mindoverflow.hubthat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public static HashMap <String, Boolean> tporting = new HashMap <String, Boolean> ();
	public static HashMap <String, String> worldrespawn = new HashMap <String, String> ();
	public static Main plugin;
	public static double spawnversion = 2.5;
	public static double version = 8.0;
	public final Logger logger = Logger.getLogger("Minecraft");
	public UpdateChecker UpdateChecker;
	protected Logger log;
	public File sharesFile = new File(getDataFolder() + File.separator + "shares.yml");
	public FileConfiguration shares = (YamlConfiguration.loadConfiguration(sharesFile));
	File pluginfolder = new File(getDataFolder() + "");
	File config = new File(getDataFolder() + File.separator + "config.yml");
	File spawnsfolder = new File(getDataFolder() + File.separator + "spawns");
	File hubFile = new File(getDataFolder() + File.separator + "hub.yml");
	File spawnFile = new File(getDataFolder() + File.separator + "spawn.yml");
	YamlConfiguration spawnYaml = YamlConfiguration.loadConfiguration(spawnFile);
	YamlConfiguration hubYaml = YamlConfiguration.loadConfiguration(hubFile);

	@SuppressWarnings("static-access")
	@Override
	public void onEnable()
	{
		if (!pluginfolder.exists())
		{
			pluginfolder.mkdir();
		}
    
		if(!spawnsfolder.exists())
		{
			spawnsfolder.mkdir();
		}
	
		if(!config.exists())
		{
			saveDefaultConfig(); 
			reloadConfig();
		}

	this.log = this.getLogger();

	getServer().getPluginManager().registerEvents(this, this);
	getServer().getPluginManager().registerEvents(new CommandHub(this), this);

	CommandHub CommandHub = new CommandHub(this);
	CommandSetHub CommandSetHub = new CommandSetHub(this);
	CommandSetSpawn CommandSetSpawn = new CommandSetSpawn(this);
	CommandSpawn CommandSpawn = new CommandSpawn(this);
	CommandGotoWorld CommandGotoWorld = new CommandGotoWorld(this);
	CommandListWorlds CommandListWorlds = new CommandListWorlds(this);
	
	getCommand("hub").setExecutor(CommandHub);
	getCommand("sethub").setExecutor(CommandSetHub);
	getCommand("setspawn").setExecutor(CommandSetSpawn);
	getCommand("spawn").setExecutor(CommandSpawn);
	getCommand("worldtp").setExecutor(CommandGotoWorld);
	getCommand("worldlist").setExecutor(CommandListWorlds);
	if(this.getConfig().getBoolean("updates.update-notify"))
	{
		UpdateChecker = new UpdateChecker(this);
		try
		{
			if(UpdateChecker.linksValid())
			{
				if(UpdateChecker.updateNeeded())
				{
					new UpdatesTimer(this).runTaskTimer(this, 0 * 20L, 5 * 60 * 20L);
					log.info(ChatColor.GRAY + "A new version is out: " + ChatColor.GOLD + UpdateChecker.getVersion() + ChatColor.GRAY + "!");
					log.info(ChatColor.GRAY + "Download: " + ChatColor.GOLD + UpdateChecker.getLink());
					log.info(UpdateChecker.updateText());
					try
					{
						if(UpdateChecker.linksValid())
						{
							if(UpdateChecker.warning() != "")
							{
								this.log.info(UpdateChecker.warning());
							}
						}
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
			else
			{
				log.info(ChatColor.GRAY + "There's a " + ChatColor.RED + "Problem" + ChatColor.GRAY + " with the updates server.");
				log.info(ChatColor.GRAY + "It may be in maintenance mode.");
				log.info(ChatColor.GRAY + "Please check if there are new updates manually.");
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	else if (!this.getConfig().getBoolean("updates.update-notify"))
	{
		String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
		log.info(PREFIX + ChatColor.RED + "Update Checking Disabled!");
	}
	
	if(getConfig().getDouble("global.VERSION") < version)
	{
		getConfig().set("global.FIRSTRUN", false);
		try
		{
			getConfig().save(config);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		reloadConfig();
	}
	
	
	if(getConfig().getDouble("global.VERSION") < 7.0)
		{
			getConfig().set("global.respawn-handler", true);
		}
	
		if(getConfig().getBoolean("global.FIRSTRUN") == false && getConfig().getDouble("global.VERSION") <= 4.1 || getConfig().getDouble("global.VERSION") == 6.0)
		{
			Bukkit.broadcastMessage(("&0[&6HT&0] &4WARNING: HUBTHAT CONFIGURATION UNCOMPATIBLE; DELETING!").replace("&", "§"));
			config.delete();
			Bukkit.broadcastMessage(("&0[&6HT&0] &4WARNING: HUBTHAT CONFIGURATION DELETED; CREATING A NEW ONE!").replace("&", "§"));
			saveDefaultConfig();
			getConfig().set("global.FIRSTRUN", false);
			Bukkit.broadcastMessage(("&0[&6HT&0] &4WARNING: HUBTHAT CONFIGURATION CREATED; RELOADING CONFIG!").replace("&", "§"));
			reloadConfig();
		}
	if(getConfig().getDouble("global.VERSION") <= 6.5)
		{
			getConfig().set("global.tp-hub-on-join", true);
			getConfig().set("global.tp-hub-on-respawn", false);
			try
			{
				getConfig().save(config);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	if(getConfig().getDouble("global.VERSION") <= 7.3)
	{
		getConfig().set("global.set-gamemode-on-join", true);
		getConfig().set("global.gamemode", 0);
		getConfig().set("global.world-related-chat", true);
		try
		{
			getConfig().save(config);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
		if(getConfig().getDouble("global.VERSION") != version)
		{
			getConfig().set("global.VERSION", version);
			try
			{
				getConfig().save(config);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		reloadConfig();

	    Metrics metrics = new Metrics(this);
	    metrics.addCustomChart(new Metrics.SimplePie("respawn-handler", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.respawn-handler");
	        }
	    }));
	    metrics.addCustomChart(new Metrics.SimplePie("world-related-chat", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.world-related-chat");
	        }
	    }));
	    metrics.addCustomChart(new Metrics.SimplePie("update-notify", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("updates.update-notify");
	        }
	    }));
	    metrics.addCustomChart(new Metrics.SimplePie("set-gamemode-on-join", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.set-gamemode-on-join");
	        }
	    }));
	    metrics.addCustomChart(new Metrics.SimplePie("tp-hub-on-join", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.tp-hub-on-join");
	        }
	    }));
	    metrics.addCustomChart(new Metrics.SimplePie("tp-hub-on-respawn", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.tp-hub-on-respawn");
	        }
	    }));
	    if(getConfig().getBoolean("global.set-gamemode-on-join"))
	    {
	    	metrics.addCustomChart(new Metrics.SimplePie("join-gamemode", new java.util.concurrent.Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return getConfig().getString("global.gamemode");
	        }
	    }));
	    }
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void playerJoin(final PlayerJoinEvent e)
	{
		
		Player player = e.getPlayer();
		tporting.put(player.getName(), false);
		if(player.getName().equalsIgnoreCase("lol7344") || player.getName().equalsIgnoreCase("mind_overflow") || player.getUniqueId().equals(UUID.fromString("297a1dc8-c0a3-485a-ad21-8956c749f927")))
		{
			Timer timer = new Timer();
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					player.sendMessage(("&0[&6HT&0]§7 This server is running §3HubThat&7 v.&3" + version + "&7!").replace("&", "§"));
				}
			}, 1000);
		}
		

		
		if(hubFile.exists())
		{
			if(this.getConfig().getBoolean("global.tp-hub-on-join"))
			{
				String world = hubYaml.getString("hub.world");;
				double x = hubYaml.getDouble("hub.x");
				double y = hubYaml.getDouble("hub.y");
				double z = hubYaml.getDouble("hub.z");
				final double yaw = hubYaml.getDouble("hub.yaw");
				final double pitch = hubYaml.getDouble("hub.pitch");
			  	final Location loc = new Location(Bukkit.getWorld(world), x, y, z);
			  	loc.setYaw((float)yaw);
				loc.setPitch((float)pitch);
				player.teleport(loc);
			}
		}
		else
		{
			if(this.getConfig().getBoolean("global.tp-hub-on-join"))
				{
				String not_set = getConfig().getString("hub.HUB_NOT_SET").replace("&", "§");
				String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
				player.sendMessage(PREFIX + ChatColor.RED + not_set);
				}
		}
		
		if(this.getConfig().getBoolean("global.set-gamemode-on-join"))
		{
			int gamemode = this.getConfig().getInt("global.gamemode");
			if(gamemode == 0)
			{
				setGamemode(player, GameMode.SURVIVAL);
			} else if (gamemode == 1)
			{
				setGamemode(player, GameMode.CREATIVE);
			} else if (gamemode == 2)
			{
				setGamemode(player, GameMode.ADVENTURE);
			} else if (gamemode == 3)
			{
				if(BukkitVersionChecker.isHigherThan(1, 7))
				{
					setGamemode(player, GameMode.SPECTATOR);
				} else {
					player.sendMessage(ChatColor.RED + "Spectator mode was not available in Minecraft 1.7 and lower!");
				}
			} else
			{
				player.sendMessage(ChatColor.RED + "Invalid HubThat join gamemode! Please contact an admin or check config file.");
			}
		}
		
		if(this.getConfig().getBoolean("updates.update-notify"))
		{
			if(player.isOp() || player.hasPermission(new permission().SeeUpdates))
			{
				if(UpdateChecker.linksValid())
				{
						if(UpdatesTimer.updateNeeded)
						{
							Timer timer = new Timer();
							final String tversion = UpdatesTimer.tversion;
							final String tlink = UpdatesTimer.tlink;
							final String text = UpdatesTimer.text;
							timer.schedule(new TimerTask()
							{
								public void run()
									{
										e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----[" + ChatColor.RESET + "" + ChatColor.DARK_AQUA + "  HubThat Updater  " + ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "]-----" + ChatColor.RESET);
										e.getPlayer().sendMessage(ChatColor.GRAY + "A new version is out: " + ChatColor.GOLD + tversion + ChatColor.GRAY + "!");
										e.getPlayer().sendMessage(ChatColor.GRAY + "Download: " + ChatColor.GOLD + tlink);
										if(text != "") {e.getPlayer().sendMessage(text);}
									}
							}, 200);
						}
				}
				else
				{
					Timer timer = new Timer();
					timer.schedule(new TimerTask()
					{
						public void run()
						{
							e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----[" + ChatColor.RESET + "" + ChatColor.DARK_AQUA + "  HubThat Updater  " + ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "]-----" + ChatColor.RESET);
							e.getPlayer().sendMessage(ChatColor.GRAY + "There's a " + ChatColor.RED + "Problem" + ChatColor.GRAY + " with the updates server.");
							e.getPlayer().sendMessage(ChatColor.GRAY + "It may be in maintenance mode.");
							e.getPlayer().sendMessage(ChatColor.GRAY + "Please check if the plugin is updated manually.");
							e.getPlayer().sendMessage(ChatColor.GRAY + "Check console logs for details.");
						}
					}, 200);
				}				
			}
		}


		if(e.getPlayer().isOp() || player.hasPermission(new permission().SeeUpdates))
		{
			final String warning = UpdatesTimer.warning;
			Timer timer = new Timer();
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					if(warning != "") {e.getPlayer().sendMessage(warning);}
				}
			}, 300);
		}
		return;
	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{

		final Player player = e.getPlayer();
		if(tporting.containsKey(player.getName()))
		{
			tporting.remove(player.getName());
		}
	
		if(worldrespawn.containsKey(player.getName()))
		{
			worldrespawn.remove(player.getName());
		}
	
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		if(!tporting.containsKey(player.getName()))
		{
			tporting.put(player.getName(), false);
		}
		if(tporting.get(player.getName()))
		{
			if(getConfig().getBoolean("global.move-detect"))
			{
				if(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ())
				{

					String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
					String MOVED = this.getConfig().getString("global.MOVED");
					player.sendMessage(PREFIX + MOVED.replace("&", "§"));
					tporting.put(player.getName(), false);
					CommandSpawn.cancelSpawnTask();
				}
			}
		}
	}
	
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

	if(!(sender instanceof Player))
	{
	    if (command.getName().equalsIgnoreCase("hubthat"))
	    {
	    	if(args.length == 1 && args[0].equalsIgnoreCase("reload"))
	    	{
	    		reloadConfig();
	    		spawnYaml = YamlConfiguration.loadConfiguration(spawnFile);
	    		if(this.getConfig().getBoolean("updates.update-notify"))
	    		{
	    			UpdateChecker = new UpdateChecker(this);
	    		}	
	    		this.log.info(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "HubThat configuration reloaded!");
	            return true;
	    		}
	    		else if (args.length == 1 && args[0].equalsIgnoreCase("help"))
	    		{
	    			this.log.info(ChatColor.DARK_GRAY + "---------" + ChatColor.BLACK + "[" + ChatColor.GOLD + "HubThat Help Page" + ChatColor.BLACK + "]" + ChatColor.DARK_GRAY + "---------");
	    			this.log.info(ChatColor.GOLD + "/hubthat" + ChatColor.GRAY + ": show HubThat info");
	    			this.log.info(ChatColor.GOLD + "/hubthat help" + ChatColor.GRAY + ": show this page");
	    			this.log.info(ChatColor.GOLD + "/hubthat reload" + ChatColor.GRAY + ": reload the config - " + ChatColor.DARK_GRAY + "hubthat.reloadconfig");
	    			this.log.info(ChatColor.GOLD + "/hub" + ChatColor.GRAY + ": teleport to the Hub - " +  ChatColor.DARK_GRAY + "hubthat.hub");
	    			this.log.info(ChatColor.GOLD + "/spawn" + ChatColor.GRAY + ": teleport to current world's Spawn - "  + ChatColor.DARK_GRAY + "hubthat.spawn");
	    			this.log.info(ChatColor.GOLD + "/sethub" + ChatColor.GRAY + ": set server's Hub - " + ChatColor.DARK_GRAY + "hubthat.sethub");
	    			this.log.info(ChatColor.GOLD + "/setspawn" + ChatColor.GRAY + ": set current world's Spawn - " + ChatColor.DARK_GRAY + "hubthat.setspawn");
	    			this.log.info(ChatColor.GOLD + "/worldlist" + ChatColor.GRAY + ": list all the worlds - "  + ChatColor.DARK_GRAY + "hubthat.listworlds");
	    			this.log.info(ChatColor.GOLD + "/worldtp <world>" + ChatColor.GRAY + ": teleport to a world - " + ChatColor.DARK_GRAY + "hubthat.gotoworld");
	    			return true;
	    		}
	    		else
	    		{
	    			this.log.info(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "HubThat Version " + ChatColor.GOLD + version + ChatColor.GRAY +  " for SpigotMC/CraftBukkit " + ChatColor.GOLD + "1.7" + ChatColor.GRAY + "-" + ChatColor.GOLD + "1.14" + ChatColor.GRAY + ".");
	    			this.log.info(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "Coded by " + ChatColor.GOLD + "lol7344" + ChatColor.GRAY + ", all rights reserved. (" + ChatColor.GOLD + "Copyright" + ChatColor.GRAY + ").");
	    			this.log.info(" ");
	    			this.log.info(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "Write "  + ChatColor.GOLD + "/hubthat help " + ChatColor.GRAY + "to see plugin commands.");
	    			return true;
	    		}
	    	}
		}
    	Player player = (Player)sender;
    	if (command.getName().equalsIgnoreCase("hubthat"))
    	{
    		if(args.length == 1 && args[0].equalsIgnoreCase("reload"))
    		{
    			if(player.hasPermission(new permission().ReloadConfig))
    			{
    				reloadConfig();
    				sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "HubThat configuration reloaded!");
    	        	return true;
    			}
    			else
    			{
    				String hubnoperm = getConfig().getString("worldlist.NO_PERMISSIONS").replace("&", "§");
    				String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
    	    		sender.sendMessage(PREFIX + ChatColor.RED +  hubnoperm);
    			}

    		}
    		else if (args.length == 1 && args[0].equalsIgnoreCase("help"))
    		{
    			sender.sendMessage(ChatColor.DARK_GRAY + "---------" + ChatColor.BLACK + "[" + ChatColor.GOLD + "HubThat Help Page" + ChatColor.BLACK + "]" + ChatColor.DARK_GRAY + "---------");
    			sender.sendMessage(ChatColor.GOLD + "/hubthat" + ChatColor.GRAY + ": show HubThat info");
    			sender.sendMessage(ChatColor.GOLD + "/hubthat help" + ChatColor.GRAY + ": show this page");
    			sender.sendMessage(ChatColor.GOLD + "/hubthat reload" + ChatColor.GRAY + ": reload the config - " + ChatColor.DARK_GRAY + "hubthat.reloadconfig");
    			sender.sendMessage(ChatColor.GOLD + "/hub" + ChatColor.GRAY + ": teleport to the Hub - " +  ChatColor.DARK_GRAY + "hubthat.hub");
    			sender.sendMessage(ChatColor.GOLD + "/spawn" +  ChatColor.DARK_GRAY + " <world>" + ChatColor.GRAY + ": teleport to current/another world's Spawn - "  + ChatColor.DARK_GRAY + "hubthat.spawn");
    			sender.sendMessage(ChatColor.GOLD + "/sethub" + ChatColor.GRAY + ": set server's Hub - " + ChatColor.DARK_GRAY + "hubthat.sethub");
    			sender.sendMessage(ChatColor.GOLD + "/setspawn" +  ChatColor.DARK_GRAY + " <world>" + ChatColor.GRAY + ": set current/another world's Spawn - " + ChatColor.DARK_GRAY + "hubthat.setspawn");
    			sender.sendMessage(ChatColor.GOLD + "/worldlist" + ChatColor.GRAY + ": list all the worlds - "  + ChatColor.DARK_GRAY + "hubthat.listworlds");
    			sender.sendMessage(ChatColor.GOLD + "/worldtp <world>" + ChatColor.GRAY + ": teleport to a world - " + ChatColor.DARK_GRAY + "hubthat.gotoworld");
    			return true;
    		}
    		else
    		{
    			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "HubThat Version " + ChatColor.GOLD + version + ChatColor.GRAY +  " for SpigotMC/CraftBukkit " + ChatColor.GOLD + "1.7" + ChatColor.GRAY + "-" + ChatColor.GOLD + "1.14" + ChatColor.GRAY + ".");
    			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "Coded by " + ChatColor.GOLD + "mind_overflow" + ChatColor.GRAY + ", all rights reserved. (" + ChatColor.GOLD + "Copyright" + ChatColor.GRAY + ").");
    			sender.sendMessage(" ");
    			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GOLD + "HT" + ChatColor.BLACK + "] " + ChatColor.GRAY + "Write "  + ChatColor.GOLD + "/hubthat help " + ChatColor.GRAY + "to see plugin commands.");
    			return true;
    		}
    	}
    	return false;
	}


@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
{
	Player sender = e.getPlayer();
	if(getConfig().getBoolean("global.world-related-chat"))
	{
		String senderWorldSpawn = spawnYaml.getString("spawn.world." + sender.getWorld().getName(), "unset");
		for (Player receiver : Bukkit.getOnlinePlayers())
		{
			String receiverWorldSpawn = spawnYaml.getString("spawn.world." + receiver.getWorld().getName(), "unset");
			if (!senderWorldSpawn.equalsIgnoreCase(receiverWorldSpawn))
			{
				e.getRecipients().remove(receiver);
			}
		}
	}
}

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent e)
	{
		final Player player = e.getPlayer();
		if(worldrespawn.containsKey(player.getName()))
		{
			if(getConfig().getBoolean("global.respawn-handler"))
			{
				if(getConfig().getBoolean("global.tp-hub-on-respawn"))
				{
					File hub = new File(getDataFolder() + File.separator + "hub.yml");
					if(hub.exists())
					{
						YamlConfiguration s = YamlConfiguration.loadConfiguration(hub);
						final double yaw = s.getDouble("hub.yaw");
						final double pitch = s.getDouble("hub.pitch");
						World world = Bukkit.getWorld(s.getString("hub.world"));
						double x = s.getDouble("hub.x");
						double y = s.getDouble("hub.y");
						double z = s.getDouble("hub.z");
						final Location loc = new Location(world, x, y, z);
						loc.setYaw((float)yaw);
						loc.setPitch((float)pitch);
						Bukkit.getScheduler().runTaskLater(this, new Runnable()
						{
							public void run()
							{
								player.teleport(loc);
								worldrespawn.remove(player.getName());
							}
						}, 10);
					}	
					else
					{
						String NOTSET = getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
						String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
						player.sendMessage(PREFIX + ChatColor.GREEN + NOTSET);
						worldrespawn.remove(player.getName());
					}
				}
				else
				{
					File nome = new File (getDataFolder() + File.separator + "spawns" + File.separator + player.getWorld().getName());
					if(!(nome.exists()))
					{
						String NOTSET = getConfig().getString("spawn.SPAWN_NOT_SET").replace("&", "§");
						String PREFIX = getConfig().getString("global.PREFIX").replace("&", "§");
						player.sendMessage(PREFIX + ChatColor.GREEN + NOTSET);
						worldrespawn.remove(player.getName());
					}
					else if (nome.exists())
					{
						final double yaw = spawnYaml.getDouble("spawn.yaw." + player.getWorld().getName());
						final double pitch = spawnYaml.getDouble("spawn.pitch." + player.getWorld().getName());
						World world = Bukkit.getWorld(spawnYaml.getString("spawn.world." + player.getWorld().getName()));
						double x = spawnYaml.getDouble("spawn.x." + player.getWorld().getName());
						double y = spawnYaml.getDouble("spawn.y." + player.getWorld().getName());
						double z = spawnYaml.getDouble("spawn.z." + player.getWorld().getName());
						final Location loc = new Location(world, x, y, z);
						loc.setYaw((float)yaw);
						loc.setPitch((float)pitch);
						Bukkit.getScheduler().runTaskLater(this, new Runnable()
						{
							public void run()
							{
								player.teleport(loc);
								worldrespawn.remove(player.getName());
							}
						}, 10);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if (worldrespawn.containsKey(e.getEntity().getName()))
		{
			worldrespawn.remove(e.getEntity().getName());
		}
	worldrespawn.put(e.getEntity().getName(), e.getEntity().getWorld().getName());
	}
	public void setGamemode(Player p, GameMode g)
	{
		p.setGameMode(g);
		Bukkit.getScheduler().runTaskLater(this, new Runnable()
		{

			@Override
			public void run() {
				p.setGameMode(g);
				//"This is sketchy, but needed because of how popular Multiverse is.
			}
		
		}, 15);
	}
	
	public void reloadConfig()
	{
		super.reloadConfig();
	}
}