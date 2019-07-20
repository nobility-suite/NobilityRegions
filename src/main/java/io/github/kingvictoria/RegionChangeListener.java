package io.github.kingvictoria;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionChangeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled()) return;

        Player player = event.getPlayer();
        Biome fromBiome = event.getFrom().getBlock().getBiome();
        World fromWorld = event.getFrom().getWorld();
        Biome toBiome = event.getTo().getBlock().getBiome();
        World toWorld = event.getTo().getWorld();

        if(fromBiome != toBiome || !fromWorld.equals(toWorld)) {
            player.sendMessage(ChatColor.YELLOW + "You have entered " + ChatColor.BLUE + "" + ChatColor.BOLD + NobilityRegions.regionMaster.getRegionName(toWorld, toBiome));
        } // if
    } // onPlayerMove

} // class
