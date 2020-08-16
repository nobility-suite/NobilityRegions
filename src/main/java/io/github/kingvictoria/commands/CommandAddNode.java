package io.github.kingvictoria.commands;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.nodes.NodeType;
import io.github.kingvictoria.regions.Region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAddNode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can't be used from the console");
            return true;
        }

        Player player = (Player) commandSender;
        Region region = NobilityRegions.getRegionManager().getRegion(player.getWorld(),
                player.getLocation().getBlock().getBiome());

        if (region == null) {
            commandSender.sendMessage(
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RED + "This area is not a region");
            return true;
        }

        if (args.length == 3) {
            String name = args[0];
            String temp = args[1];
            String temp2 = args[2];
            int slots = Integer.parseInt(temp);
            NodeType type = NodeType.valueOf(temp2);
            List<ItemStack> output = new ArrayList<>();
            output.add(new ItemStack(Material.STONE));
            region.makeNode(name, name, slots, type, output);
            commandSender.sendMessage(ChatColor.YELLOW + "Added Node to " + ChatColor.BLUE + "" + ChatColor.BOLD
                    + region.getName() + ChatColor.YELLOW + " region");

        } else {
            commandSender
                    .sendMessage(ChatColor.DARK_RED + "Error invalid arguments. /command <name> <slots> <NodeType>");
        }

        return true;
    }

}
