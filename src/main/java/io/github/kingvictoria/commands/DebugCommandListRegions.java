package io.github.kingvictoria.commands;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommandListRegions implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        }

        commandSender.sendMessage(ChatColor.YELLOW + "==== Regions ====");

        for (Region region : NobilityRegions.getRegionManager().getRegions()) {
            if (args.length > 0 && !args[0].equalsIgnoreCase(region.getWorld().getName()))
                continue;

            commandSender.sendMessage(ChatColor.BLUE + region.getName());
        }

        return true;
    }
}
