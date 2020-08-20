package io.github.kingvictoria;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.Node;
import io.github.kingvictoria.regions.nodes.NodeType;
import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String pluginName = ChatColor.DARK_GREEN + "" 
            + ChatColor.BOLD + "[" + NobilityRegions.getInstance().getName() 
            + " " + NobilityRegions.getInstance().getDescription().getVersion() + "]";

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "=== " + pluginName + ChatColor.GREEN + " ===");
            sender.sendMessage(ChatColor.YELLOW + "/nr " 
                + ChatColor.GREEN + "NobilityRegions help");
            sender.sendMessage(ChatColor.YELLOW + "/nr list (search term) " 
                + ChatColor.GREEN + "Lists all regions, optional search");
            sender.sendMessage(ChatColor.YELLOW + "/nr info (region name) " 
                + ChatColor.GREEN + "Gets info on a region, defaults to the region you're in");
            
            if (sender.isOp()) {
                sender.sendMessage(ChatColor.AQUA + "---" + ChatColor.DARK_AQUA + "Admin Commands" + ChatColor.AQUA + "---");
                sender.sendMessage(ChatColor.GOLD + "/nr generate (world name) " 
                    + ChatColor.DARK_GREEN + "Generates regions for a world, defaults to the world you're in");
                sender.sendMessage(ChatColor.GOLD + "/nr set name (-r <region name> -n) <new name> " 
                    + ChatColor.DARK_GREEN + "Sets the name of a region, defaults to the region you're in");
                sender.sendMessage(ChatColor.GOLD + "/nr set habitability <true|false> (region name)" 
                    + ChatColor.DARK_GREEN + "Sets the habitability of a region, defaults to the region you're in");
                sender.sendMessage(ChatColor.GOLD + "/nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>) " 
                    + ChatColor.DARK_GREEN + "Adds a node to a region, defaults to the region you're in");
            }
            
            return true;
        } else if (args[0].equals("list")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.GREEN + "=== Regions ===");

                for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                    TextComponent component = new TextComponent(ChatColor.BLUE + region.getName());
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nr info " + region.getName()));
                    sender.spigot().sendMessage(component);
                }

                return true;
            } else {
                String search = args[1];
                for (int i = 2; i < args.length; i++) {
                    search += " " + args[i];
                }

                sender.sendMessage(ChatColor.GREEN + "=== Regions Matching: '" 
                    + ChatColor.GREEN + search + ChatColor.GREEN + "' ===");
                
                for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                    if (region.getName().toLowerCase().contains(search.toLowerCase())) {
                        TextComponent component = new TextComponent(ChatColor.BLUE + region.getName());
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nr info " + region.getName()));
                        sender.spigot().sendMessage(component);
                    }
                }

                return true;
            }
        } else if (args[0].equals("info")) {
            Region region;

            if (args.length == 0 && player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            } else if (args.length == 1) {
                region = NobilityRegions.getRegionManager().getRegionByLocation(((Player) sender).getLocation());

                if (region == null) {
                    sender.sendMessage(ChatColor.GREEN + "This area is not a region!");
                    return true;
                }
            } else {
                String regionName = args[1];
                for (int i = 2; i < args.length; i++) {
                    regionName += " " + args[i];
                }

                region = NobilityRegions.getRegionManager().getRegionByName(regionName);

                if (region == null) {
                    sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a region!");
                    return true;
                }
            }

            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "=== " + region.getName() + " ===");
            sender.sendMessage(ChatColor.YELLOW + "Biome: " + ChatColor.BLUE + region.getBiome().name());
            sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.BLUE + region.getWorld().getName());

            if (region.isHabitable())
                sender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Habitable");
            else
                sender.sendMessage(ChatColor.YELLOW + "Habitability: " + ChatColor.BLUE + "Wilderness");

            if (region.getNodes().size() > 0) {
                sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "-- Nodes --");
                for (Node node : region.getNodes()) {
                    sender.sendMessage(ChatColor.YELLOW + node.getName() + ":");
                    sender.sendMessage(ChatColor.YELLOW + " Slots: " + ChatColor.BLUE + "" + node.getSlots());
                    sender.sendMessage(ChatColor.YELLOW + " Type:  " + ChatColor.BLUE + node.getType().toString());
                    sender.sendMessage(ChatColor.YELLOW + " Output: ");
                    for (NobilityItem item : node.getOutput().keySet()) {
                        if (item == null) {
                            sender.sendMessage(ChatColor.RED + " - ERROR!!");
                            continue;
                        }

                        TextComponent component = new TextComponent(ChatColor.BLUE + " - " + node.getOutput().get(item) + "x ");
                        TextComponent displayName = new TextComponent(item.getDisplayName());
                        if (sender.isOp()) {
                            displayName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni get " + item.getInternalName()));
                        }
                        if (item.hasLore()) {
                            BaseComponent[] loreText = new BaseComponent[item.getLore().size()];
                            for (int i = 0; i < item.getLore().size(); i++) {
                                if (i == item.getLore().size() - 1) {
                                    loreText[i] = new TextComponent(item.getLore().get(i));
                                } else {
                                    loreText[i] = new TextComponent(item.getLore().get(i) + "\n");
                                }
                            }
                            
                            displayName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, loreText));
                        }
                        component.addExtra(displayName);

                        sender.spigot().sendMessage(component);
                    }

                    if (node.getWorkers().size() > 0) {
                        sender.sendMessage(ChatColor.YELLOW + " Workers: ");
                        for (UUID worker : node.getWorkers()) {
                            Player playerW = Bukkit.getPlayer(worker);
                            sender.sendMessage(ChatColor.BLUE + " - " + playerW.getName());
                        }
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + " Workers: " + ChatColor.BLUE + "None");
                    }
                }
            }

            return true;
        } else if (args[0].equals("generate")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }
    
            if (args.length == 1 && player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the world to use this command from the console");
                return true;
            }
    
            World world;
    
            if (player != null && args.length == 1) {
                world = player.getWorld();
            } else {
                String worldName = args[1];
                for (int i = 2; i < args.length; i++) {
                    worldName += " " + args[i];
                }

                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    sender.sendMessage(ChatColor.RED + worldName + " is not a valid world!");
                    return true;
                }
            }
    
            sender.sendMessage(ChatColor.YELLOW + "Generating Regions...");
            NobilityRegions.getRegionManager().generateRegions(world);
            sender.sendMessage(ChatColor.YELLOW + "Regions Generated!");
    
            return true;
        } else if (args[0].equals("set")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Usage:");
                sender.sendMessage(ChatColor.RED + "/nr set name (-r <region name> -n) <new name>");
                sender.sendMessage(ChatColor.RED + "/nr set habitability <true|false> (region name)");
                return true;
            }

            if (args[1].equals("name")) {
                if (args.length == 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /nr set name (-r <region name> -n) <new name>");
                    return true;
                }

                if (args[2].equals("-r")) {
                    if (args.length < 5) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr set name -r <region name> -n <new name>");
                        return true;
                    }

                    int newNameIndex = -1;
                    String regionName = args[3];
                    for (int i = 4; i < args.length; i++) {
                        if (args[i].equals("-n")) {
                            newNameIndex = i + 1;
                            break;
                        }

                        regionName += " " + args[i];
                    }

                    if (newNameIndex == -1 || args.length < newNameIndex + 1) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr set name -r <region name> -n <new name>");
                        return true;
                    }

                    Region region = NobilityRegions.getRegionManager().getRegionByName(regionName);

                    if (region == null) {
                        sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid Region!");
                        return true;
                    }

                    String newName = args[newNameIndex];
                    for (int i = newNameIndex + 1; i < args.length; i++) {
                        newName += " " + args[i];
                    }

                    if(!region.setName(newName)) {
                        sender.sendMessage(ChatColor.BLUE + region.getName() 
                            + ChatColor.RED + " could not have its name set to " + ChatColor.BLUE + newName 
                            + ChatColor.RED + "! Is it already used?");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Region name set to " + ChatColor.BLUE + newName);
                    }

                    return true;
                } else {
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "You must specify the region in the console!");
                        sender.sendMessage(ChatColor.RED + "Usage: /nr set name -r <region name> -n <new name>");
                        return true;
                    }

                    Region region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());
                    if (region == null) {
                        sender.sendMessage(ChatColor.RED + "You are not in a region!");
                        return true;
                    }

                    String newName = args[2];
                    for (int i = 3; i < args.length; i++) {
                        newName += " " + args[i];
                    }

                    if(!region.setName(newName)) {
                        sender.sendMessage(ChatColor.BLUE + region.getName() 
                            + ChatColor.RED + " could not have its name set to " + ChatColor.BLUE + newName 
                            + ChatColor.RED + "! Is it already used?");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Region name set to " + ChatColor.BLUE + newName);
                    }

                    return true;
                }


            } else if (args[1].equals("habitability")) {
                if (args.length == 2 || (!args[2].equals("true") && !args[2].equals("false"))) {
                    sender.sendMessage(ChatColor.RED + "Usage: /nr set habitability <true|false> (region name)");
                    return true;
                }

                if (args.length == 3) {
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "You must specify the region in the console!");
                        sender.sendMessage(ChatColor.RED + "Usage: /nr set habitability <true|false> <region name>");
                        return true;
                    }

                    Region region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

                    if (region == null) {
                        sender.sendMessage(ChatColor.RED + "You are not in a region!");
                        return true;
                    }

                    region.setHabitable(args[2].equals("true"));
                    sender.sendMessage(ChatColor.YELLOW + "Habitability of " + ChatColor.BLUE + region.getName()
                        + ChatColor.YELLOW + " set to " + ChatColor.GREEN + "" + args[2].equals("true"));
                    return true;
                } else {
                    boolean habitability = args[2].equals("true");

                    String regionName = args[3];
                    for (int i = 4; i < args.length; i++) {
                        regionName += " " + args[i];
                    }

                    Region region = NobilityRegions.getRegionManager().getRegionByName(regionName);

                    if (region == null) {
                        sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid Region!");
                        return true;
                    }

                    region.setHabitable(habitability);
                    sender.sendMessage(ChatColor.YELLOW + "Habitability of " + ChatColor.BLUE + region.getName()
                        + ChatColor.YELLOW + " set to " + ChatColor.GREEN + "" + habitability);
                    return true;
                }
            }

            sender.sendMessage(ChatColor.RED + "Usage:");
            sender.sendMessage(ChatColor.RED + "/nr set name (-r <region name> -n) <new name>");
            sender.sendMessage(ChatColor.RED + "/nr set habitability <true|false> (region name)");
            return true;
        } else if (args[0].equals("add")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Usage:");
                sender.sendMessage(ChatColor.RED + "/nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                return true;
            }

            if (args[1].equals("node")) {
                if (args.length < 13 || !args[2].equals("-i")) {
                    sender.sendMessage(ChatColor.RED 
                        + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                    return true;
                }

                String id = args[3];

                if (!args[4].equals("-n")) {
                    sender.sendMessage(ChatColor.RED 
                        + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                    return true;
                }

                String name = args[5];
                int index = -1;
                for (int i = 6; i < args.length; i++) {
                    if (args[i].equals("-s")) {
                        index = i + 1;
                        break;
                    }

                    name += " " + args[i];
                }

                if (index == -1 || args.length < index + 6) {
                    sender.sendMessage(ChatColor.RED 
                        + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                    return true;
                }

                int slots = -1;

                try {
                    slots = Integer.parseInt(args[index]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + args[index] + " is not a number!"); 
                    return true;
                }

                if (!args[index + 1].equals("-t")) {
                    sender.sendMessage(ChatColor.RED 
                        + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                    return true;
                }

                index += 2;

                NodeType type = null;

                try {
                    type = NodeType.valueOf(args[index]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + args[index] + " is not a valid NodeType!"); 
                    return true;
                }

                index += 1;
                Map<NobilityItem, Integer> output = new HashMap<>();

                while (index + 2 < args.length && !args[index].equals("-r")) {
                    NobilityItem item = null;
                    int amount = -1;
                    try {
                        item = NobilityItems.getItemByName(args[index + 1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + args[index + 1] + " is not a valid NobilityItem!"); 
                        return true;
                    }

                    try {
                        amount = Integer.valueOf(args[index + 2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + args[index + 2] + " is not a number!"); 
                        return true;
                    }

                    output.put(item, amount);
                    index += 3;
                }

                if (index < args.length && !args[index].equals("-r")) {
                    sender.sendMessage(ChatColor.RED 
                        + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
                    return true;
                }

                Region region;
                if (index >= args.length) {
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "You must specify region from the console!");
                        return true;
                    }

                    region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

                    if (region == null) {
                        sender.sendMessage(ChatColor.RED + "You are not in a region!");
                        return true;
                    }

                    
                } else {
                    index++;
                    String regionName = args[index];
                    for (int i = index + 1; i < args.length; i++) {
                        regionName += " " + args[i];
                    }

                    region = NobilityRegions.getRegionManager().getRegionByName(regionName);

                    if (region == null) {
                        sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.YELLOW + " is not a valid region!");
                        return true;
                    }
                }
                
                region.makeNode(id, name, slots, type, output);
                sender.sendMessage(ChatColor.YELLOW + "Node " + ChatColor.GREEN + name + ChatColor.YELLOW
                    + " created in " + ChatColor.BLUE + region.getName());

                return true;
            }

            sender.sendMessage(ChatColor.RED + "Usage:");
            sender.sendMessage(ChatColor.RED + "/nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)"); 
            return true;
        }

        return false;
    }
}