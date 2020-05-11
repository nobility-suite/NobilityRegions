package io.github.kingvictoria;

import io.github.kingvictoria.commands.*;
import io.github.kingvictoria.nodes.NodeManager;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class NobilityRegions extends JavaPlugin {
    private static RegionMaster regionMaster;
    private static NobilityRegions instance;
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
        regionMaster = new RegionMaster(getConfig(), getServer().getWorlds());
        nodeManager = new NodeManager();
        saveConfig();
    } // onEnable

    @Override
    public void onDisable() {
        saveConfig();
    } // onDisable

    /**
     * Access the RegionMaster to retrieve a Region object
     * @return
     */
    public static RegionMaster getRegionMaster() {
        return regionMaster;
    } // getRegionMaster

    /**
     * Access the NodeManager to retrieve a Node object
     * @return
     */
    public static NodeManager getNodeManager() {
        return nodeManager;
    } // getNodeManager
    
    /**
     * Access the NobilityRegions Instance
     * @return
     */
    public static NobilityRegions getInstance() {
        return instance;
    } // getInstance
} // class
