package io.github.kingvictoria.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.Node;
import net.civex4.nobilityitems.NobilityItem;

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

        commandSender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "==== " + region.getName() + " ====");
        commandSender.sendMessage(ChatColor.YELLOW + "Biome: " + ChatColor.BLUE + region.getBiome().name());
        commandSender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.BLUE + region.getWorld().getName());

        if (region.isHabitable())
            commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Habitable");
        else
            commandSender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Wilderness");

        if (region.getNodes().size() > 0) {
            commandSender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "-- Nodes --");
            for (Node node : region.getNodes()) {
                commandSender.sendMessage(ChatColor.YELLOW + node.getName() + ":");
                commandSender.sendMessage(ChatColor.YELLOW + " Slots: " + ChatColor.BLUE + "" + node.getSlots());
                commandSender.sendMessage(ChatColor.YELLOW + " Type:  " + ChatColor.BLUE + node.getType().toString());
                commandSender.sendMessage(ChatColor.YELLOW + " Output: ");
                for (NobilityItem item : node.getOutput()) {
                    if (item == null) {
                        commandSender.sendMessage(ChatColor.RED + " - ERROR!!");
                        continue;
                    }

                    commandSender.sendMessage(ChatColor.BLUE + " - " + item.getInternalName());
                }

                if (node.getWorkers().size() > 0) {
                    commandSender.sendMessage(ChatColor.YELLOW + " Workers: ");
                    for (UUID worker : node.getWorkers()) {
                        Player player = Bukkit.getPlayer(worker);
                        commandSender.sendMessage(ChatColor.BLUE + " - " + player.getName());
                    }
                } else {
                    commandSender.sendMessage(ChatColor.YELLOW + " Workers: " + ChatColor.BLUE + "None");
                }
            }
        }

        return true;
    }
}
