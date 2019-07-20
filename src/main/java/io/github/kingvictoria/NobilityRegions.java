package io.github.kingvictoria;

import io.github.kingvictoria.commands.CommandGetRegion;
import io.github.kingvictoria.commands.DebugCommandListRegions;
import io.github.kingvictoria.commands.DebugCommandRegionInfo;
import io.github.kingvictoria.commands.DebugCommandSearchRegions;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class NobilityRegions extends JavaPlugin {
    public static RegionMaster regionMaster;
    public static NobilityRegions instance;

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
