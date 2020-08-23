package io.github.kingvictoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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

    String pluginName;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        pluginName = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[" + NobilityRegions.getInstance().getName()
                + " " + NobilityRegions.getInstance().getDescription().getVersion() + "]";

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length == 0 || args[0].equals("help")) {
            return help(sender, args, player);
        } else if (args[0].equals("list")) {
            return list(sender, args, player);
        } else if (args[0].equals("info")) {
            return info(sender, args, player);
        } else if (args[0].equals("generate")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            return generate(sender, args, player);
        } else if (args[0].equals("set")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            if (args.length == 1) {
                return setUsage(sender);
            } else if (args[1].equals("name")) {
                return setName(sender, args, player);
            } else if (args[1].equals("habitability")) {
                return setHabitability(sender, args, player);
            } else if (args[1].equals("slots")) {
                return setSlots(sender, args, player);
            } else if (args[1].equals("type")) {
                return setType(sender, args, player);
            }

            return setUsage(sender);
        } else if (args[0].equals("add")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            if (args.length == 1) {
                return addUsage(sender);
            } else if (args[1].equals("output")) {
                return addOutput(sender, args, player);
            } else if (args[1].equals("worker")) {
                return addWorker(sender, args, player);
            } else if (args[1].equals("node")) {
                return addNode(sender, args, player);
            }

            return addUsage(sender);
        } else if (args[0].equals("remove")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
                return true;
            }

            if (args.length == 1) {
                return removeUsage(sender);
            } else if (args[1].equals("output")) {
                return removeOutput(sender, args, player);
            } else if (args[1].equals("worker")) {
                return removeWorker(sender, args, player);
            } else if (args[1].equals("node")) {
                return removeNode(sender, args, player);
            }

            return removeUsage(sender);
        }

        return false;
    }

    private boolean removeOutput(CommandSender sender, String[] args, Player player) {
        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr remove output [<output item>...] -n <node> (-r <region>)");
            return true;
        }

        int index = 2;
        List<NobilityItem> toRemove = new ArrayList<>();

        while (index < args.length && !args[index].equals("-n")) {
            NobilityItem item = null;
            try {
                item = NobilityItems.getItemByName(args[index]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + args[index] + " is not a valid NobilityItem!");
                return true;
            }

            toRemove.add(item);
            index++;
        }

        if (index >= args.length) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr remove output [<output item>...] -n <node> (-r <region>)");
            return true;
        }

        int oldIndex = index;
        String nodeName = args[index + 1];
        for (int i = index + 2; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == oldIndex) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr remove output [<output item>...] -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            List<NobilityItem> didNotRemove = new ArrayList<>();
            Map<NobilityItem, Integer> output = node.getOutput();
            for (NobilityItem item: toRemove) {
                Integer value = output.remove(item);
                if (value == null) {
                    didNotRemove.add(item);
                } else {
                    output.put(item, null);
                }
            }
            if (!didNotRemove.isEmpty()) {
                String out = ChatColor.RED + "Unable to remove ";
                for (NobilityItem item : didNotRemove) {
                    toRemove.remove(item);
                    out += ChatColor.BLUE + item.getInternalName() + ChatColor.RED + ", ";
                }
                out = out.substring(0, out.length() - 2);
                out += "!";
                sender.sendMessage(out);
            }
 
            node.setOutput(output);

            String out = ChatColor.YELLOW + "Removed ";
            for (NobilityItem item : toRemove) {
                out += ChatColor.BLUE + item.getInternalName() + ChatColor.YELLOW + ", ";
            }
            out = out.substring(0, out.length() - 2);
            out += "!";

            if (out.length() > 9) {
                sender.sendMessage(out);
            }
        }

        return true;
    }

    private boolean removeWorker(CommandSender sender, String[] args, Player player) {
        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr remove worker [<player>...] -n <node> (-r <region>)");
            return true;
        }

        List<OfflinePlayer> workersToRemove = new ArrayList<>();
        int index = 2;
        get_workers_loop:
        while (index < args.length && !args[index].equals("-n")) {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName().equals(args[index])) {
                    workersToRemove.add(offlinePlayer);
                    index++;
                    continue get_workers_loop;
                }
            }

            sender.sendMessage(ChatColor.GREEN + args[index] + ChatColor.RED + " is not a player!");
            return true;
        }

        if (index >= args.length) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr remove worker [<player>...] -n <node> (-r <region>)");
            return true;
        }

        int oldIndex = index;
        String nodeName = args[index + 1];
        for (int i = index + 2; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == oldIndex) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr remove worker [<player>...] -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            List<OfflinePlayer> workersUnableToRemove = new ArrayList<>();
            for (OfflinePlayer worker : workersToRemove) {
                if(!node.removeWorker(worker)) {
                    workersUnableToRemove.add(worker);
                }
            }

            if (!workersUnableToRemove.isEmpty()) {
                String out = ChatColor.RED + "Unable to remove ";
                for (OfflinePlayer worker : workersUnableToRemove) {
                    workersToRemove.remove(worker);
                    out += ChatColor.BLUE + worker.getName() + ChatColor.RED + ", ";
                }
                out = out.substring(0, out.length() - 2) + "! Are they workers on this node?";
                sender.sendMessage(out);
            }

            String out = ChatColor.YELLOW + "Removed ";
            for (OfflinePlayer worker : workersToRemove) {
                out += ChatColor.BLUE + worker.getName() + ChatColor.YELLOW + ", ";
            }
            out = out.substring(0, out.length() - 2) + "!";
            if (out.length() > 8) {
                sender.sendMessage(out);
            }
        }


        return true;
    }

    private boolean addWorker(CommandSender sender, String[] args, Player player) {
        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr add worker [<player>...] -n <node> (-r <region>)");
            return true;
        }

        List<OfflinePlayer> workersToAdd = new ArrayList<>();
        int index = 2;
        get_workers_loop:
        while (index < args.length && !args[index].equals("-n")) {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName().equals(args[index])) {
                    workersToAdd.add(offlinePlayer);
                    index++;
                    continue get_workers_loop;
                }
            }

            sender.sendMessage(ChatColor.GREEN + args[index] + ChatColor.RED + " is not a player!");
            return true;
        }

        if (index >= args.length) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr add worker [<player>...] -n <node> (-r <region>)");
            return true;
        }

        int oldIndex = index;
        String nodeName = args[index + 1];
        for (int i = index + 2; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == oldIndex) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr add worker [<player>...] -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            List<OfflinePlayer> workersUnableToAdd = new ArrayList<>();
            for (OfflinePlayer worker : workersToAdd) {
                if(!node.addWorker(worker)) {
                    workersUnableToAdd.add(worker);
                }
            }

            if (!workersUnableToAdd.isEmpty()) {
                String out = ChatColor.RED + "Unable to add ";
                for (OfflinePlayer worker : workersUnableToAdd) {
                    workersToAdd.remove(worker);
                    out += ChatColor.BLUE + worker.getName() + ChatColor.RED + ", ";
                }
                out = out.substring(0, out.length() - 2) + "!";
                sender.sendMessage(out);
            }

            String out = ChatColor.YELLOW + "Added ";
            for (OfflinePlayer worker : workersToAdd) {
                out += ChatColor.BLUE + worker.getName() + ChatColor.YELLOW + ", ";
            }
            out = out.substring(0, out.length() - 2) + "!";
            if (out.length() > 7) {
                sender.sendMessage(out);
            }
        }


        return true;
    }

    private boolean addOutput(CommandSender sender, String[] args, Player player) {
        if (args.length < 6) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr add output [<output item> <amount>...] -n <node> (-r <region>)");
            return true;
        }

        int index = 2;
        Map<NobilityItem, Integer> output = new HashMap<>();

        while (index + 1 < args.length && !args[index].equals("-n")) {
            NobilityItem item = null;
            int amount = -1;
            try {
                item = NobilityItems.getItemByName(args[index]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + args[index] + " is not a valid NobilityItem!");
                return true;
            }

            try {
                amount = Integer.valueOf(args[index + 1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + args[index + 1] + " is not a number!");
                return true;
            }

            output.put(item, amount);
            index += 2;
        }

        if (index >= args.length) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr add output [<output item> <amount>...] -n <node> (-r <region>)");
            return true;
        }

        int oldIndex = index;
        String nodeName = args[index + 1];
        for (int i = index + 2; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == oldIndex) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr set slots <number> -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            node.getOutput().forEach(output::putIfAbsent);
            node.setOutput(output);
            sender.sendMessage(ChatColor.YELLOW + "Outputs added to " + ChatColor.GREEN + node.getName() + ChatColor.YELLOW + "!");
        }

        return true;
    }

    private boolean setType(CommandSender sender, String[] args, Player player) {
        if (args.length < 5 || !args[3].equals("-n")) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr set type <NodeType> -n <node> (-r <region>)");
            return true;
        }

        NodeType type = null;
        try {
            type = NodeType.valueOf(args[2]);
        } catch (NullPointerException e) {
            sender.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is not a valid NodeType!");
            return true;
        }

        int index = -1;
        String nodeName = args[4];
        for (int i = 5; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == -1) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr set slots <number> -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.RED + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            node.setType(type);
            sender.sendMessage(ChatColor.YELLOW + "Type of " + ChatColor.GREEN + node.getName() + ChatColor.YELLOW
                + " set to " + ChatColor.GREEN + "" + type.name());
        }

        return true;
    }

    private boolean setSlots(CommandSender sender, String[] args, Player player) {
        if (args.length < 5 || !args[3].equals("-n")) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr set slots <number> -n <node> (-r <region>)");
            return true;
        }

        int slots;
        try {
            slots = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + args[2] + " is not a number!");
            return true;
        }

        int index = -1;
        String nodeName = args[4];
        for (int i = 5; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        if (index == -1) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr set slots <number> -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.YELLOW + " is not a valid region!");
                return true;
            }
        }


        Node node = null;
        for (Node n : region.getNodes()) {
            if (n.getName().equals(nodeName)) {
                node = n;
                break;
            }
        }

        if (node == null) {
            sender.sendMessage(ChatColor.RED + "There is no node " + ChatColor.GREEN + nodeName + ChatColor.RED 
                + " in " + ChatColor.BLUE + region.getName());
        } else {
            node.setSlots(slots);
            sender.sendMessage(ChatColor.YELLOW + "Slots in " + ChatColor.GREEN + node.getName() + ChatColor.YELLOW
                + " set to " + ChatColor.GREEN + "" + slots);
        }

        return true;
    }

    private boolean removeNode(CommandSender sender, String[] args, Player player) {
        if (args.length < 4 || !args[2].equals("-n")) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr remove node -n <node> (-r <region>)");
            return true;
        }

        String nodeName = args[3];
        int index = -1;
        for (int i = 4; i < args.length; i++) {
            if (args[i].equals("-r")) {
                index = i;
                break;
            }

            nodeName += " " + args[i];
        }

        Region region;
        
        if (index == -1) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify the region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        } else {
            if (index + 1 >= args.length) {
                sender.sendMessage(ChatColor.RED + "Usage: /nr remove node -n <node> (-r <region>)");
                return true;
            }

            String regionName = args[index + 1];
            for (int i = index + 2; i < args.length; i++) {
                regionName += " " + args[i];
            }

            region = NobilityRegions.getRegionManager().getRegionByName(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.YELLOW + " is not a valid region!");
                return true;
            }
        }

        if (!region.removeNode(nodeName)) {
            sender.sendMessage(ChatColor.GREEN + nodeName + ChatColor.RED + " is not a valid node in " 
                + ChatColor.BLUE + region.getName() + ChatColor.RED + "!");
        } else {
            sender.sendMessage(ChatColor.GREEN + nodeName + ChatColor.YELLOW + " removed from " 
                + ChatColor.BLUE + region.getName() + ChatColor.YELLOW + "!");
        }

        return true;
    }

    private boolean removeUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/nr remove output [<output item>...] -n <node> (-r <region>)");
        sender.sendMessage(ChatColor.RED + "/nr remove worker [<player>...] -n <node> (-r <region>)");
        sender.sendMessage(ChatColor.RED + "/nr remove node -n <node> (-r <region>)");
        return true;
    }

    private boolean addNode(CommandSender sender, String[] args, Player player) {
        if (args.length < 13) {
            sender.sendMessage(ChatColor.RED
                    + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
            return true;
        }

        String id = null;
        String name = null;
        int slots = -1;
        NodeType type = null;
        Map<NobilityItem, Integer> output = null;
        Region region = null;

        int index = 2;
        while (index < args.length) {
            switch (args[index]) {
                case "-i":
                    if (index + 1 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    id = args[index + 1];

                    index += 2;
                    break;

                case "-n":
                    if (index + 1 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    name = args[index + 1];
                    for (int i = index + 2; i < args.length; i++) {
                        if (args[i].charAt(0) == '-') {
                            index = i;
                            break;
                        }
    
                        name += " " + args[i];

                        if (i + 1 >= args.length) {
                            index = i + 1;
                        }
                    }

                    break;

                case "-s":
                    if (index + 1 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    try {
                        slots = Integer.parseInt(args[index + 1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + args[index + 1] + " is not a number!");
                        return true;
                    }

                    index += 2;
                    break;

                case "-t":
                    if (index + 1 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    try {
                        type = NodeType.valueOf(args[index + 1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + args[index + 1] + " is not a valid NodeType!");
                        return true;
                    }

                    index += 2;
                    break;

                case "-o":
                    if (index + 2 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    if (output == null) {
                        output = new HashMap<>();
                    }

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
                    break;

                case "-r":
                    if (index + 1 >= args.length) {
                        sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                        return true;
                    }

                    String regionName = args[index + 1];
                    for (int i = index + 2; i < args.length; i++) {
                        if (args[i].charAt(0) == '-') {
                            index = i;
                            break;
                        }
    
                        regionName += " " + args[i];

                        if (i + 1 >= args.length) {
                            index = i + 1;
                        }
                    }

                    region = NobilityRegions.getRegionManager().getRegionByName(regionName);

                    if (region == null) {
                        sender.sendMessage(ChatColor.BLUE + regionName + ChatColor.YELLOW + " is not a valid region!");
                        return true;
                    }

                    break;
                default:
                    sender.sendMessage(ChatColor.GREEN + args[index] + ChatColor.YELLOW + " is not a valid argument!");
                    sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
                    return true;
            }
        }

        if (id == null || name == null || slots == -1 || type == null || output == null) {
            sender.sendMessage(ChatColor.RED + "Usage: /nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
            return true;
        }

        if (region == null) {
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "You must specify region from the console!");
                return true;
            }

            region = NobilityRegions.getRegionManager().getRegionByLocation(player.getLocation());

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a region!");
                return true;
            }
        }
        
        region.makeNode(id, name, slots, type, output);
        sender.sendMessage(ChatColor.YELLOW + "Node " + ChatColor.GREEN + name + ChatColor.YELLOW
                + " created in " + ChatColor.BLUE + region.getName());

        return true;
    }

    private boolean addUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/nr add output [<output item> <amount>...] -n <node> (-r <region>)");
        sender.sendMessage(ChatColor.RED + "/nr add worker [<player>...] -n <node> (-r <region>)");
        sender.sendMessage(ChatColor.RED + "/nr add node -i <id> -n <name> -s <slots> -t <type> [-o <output item> <amount>...] (-r <region>)");
        return true;
    }

    private boolean setHabitability(CommandSender sender, String[] args, Player player) {
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

    private boolean setName(CommandSender sender, String[] args, Player player) {
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

            if (!region.setName(newName)) {
                sender.sendMessage(
                        ChatColor.BLUE + region.getName() + ChatColor.RED + " could not have its name set to "
                                + ChatColor.BLUE + newName + ChatColor.RED + "! Is it already used?");
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

            if (!region.setName(newName)) {
                sender.sendMessage(
                        ChatColor.BLUE + region.getName() + ChatColor.RED + " could not have its name set to "
                                + ChatColor.BLUE + newName + ChatColor.RED + "! Is it already used?");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Region name set to " + ChatColor.BLUE + newName);
            }

            return true;
        }
    }

    private boolean setUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/nr set name (-r <region name> -n) <new name>");
        sender.sendMessage(ChatColor.RED + "/nr set habitability <true|false> (region name)");
        sender.sendMessage(ChatColor.RED + "/nr set slots <number> -n <node> (-r <region>)");
        sender.sendMessage(ChatColor.RED + "/nr set type <NodeType> -n <node> (-r <region>)");
        return true;
    }

    private boolean help(CommandSender sender, String[] args, Player player) {
        sender.sendMessage(ChatColor.GREEN + "=== " + pluginName + ChatColor.GREEN + " ===");

        sender.sendMessage(ChatColor.GREEN + "/nr (help)");
        sender.sendMessage(ChatColor.YELLOW + "NobilityRegions help");

        sender.sendMessage(ChatColor.GREEN + "/nr list (search term)");
        sender.sendMessage(ChatColor.YELLOW + "Lists all regions, optional search");

        sender.sendMessage(ChatColor.GREEN + "/nr info (region name)");
        sender.sendMessage(ChatColor.YELLOW + "Gets info on a region, defaults to the region you're in");

        if (sender.isOp()) {
            sender.sendMessage(ChatColor.GREEN + "--- " + ChatColor.DARK_GREEN + ""
                + ChatColor.BOLD + "Admin Commands" + ChatColor.GREEN + " ---");

            sender.sendMessage(ChatColor.GREEN + "/nr generate (world name)"); 
            sender.sendMessage(ChatColor.YELLOW + "Generates regions for a world, defaults to the world you're in");

            sender.sendMessage(ChatColor.GREEN + "/nr set <name | habitability | slots | type> <args..>"); 
            sender.sendMessage(ChatColor.YELLOW + "Sets region/node data");

            sender.sendMessage(ChatColor.GREEN + "/nr add <output | worker | node> <args..>"); 
            sender.sendMessage(ChatColor.YELLOW + "Adds region/node data");

            sender.sendMessage(ChatColor.GREEN + "/nr remove <output | worker | node> <args..>"); 
            sender.sendMessage(ChatColor.YELLOW + "Removes region/node data");
        }

        return true;
    }

    private boolean list(CommandSender sender, String[] args, Player player) {
        if (args.length == 1) {
            sender.sendMessage(ChatColor.GREEN + "=== Regions ===");

            for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                TextComponent component = new TextComponent(ChatColor.BLUE + region.getName());
                component.setClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nr info " + region.getName()));
                sender.spigot().sendMessage(component);
            }
        } else {
            String search = args[1];
            for (int i = 2; i < args.length; i++) {
                search += " " + args[i];
            }

            sender.sendMessage(ChatColor.GREEN + "=== Regions Matching: '" + ChatColor.GREEN + search
                    + ChatColor.GREEN + "' ===");

            for (Region region : NobilityRegions.getRegionManager().getRegions()) {
                if (region.getName().toLowerCase().contains(search.toLowerCase())) {
                    TextComponent component = new TextComponent(ChatColor.BLUE + region.getName());
                    component.setClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nr info " + region.getName()));
                    sender.spigot().sendMessage(component);
                }
            }
        }

        return true;
    }

    private boolean info(CommandSender sender, String[] args, Player player) {
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

                    TextComponent component = new TextComponent(
                            ChatColor.BLUE + " - " + node.getOutput().get(item) + "x ");
                    TextComponent displayName = new TextComponent(item.getDisplayName());
                    if (sender.isOp()) {
                        displayName.setClickEvent(
                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni get " + item.getInternalName()));
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
    }

    private static boolean generate(CommandSender sender, String[] args, Player player) {
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
    }
}