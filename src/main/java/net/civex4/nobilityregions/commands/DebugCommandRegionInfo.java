package net.civex4.nobilityregions.commands;

import net.civex4.nobilityregions.NobilityRegions;
import net.civex4.nobilityregions.region.Region;
import net.civex4.nobilityregions.region.RegionResource;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommandRegionInfo implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    if (!commandSender.isOp()) {
      commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
      return true;
    }

    Region region;

    if (args.length == 0 && commandSender instanceof Player) {
      region = NobilityRegions.getRegionMaster().getRegionByLocation(((Player) commandSender).getLocation());
    } else {
      region = NobilityRegions.getRegionMaster().getRegionByName(String.join(" ", args));
    }

    if (region == null) {
      commandSender.sendMessage(ChatColor.RED + "Invalid Region");
      return true;
    }

    commandSender.sendMessage(ChatColor.YELLOW + "==== " + region.getName() + " ====");
    commandSender.sendMessage(ChatColor.YELLOW + "Biome: " + ChatColor.BLUE + region.getBiome().name());
    commandSender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.BLUE + region.getWorld().getName());

    if (region.isHabitable()) {
      commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Habitable");
    } else {
      commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Wilderness");
    }

    commandSender.sendMessage(ChatColor.YELLOW + "-- Resources --");
    for (RegionResource resource : region.getResources().keySet()) {
      commandSender.sendMessage(ChatColor.YELLOW + resource.toString() + ": " + ChatColor.BLUE + region.getResource(resource));
    }

    return true;
  }
}
