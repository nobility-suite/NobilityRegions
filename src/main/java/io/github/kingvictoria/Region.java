package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Map;

/**
 * A unique, World-Biome combination
 */
public class Region {
    private String name;
    private World world;
    private Biome biome;
    private boolean habitable;
    private Map<String, Double> resources;

    /**
     * Creates a Region from a String name, World, and Biome
     * @param name The String name of this Region
     * @param world The World of this Region
     * @param biome The Biome of this Region
     */
    protected Region(String name, World world, Biome biome, boolean habitable) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
    } // constructor

    /**
     * Sets the name of a Region and updates the config to match
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if(NobilityRegions.regionMaster.getRegionByName(name) != null) return false;

        this.name = name;
        Configs.region(this).setName(name).save();

        return true;
    } // setName

    /**
     * Sets whether this region is habitable or wilderness
     * @param value true if habitable
     */
    public void setHabitable(boolean value) {
        habitable = value;
        Configs.region(this).setHabitable(value).save();
    } // setHabitable

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Biome getBiome() {
        return biome;
    }

    public boolean isHabitable() {
        return habitable;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Region) {
            Region region = (Region) o;

            if(((Region) o).name.equalsIgnoreCase(name)) return true;
        } // if

        return false;
    } // equals
} // class
