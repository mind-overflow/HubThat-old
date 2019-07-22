package net.mindoverflow.hubthat;

import org.bukkit.permissions.Permission;

public class permission {
	public Permission Spawn;
	public Permission SetSpawn;
	public Permission SetHub;
	public Permission Hub;
	public Permission HubDelayBypass;
	public Permission SpawnDelayBypass;
	public Permission GotoWorld;
	public Permission ListWorlds;
	public Permission SeeUpdates;
	public Permission ReloadConfig;
	public permission()
	{
		Spawn = new Permission ("hubthat.spawn");
		SetSpawn = new Permission ("hubthat.setspawn");
        SetHub = new Permission ("hubthat.sethub");
        Hub = new Permission ("hubthat.hub");
        SpawnDelayBypass = new Permission ("hubthat.nospawndelay");
        HubDelayBypass = new Permission ("hubthat.nohubdelay");
        GotoWorld = new Permission ("hubthat.gotoworld");
        ListWorlds = new Permission ("hubthat.listworlds");
        SeeUpdates = new Permission ("hubthat.updates");
        ReloadConfig = new Permission ("hubthat.reloadconfig");
	}
}
