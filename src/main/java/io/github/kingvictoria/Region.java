package io.github.kingvictoria;

import org.bukkit.Bukkit;
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
    private Map<RegionResource, Integer> resources;

    /**
     * Creates a Region from a String name, World, and Biome
     * 
     * @param name  The String name of this Region
     * @param world The World of this Region
     * @param biome The Biome of this Region
     */
    protected Region(String name, World world, Biome biome, boolean habitable, Map<RegionResource, Integer> resources) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
        this.resources = resources;
    } // constructor

    /**
     * Sets the name of a Region and updates the config to match
     * 
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if (NobilityRegions.getRegionManager().getRegionByName(name) != null)
            return false;

        this.name = name;
        Configs.region(this).setName(name).save();

        return true;
    } // setName

    /**
     * Sets whether this region is habitable or wilderness and updates the config to
     * match
     * 
     * @param value true if habitable
     */
    public void setHabitable(boolean value) {
        habitable = value;
        Configs.region(this).setHabitable(value).save();
    } // setHabitable

    /**
     * Sets a resource with a value (overwrites existing resources with the same
     * name) and updates the config to match
     * 
     * @param resource String name of resource
     * @param value    int value
     */
    public void setResource(RegionResource resource, int value) {
        resources.put(resource, value);
        Configs.region(this).putResource(resource, value).save();
    } // setResource

    /**
     * Removes a resource from this region and updates the config to match
     * 
     * @param resource String name of resource
     */
    public void removeResource(RegionResource resource) {
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

    public double getResource(RegionResource resource) {
        if (!resources.containsKey(resource)) {
            Bukkit.getLogger().warning("A resource (" + resource.toString() + ")has been requested in a region ("
                    + this.name + ") where it does not exist");
            return 0;
        }
        return resources.get(resource).intValue();
    }

    public double getResource(String resourceString) {
        RegionResource resource = RegionResource.getResource(resourceString);
        return getResource(resource);
    }

    public Map<RegionResource, Integer> getResources() {
        return resources;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Region) {
            if (((Region) o).name.equalsIgnoreCase(name))
                return true;
        } // if

        return false;
    } // equals
} // class
