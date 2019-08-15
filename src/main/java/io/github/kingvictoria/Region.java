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
    protected Region(String name, World world, Biome biome, boolean habitable, Map<String, Double> resources) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
        this.resources = resources;
    } // constructor

    /**
     * Sets the name of a Region and updates the config to match
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if(NobilityRegions.getRegionMaster().getRegionByName(name) != null) return false;

        this.name = name;
        Configs.region(this).setName(name).save();

        return true;
    } // setName

    /**
     * Sets whether this region is habitable or wilderness and updates the config to match
     * @param value true if habitable
     */
    public void setHabitable(boolean value) {
        habitable = value;
        Configs.region(this).setHabitable(value).save();
    } // setHabitable

    /**
     * Sets a resource with a value (overwrites existing resources with the same name) and updates the config to match
     * @param resource String name of resource
     * @param value double value
     */
    public void setResource(String resource, double value) {
        resources.put(resource, value);
        Configs.region(this).putResource(resource, value).save();
    } // setResource

    /**
     * Removes a resource from this region and updates the config to match
     * @param resource String name of resource
     */
    public void removeResource(String resource) {
        resources.remove(resource);
        Configs.region(this).deleteResource(resource).save();
    } // removeResource

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

    public double getResource(String resource) {
        return resources.get(resource).doubleValue();
    }

    public Map<String, Double> getResources() {
        return resources;
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
