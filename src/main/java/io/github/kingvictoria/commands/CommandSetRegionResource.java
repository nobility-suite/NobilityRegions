package io.github.kingvictoria.commands;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class CommandSetRegionResource implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "Usage of this command is restricted");
            return true;
        } // if

        if(!(commandSender instanceof Player) && args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "This command can't be used from the console without specifying a region");
            return true;
        } // if

        if(!(commandSender instanceof Conversable)) {
            commandSender.sendMessage(ChatColor.RED + "This command requires conversation");
            return true;
        } // if

        Region region;

        if(args.length == 0) {
            Player player = (Player) commandSender;
            region = NobilityRegions.getRegionMaster().getRegion(player.getWorld(), player.getLocation().getBlock().getBiome());

            if(region == null) {
                commandSender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RED + "This area is not a region");
                return true;
            } // if
        } else {
            region = NobilityRegions.getRegionMaster().getRegionByName(String.join(" ", args));

            if(region == null) {
                commandSender.sendMessage(ChatColor.RED + "Invalid Region");
                return true;
            } // if
        } // if/else

        ConversationFactory factory = new ConversationFactory(NobilityRegions.getInstance());

        Conversation conv = factory
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext conversationContext) {
                        return "Enter resource name:";
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext conversationContext, String s) {

                        return new NumericPrompt() {
                            @Override
                            protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
                                region.setResource(s, number.doubleValue());
                                conversationContext.getForWhom().sendRawMessage(ChatColor.BLUE
                                        + s + ChatColor.YELLOW + " resource set to " + ChatColor.BLUE + number.doubleValue());

                                return null;
                            }

                            @Override
                            public String getPromptText(ConversationContext conversationContext) {
                                return "Enter value of resource as a double:";
                            }
                        };
                    }
                })
                .withPrefix(arg0 -> ChatColor.BLUE + "" + ChatColor.BOLD + "[" + NobilityRegions.getInstance().getName() + "]" + ChatColor.YELLOW + " ")
                .withLocalEcho(false)
                .buildConversation((Conversable) commandSender);
        conv.begin();

        return true;
    } // onCommand

}
