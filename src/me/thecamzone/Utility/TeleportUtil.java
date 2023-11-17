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
            		player.sendMessage(MessageUtil.getConfigString("config.teleport-countdown-start"));
            	}
            	
            	timeLeft--;
            	
                if(timeLeft > 0) {
                	String message = MessageUtil.getConfigString("config.teleport-countdown");
                	message = message.replace("{time}", timeLeft + "");
                	
                	player.sendMessage(message);
                } else if(timeLeft <= 0) {
                	player.sendMessage(MessageUtil.getConfigString("config.teleport-message"));
                	
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
		
		if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ() || e.getFrom().getBlockY() != e.getTo().getBlockY()) {
			teleportCountdowns.get(player).cancel();
			teleportCountdowns.remove(player);
			
			player.sendMessage(MessageUtil.getConfigString("config.teleport-cancel"));
		}
	}
	
}
