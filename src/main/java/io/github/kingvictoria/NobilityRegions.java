package io.github.kingvictoria;

import io.github.kingvictoria.commands.*;
import io.github.kingvictoria.nodes.Node;
import io.github.kingvictoria.nodes.NodeManager;

import java.time.chrono.ThaiBuddhistChronology;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class NobilityRegions extends JavaPlugin {
    private static NobilityRegions instance;
    private static RegionManager regionManager;
    private static NodeManager nodeManager;

    @Override
    public void onEnable() {
        instance = this;

        // Listeners
        getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);

        // Commands
        this.getCommand("listregions").setExecutor(new DebugCommandListRegions());
        this.getCommand("searchregions").setExecutor(new DebugCommandSearchRegions());
        this.getCommand("regioninfo").setExecutor(new DebugCommandRegionInfo());
        this.getCommand("getregion").setExecutor(new CommandGetRegion());
        this.getCommand("setregionname").setExecutor(new CommandSetRegionName());
        this.getCommand("setregionhabitability").setExecutor(new CommandSetRegionHabitability());
        this.getCommand("setregionresource").setExecutor(new CommandSetRegionResource());
        this.getCommand("addnode").setExecutor(new CommandAddNode());

        // Regions Initialization
        regionManager = new RegionManager(getConfig(), getServer().getWorlds());
        nodeManager = new NodeManager();
        saveConfig();
    } // onEnable

    @Override
    public void onDisable() {
        saveConfig();
    }

    /**
     * Access the RegionMaster to retrieve a Region object
     * 
     * Replaced by {@link #getRegionManager()}
     * 
     * @return RegionManager
     */
    @Deprecated
    public static RegionManager getRegionMaster() {
        return regionManager;
    }

    /**
     * Access the RegionManager to interact with Region objects
     * 
     * @return RegionManager
     */
    public static RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Access the NodeManager to interact with Region objects
     * 
     * @return
     */
    public static NodeManager getNodeManager() {
        return nodeManager;
    }

    /**
     * Access the NobilityRegions Instance
     * 
     * @return
     */
    public static NobilityRegions getInstance() {
        return instance;
    }
} // class
