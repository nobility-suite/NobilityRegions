package net.civex4.nobilityregions.commands;

import net.civex4.nobilityregions.NobilityRegions;
import net.civex4.nobilityregions.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommandSearchRegions implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    if (!commandSender.isOp()) {
      commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
      return true;
    }

    String search = String.join(" ", args);

    commandSender.sendMessage(ChatColor.YELLOW + "==== Regions Matching '" + search + "' ====");

    for (Region region : NobilityRegions.getRegionMaster().getRegions()) {
      if (!region.getName().matches("(?i:.*" + search + ".*)")) {
        continue;
      }

      commandSender.sendMessage(ChatColor.BLUE + region.getName());
    }

    return true;
  }

}
