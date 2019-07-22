package net.mindoverflow.hubthat;

import java.io.IOException;

import org.bukkit.scheduler.BukkitRunnable;

public class UpdatesTimer extends BukkitRunnable {
	
	public Main plugin;
	public UpdatesTimer(Main plugin) {
		this.plugin = plugin;
	}
	
	public static String tlink;
	public static String tversion;
	public static String text;
	public static boolean updateNeeded;
	public static String warning;
	
	@Override
	public void run() {

		if(plugin.getConfig().getBoolean("updates.update-notify")){
			try {
				updateNeeded = UpdateChecker.updateNeeded();
				tversion = UpdateChecker.getVersion();
				tlink = UpdateChecker.getLink();
				text = UpdateChecker.updateText();
				warning = UpdateChecker.warning();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
