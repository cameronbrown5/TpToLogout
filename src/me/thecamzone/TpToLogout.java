package me.thecamzone;

import org.bukkit.plugin.java.JavaPlugin;

import me.thecamzone.Commands.TpToLogoutCommand;
import me.thecamzone.Events.PlayerQuitHandler;
import me.thecamzone.Files.DataFile;
import me.thecamzone.Utility.TeleportUtil;

public class TpToLogout extends JavaPlugin {

	private static TpToLogout plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		makeDir();
		DataFile.setup();
		this.saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(new PlayerQuitHandler(), this);
		getServer().getPluginManager().registerEvents(new TeleportUtil(), this);
		
		getCommand("tptologout").setExecutor(new TpToLogoutCommand());
	}
	
	public static TpToLogout getInstance() {
		return plugin;
	}
	
	private Boolean makeDir() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
			return true;
		}
		return false;
	}
	
}
