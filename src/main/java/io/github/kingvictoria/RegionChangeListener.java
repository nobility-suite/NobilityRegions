package io.github.kingvictoria;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionChangeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled()) return;

        Player player = event.getPlayer();
        Region from = NobilityRegions.getRegionMaster().getRegionByLocation(event.getFrom());
        Region to = NobilityRegions.getRegionMaster().getRegionByLocation(event.getTo());

        if(!from.equals(to)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("You have entered ").color(ChatColor.YELLOW).append(to.getName()).color(ChatColor.GREEN).bold(true).create());

            if(from.isHabitable() != to.isHabitable()) {
                if(to.isHabitable()) {
                    player.sendTitle("",ChatColor.GREEN + "You have left the wilderness", 10, 70, 20);
                } else {
                    player.sendTitle("",ChatColor.GOLD + "You have entered the wilderness", 10, 70, 20);
                } // if/else
            } // if
        } // if
    } // onPlayerMove

} // class
