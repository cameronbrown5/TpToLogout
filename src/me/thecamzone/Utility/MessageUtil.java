package me.thecamzone.Utility;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.thecamzone.TpToLogout;
import net.md_5.bungee.api.ChatColor;

public class MessageUtil {

	public static String getConfigString(String path) {
		String message = TpToLogout.getInstance().getConfig().getString(path);
		
		if(message == null) {
			Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Could not find " + path + " message in config.yml.");
			Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Most likely due to a broken config.yml.");
			return null;
		}
		
		message = ChatColor.translateAlternateColorCodes('&', message);

		return message;
	}
	
}
