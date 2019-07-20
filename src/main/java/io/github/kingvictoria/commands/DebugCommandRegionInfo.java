package io.github.kingvictoria.commands;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommandRegionInfo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        } // if

        Region region = NobilityRegions.regionMaster.getRegionByName(String.join(" ", args));

        if(region == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Region");
            return true;
        } // if

        commandSender.sendMessage(ChatColor.YELLOW + "==== "+region.getName()+" ====");
        commandSender.sendMessage(ChatColor.YELLOW + "Biome: " + ChatColor.BLUE + region.getBiome().name());
        commandSender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.BLUE + region.getWorld().getName());

        return true;
    } // onCommand

} // class
