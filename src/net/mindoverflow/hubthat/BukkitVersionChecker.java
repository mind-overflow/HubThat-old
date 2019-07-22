package net.mindoverflow.hubthat;

import org.bukkit.Bukkit;

public class BukkitVersionChecker {
	public Main plugin;
	public BukkitVersionChecker (Main plugin){
		this.plugin = plugin;
	}
	

	
	public static int getFirstNum() {
		
		String mcVersion = Bukkit.getVersion().substring(Bukkit.getVersion().indexOf("(")+1,Bukkit.getVersion().indexOf(")"));
		String firstNum = mcVersion.substring(mcVersion.indexOf(" ")+1,mcVersion.indexOf("."));
		return Integer.parseInt(firstNum);
	}

	public static int getSecondNum() {

		String mcVersion = Bukkit.getVersion().substring(Bukkit.getVersion().indexOf("(")+1,Bukkit.getVersion().indexOf(")"));
		String secondNum = mcVersion.substring(mcVersion.indexOf(".")+1, mcVersion.lastIndexOf("."));
		return Integer.parseInt(secondNum);
	}
	
	public static boolean isLowerThan(int first, int second)
	{
		if (getFirstNum() == first) {
			if (getSecondNum() < second) return true;
			return false;
		}
		if (getFirstNum() < first) return true;
		return false;
	}

	public static boolean isHigherThan(int first, int second)
	{
		if (getFirstNum() == first) {
			if (getSecondNum() > second) return true;
			return false;
		}
		if (getFirstNum() > first) return true;
		return false;
	}
	
	public static boolean isVersion(int first, int second)
	{
		if (getFirstNum() == first && getSecondNum() == second) return true;
		return false;
	}
}
