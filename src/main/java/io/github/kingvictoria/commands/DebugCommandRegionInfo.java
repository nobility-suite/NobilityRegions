package io.github.kingvictoria.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;

public class DebugCommandRegionInfo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        } // if

        Region region;

        if (args.length == 0 && commandSender instanceof Player) {
            region = NobilityRegions.getRegionManager().getRegionByLocation(((Player) commandSender).getLocation());
        } else {
            region = NobilityRegions.getRegionManager().getRegionByName(String.join(" ", args));
        }

        if (region == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Region");
            return true;
        } // if

        commandSender.sendMessage(ChatColor.YELLOW + "==== " + region.getName() + " ====");
        commandSender.sendMessage(ChatColor.YELLOW + "Biome: " + ChatColor.BLUE + region.getBiome().name());
        commandSender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.BLUE + region.getWorld().getName());

        if (region.isHabitable())
            commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Habitable");
        else
            commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Wilderness");

        // TODO: Display Nodes in this Region

        return true;
    } // onCommand

} // class
