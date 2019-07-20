package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;

/**
 * A unique, World-Biome combination
 */
public class Region {
    private String name;
    private World world;
    private Biome biome;

    /**
     * Creates a Region from a String name, World, and Biome
     * @param name The String name of this Region
     * @param world The World of this Region
     * @param biome The Biome of this Region
     */
    protected Region(String name, World world, Biome biome) {
        this.name = name;
        this.world = world;
        this.biome = biome;
    } // constructor

    /**
     * Sets the name of a Region and updates the config to match
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if(NobilityRegions.regionMaster.getRegionByName(name) != null) return false;

        this.name = name;
        NobilityRegions.instance.getConfig().set("regions." + world.getName() + "." + biome.name() + ".name", name);
        NobilityRegions.instance.saveConfig();

        return true;
    } // setName

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Biome getBiome() {
        return biome;
    }
} // class
