package io.github.kingvictoria.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.kingvictoria.NobilityRegions;

public class CommandGenerateRegions implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        }

        if (args.length == 0 && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must specify the world to use this command from the console");
            return true;
        } else if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /generateregions (world)");
            return true;
        }

        World world;

        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else {
            world = Bukkit.getWorld(args[0]);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + args[0] + " is not a valid world!");
                return true;
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "Generating Regions...");
        NobilityRegions.getRegionManager().generateRegions(world);
        sender.sendMessage(ChatColor.YELLOW + "Regions Generated!");

        return true;
    }
    
}