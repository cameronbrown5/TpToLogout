package me.thecamzone.Utility;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.thecamzone.TpToLogout;
import net.md_5.bungee.api.ChatColor;

public class TeleportUtil implements Listener {

	static TpToLogout plugin = TpToLogout.getInstance();
	
	static Map<Player, BukkitTask> teleportCountdowns = new HashMap<>();
	
	public static void teleport(Player player, Location location, int delay) {
		if(teleportCountdowns.containsKey(player)) {
			return;
		}
		
		BukkitTask runnable = new BukkitRunnable() {
	        
			Integer timeLeft = delay;
			
			@Override
			public synchronized void cancel() throws IllegalStateException {
				Bukkit.getScheduler().cancelTask(getTaskId());
				
				if(teleportCountdowns.get(player) != null) {
					teleportCountdowns.remove(player);
				}
			}
			
            @Override
            public void run() {
            	if(player == null) {
            		this.cancel();
            		return;
            	}
            	
            	if(timeLeft == delay && delay > 0) {
            		String startMessage = plugin.getConfig().getString("config.teleport-countdown-start");
            		
            		if(startMessage == null) {
                		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error in the configuration file.");
                		this.cancel();
                		return;
                	}
            		
            		startMessage = ChatColor.translateAlternateColorCodes('&', startMessage);
            		
            		player.sendMessage(startMessage);
            	}
            	
            	timeLeft--;
            	
                if(timeLeft > 0) {
                	String message = plugin.getConfig().getString("config.teleport-countdown");
                	
                	if(message == null) {
                		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error in the configuration file.");
                		this.cancel();
                		return;
                	}
                	
                	message = message.replace("{time}", timeLeft + "");
                	message = ChatColor.translateAlternateColorCodes('&', message);
                	
                	player.sendMessage(message);
                } else if(timeLeft <= 0) {
                	String teleportMessage = plugin.getConfig().getString("config.teleport-message");
                	
                	if(teleportMessage == null) {
                		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error in the configuration file.");
                		this.cancel();
                		return;
                	}
                	
                	teleportMessage = ChatColor.translateAlternateColorCodes('&', teleportMessage);
                	
                	player.sendMessage(teleportMessage);
                	
                	player.teleport(location);
                	
                	this.cancel();
                }
            }
            
        }.runTaskTimer(plugin, 0, 20);
        
        teleportCountdowns.put(player, runnable);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		
		if(!teleportCountdowns.containsKey(player)) {
			return;
		}
		
		teleportCountdowns.get(player).cancel();
		teleportCountdowns.remove(player);
		
		String cancelMessage = plugin.getConfig().getString("config.teleport-cancel");
		
		if(cancelMessage == null) {
    		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error in the configuration file.");
    		return;
    	}
		
		cancelMessage = ChatColor.translateAlternateColorCodes('&', cancelMessage);
		
		player.sendMessage(cancelMessage);
	}
	
}
