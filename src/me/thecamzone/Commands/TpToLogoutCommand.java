package me.thecamzone.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.thecamzone.TpToLogout;
import me.thecamzone.Files.DataFile;
import me.thecamzone.Utility.TeleportUtil;
import net.md_5.bungee.api.ChatColor;

public class TpToLogoutCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("tptologout")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to run this command.");
				return true;
			}
			
			Player player = (Player) sender;
			
			if(args.length == 0) {
				String locationString = DataFile.get().getString("Locations." + player.getUniqueId());
				
				if(locationString == null) {
					sender.sendMessage(ChatColor.RED + "You do not have a saved logout location.");
					return true; 
				}
				
				String[] locationInfo = locationString.split(", ");
				
				World world = Bukkit.getWorld(locationInfo[0]);
				Double playerX = Double.valueOf(locationInfo[1]);
				Double playerY = Double.valueOf(locationInfo[2]);
				Double playerZ = Double.valueOf(locationInfo[3]);
				Float playerYaw = Float.valueOf(locationInfo[4]);
				Float playerPitch = Float.valueOf(locationInfo[5]);
				
				Location location = new Location(world, playerX, playerY, playerZ, playerYaw, playerPitch);
				
				if(player.hasPermission("tptologout.bypass")) {
					TeleportUtil.teleport(player, location, 0);
				} else {
					TeleportUtil.teleport(player, location, TpToLogout.getInstance().getConfig().getInt("teleport-delay-seconds"));
				}
				
			} else if (args.length == 1) {
				
			}
		}
		
		return true;
	}

}
