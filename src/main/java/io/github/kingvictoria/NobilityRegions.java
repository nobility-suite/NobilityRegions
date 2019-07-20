package io.github.kingvictoria;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class NobilityRegions extends JavaPlugin {
    protected static RegionMaster regionMaster;

    @Override
    public void onEnable() {
        // Listeners
        getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);

        // Initialization
        regionMaster = new RegionMaster(getConfig());
    } // onEnable

    @Override
    public void onDisable() {
        // TODO: Save Regions
    } // onDisable
} // class
