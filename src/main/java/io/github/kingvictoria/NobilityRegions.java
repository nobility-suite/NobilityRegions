package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class NobilityRegions extends JavaPlugin {
    protected static RegionMaster regionMaster;
    protected static NobilityRegions instance;

    @Override
    public void onEnable() {
        instance = this;

        // Listeners
        getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);

        // Regions Initialization
        regionMaster = new RegionMaster(getConfig(), getServer().getWorlds());
        saveConfig();
    } // onEnable

    @Override
    public void onDisable() {
        saveConfig();
    } // onDisable

    /**
     * Retrieves the Region for a given World-Biome combination
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return null if combination is correlated with no region (likely something is wrong)
     */
    public static Region getRegion(World world, Biome biome) {
        return regionMaster.getRegion(world, biome);
    } // getRegion
} // class
