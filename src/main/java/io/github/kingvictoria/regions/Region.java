package io.github.kingvictoria.regions;

import org.bukkit.World;
import org.bukkit.block.Biome;

import io.github.kingvictoria.Configs;
import io.github.kingvictoria.NobilityRegions;

/**
 * A unique, World-Biome combination
 */
public class Region {
    private String name;
    private World world;
    private Biome biome;
    private boolean habitable;

    /**
     * Creates a Region from a String name, World, and Biome
     * 
     * @param name  The String name of this Region
     * @param world The World of this Region
     * @param biome The Biome of this Region
     */
    public Region(String name, World world, Biome biome, boolean habitable) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
    }

    /**
     * Sets the name of a Region and updates the config to match
     * 
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if (NobilityRegions.getRegionManager().getRegionByName(name) != null) {
            return false;
        }

        this.name = name;
        Configs.region(this).setName(name).save();

        return true;
    }

    /**
     * Sets whether this region is habitable or wilderness and updates the config to
     * match
     * 
     * @param value true if habitable
     */
    public void setHabitable(boolean value) {
        habitable = value;
        Configs.region(this).setHabitable(value).save();
    }

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
        if (o instanceof Region) {
            if (((Region) o).name.equalsIgnoreCase(name))
                return true;
        }

        return false;
    }
}
