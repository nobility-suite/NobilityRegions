package io.github.kingvictoria;

import org.bukkit.ChatColor;
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
            player.sendMessage(ChatColor.YELLOW + "You have entered " + ChatColor.BLUE + "" + ChatColor.BOLD + to.getName());

            if(from.isHabitable() != to.isHabitable()) {
                if(to.isHabitable()) {
                    player.sendMessage(ChatColor.GREEN + "You have left the wilderness");
                } else {
                    player.sendMessage(ChatColor.GOLD + "You have entered the wilderness");
                } // if/else
            } // if
        } // if
    } // onPlayerMove

} // class
