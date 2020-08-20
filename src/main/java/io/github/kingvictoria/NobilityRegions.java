package io.github.kingvictoria;

import io.github.kingvictoria.regions.nodes.Node;
import io.github.kingvictoria.regions.RegionManager;

import java.io.File;
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

    @Override
    public void onEnable() {
        instance = this;

        // Register Events
        getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);

        // Register Commands
        this.getCommand("nobilityregions").setExecutor(new CommandListener());

        // Initializations
        Configs.init(new File(getDataFolder(), "regions.yml"));
        regionManager = new RegionManager();
    } // onEnable

    /**
     * Access the RegionManager to interact with Region objects
     * 
     * @return RegionManager
     */
    public static RegionManager getRegionManager() {
        return regionManager;
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
