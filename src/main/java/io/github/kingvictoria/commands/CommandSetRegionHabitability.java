package io.github.kingvictoria.commands;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSetRegionHabitability implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        } // if

        if (args.length == 0) {
            commandSender.sendMessage(
                    ChatColor.RED + "You must specify what you want to set the habitability to (true or false)");
            return true;
        } // if

        if (!(commandSender instanceof Player) && args.length == 1) {
            commandSender.sendMessage(
                    ChatColor.RED + "This command can't be used from the console without specifying a region");
            return true;
        } // if

        if (!(commandSender instanceof Conversable)) {
            commandSender.sendMessage(ChatColor.RED + "This command requires conversation");
            return true;
        } // if

        Region region;
        boolean habitability;
        if (args[args.length - 1].equalsIgnoreCase("true")) {
            habitability = true;
        } else if (args[args.length - 1].equalsIgnoreCase("false")) {
            habitability = false;
        } else {
            commandSender.sendMessage(
                    ChatColor.RED + "You must specify what you want to set the habitability to (true or false)");
            return true;
        } // if else/if else

        args = Arrays.copyOf(args, args.length - 1);

        if (args.length == 0) {
            Player player = (Player) commandSender;
            region = NobilityRegions.getRegionManager().getRegion(player.getWorld(),
                    player.getLocation().getBlock().getBiome());

            if (region == null) {
                commandSender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RED
                        + "This area is not a region");
                return true;
            } // if
        } else {
            region = NobilityRegions.getRegionManager().getRegionByName(String.join(" ", args));

            if (region == null) {
                commandSender.sendMessage(ChatColor.RED + "Invalid Region");
                return true;
            } // if
        } // if/else

        region.setHabitable(habitability);

        commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + region.getName() + ChatColor.YELLOW
                + "'s habitability is set to " + habitability);

        return true;
    } // onCommand

} // class
