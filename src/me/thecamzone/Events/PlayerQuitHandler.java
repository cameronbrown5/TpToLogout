package me.thecamzone.Events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.thecamzone.TpToLogout;
import me.thecamzone.Files.DataFile;

public class PlayerQuitHandler implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		
		List<String> configWorlds = TpToLogout.getInstance().getConfig().getStringList("config.exclude-log-out-worlds");
		if(configWorlds.contains(world.getName())) {
			return;
		}
		
		Location loc = player.getLocation();
		
		double playerX = round(loc.getX());
		double playerY = round(loc.getY());
		double playerZ = round(loc.getZ());
		double playerYaw = round(loc.getYaw());
		double playerPitch = round(loc.getPitch());
		
		DataFile.get().set("Locations." + player.getUniqueId(), world.getName() + ", " + playerX + ", " + playerY + ", " + playerZ + ", " + playerYaw + ", " + playerPitch);
		DataFile.save();
	}
	
	private double round(double input) {
		input *= 100;
		input = Math.round(input);
		input /= 100;
		
		return input;
	}
	
}
