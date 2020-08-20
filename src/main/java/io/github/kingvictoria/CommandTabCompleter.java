package io.github.kingvictoria;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.NodeType;
import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;

public class CommandTabCompleter implements TabCompleter {

    private static final List<String> COMMANDS = new ArrayList<>();
    private static final List<String> ADMIN_COMMANDS = new ArrayList<>();
    private static final List<String> SET_COMMANDS = new ArrayList<>();
    private static final List<String> ADD_COMMANDS = new ArrayList<>();
    private static final List<String> BOOLEANS = new ArrayList<>();

    static {
        COMMANDS.add("list");
        COMMANDS.add("info");

        
        ADMIN_COMMANDS.add("list");
        ADMIN_COMMANDS.add("info");
        ADMIN_COMMANDS.add("generate");
        ADMIN_COMMANDS.add("set");
        ADMIN_COMMANDS.add("add");

        SET_COMMANDS.add("name");
        SET_COMMANDS.add("habitability");

        ADD_COMMANDS.add("node");

        BOOLEANS.add("true");
        BOOLEANS.add("false");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabs = new ArrayList<>();

        if (args.length == 1) {
            if (sender.isOp()) {
                StringUtil.copyPartialMatches(args[0], ADMIN_COMMANDS, tabs);
            } else {
                StringUtil.copyPartialMatches(args[0], COMMANDS, tabs);
            }
        } else if (args.length == 2) {
            if (args[0].equals("info")) {
                List<String> regionNames = new ArrayList<>();
                for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                    regionNames.add(region.getName());
                }
    
                StringUtil.copyPartialMatches(args[1], regionNames, tabs);
            } else if (args[0].equals("generate") && sender.isOp()) {
                List<String> worldNames = new ArrayList<>();
                for (World world : Bukkit.getWorlds()) {
                    worldNames.add(world.getName());
                }

                StringUtil.copyPartialMatches(args[1], worldNames, tabs);
            } else if (args[0].equals("set") && sender.isOp()) {
                StringUtil.copyPartialMatches(args[1], SET_COMMANDS, tabs);
            } else if (args[0].equals("add") && sender.isOp()) {
                StringUtil.copyPartialMatches(args[1], ADD_COMMANDS, tabs);
            }
        } else if (args.length == 3) {
            if (args[0].equals("set") && args[1].equals("habitability") && sender.isOp()) {
                StringUtil.copyPartialMatches(args[2], BOOLEANS, tabs);
            }
        } else {
            if (args[args.length - 2].equals("-r") || (args[1].equals("habitability") && args.length == 4)) {
                List<String> regionNames = new ArrayList<>();
                for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                    regionNames.add(region.getName());
                }
    
                StringUtil.copyPartialMatches(args[args.length - 1], regionNames, tabs);
            } else if (args[args.length - 2].equals("-t")) {
                List<String> nodeTypeStrings = new ArrayList<>();
                for (NodeType type : NodeType.values()) {
                    nodeTypeStrings.add(type.name());
                }

                StringUtil.copyPartialMatches(args[args.length - 1], nodeTypeStrings, tabs);
            } else if (args[args.length - 2].equals("-o")) {
                List<String> nobilityItemStrings = new ArrayList<>();
                for (NobilityItem item : NobilityItems.getItems()) {
                    nobilityItemStrings.add(item.getInternalName());
                }

                StringUtil.copyPartialMatches(args[args.length - 1], nobilityItemStrings, tabs);
            }
        }
        
        

        return tabs;
    }

}
