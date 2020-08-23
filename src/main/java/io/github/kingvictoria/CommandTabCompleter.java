package io.github.kingvictoria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
        COMMANDS.add("help");

        
        ADMIN_COMMANDS.add("list");
        ADMIN_COMMANDS.add("info");
        ADMIN_COMMANDS.add("generate");
        ADMIN_COMMANDS.add("set");
        ADMIN_COMMANDS.add("add");
        ADMIN_COMMANDS.add("remove");

        SET_COMMANDS.add("name");
        SET_COMMANDS.add("habitability");
        SET_COMMANDS.add("slots");
        SET_COMMANDS.add("type");

        ADD_COMMANDS.add("output");
        ADD_COMMANDS.add("worker");
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

            return tabs;
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
            } else if ((args[0].equals("add") || args[0].equals("remove")) && sender.isOp()) {
                StringUtil.copyPartialMatches(args[1], ADD_COMMANDS, tabs);
            }

            return tabs;
        } else if (args.length == 3) {
            if (args[0].equals("set") && sender.isOp()) {
                if (args[1].equals("habitability")) {
                    StringUtil.copyPartialMatches(args[2], BOOLEANS, tabs);
                    return tabs;
                } else if (args[1].equals("type")) {
                    List<String> nodeTypeStrings = new ArrayList<>();
                    for (NodeType type : NodeType.values()) {
                        nodeTypeStrings.add(type.name());
                    }

                    StringUtil.copyPartialMatches(args[args.length - 1], nodeTypeStrings, tabs);
                    return tabs;
                }
            } else if ((args[0].equals("add") || args[0].equals("remove")) && sender.isOp()) {
                if (args[1].equals("output")) {
                    List<String> nobilityItemStrings = new ArrayList<>();
                    for (NobilityItem item : NobilityItems.getItems()) {
                        nobilityItemStrings.add(item.getInternalName());
                    }

                    StringUtil.copyPartialMatches(args[args.length - 1], nobilityItemStrings, tabs);
                } else if (args[1].equals("worker")) {
                    List<String> names = new ArrayList<>();
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        names.add(player.getName());
                    }

                    StringUtil.copyPartialMatches(args[args.length - 1], names, tabs);
                    return tabs;
                }
            }
        } else if (args.length > 3) {
            if (args[0].equals("add") && sender.isOp()) {
                List<String> argsList = Arrays.asList(args);
                if (!argsList.contains("-n") && !argsList.contains("-r")) {
                    if (args[1].equals("output")) {
                        try {
                            Integer.parseInt(args[args.length - 1]);
                            List<String> nobilityItemStrings = new ArrayList<>();
                            for (NobilityItem item : NobilityItems.getItems()) {
                                nobilityItemStrings.add(item.getInternalName());
                            }
    
                            StringUtil.copyPartialMatches(args[args.length - 1], nobilityItemStrings, tabs);

                            return tabs;
                        } catch (NumberFormatException e) {
                            // DO NOTHING (INTENDED)
                        }
                    } else if (args[1].equals("worker")) {
                        List<String> names = new ArrayList<>();
                        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                            names.add(player.getName());
                        }
    
                        StringUtil.copyPartialMatches(args[args.length - 1], names, tabs);
                        return tabs;
                    }
                }
            } else if (args[0].equals("remove") && sender.isOp()) {
                List<String> argsList = Arrays.asList(args);
                if (!argsList.contains("-n") && !argsList.contains("-r")) {
                    if (args[1].equals("output")) {
                        List<String> nobilityItemStrings = new ArrayList<>();
                        for (NobilityItem item : NobilityItems.getItems()) {
                            nobilityItemStrings.add(item.getInternalName());
                        }

                        StringUtil.copyPartialMatches(args[args.length - 1], nobilityItemStrings, tabs);

                        return tabs;
                    } else if (args[1].equals("worker")) {
                        List<String> names = new ArrayList<>();
                        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                            names.add(player.getName());
                        }
    
                        StringUtil.copyPartialMatches(args[args.length - 1], names, tabs);
                        return tabs;
                    }
                }
            }
        }

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
        
        

        return tabs;
    }

}
