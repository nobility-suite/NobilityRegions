package io.github.kingvictoria.regions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;

import io.github.kingvictoria.Configs;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {

    private List<Region> regions;

    public RegionManager(Configuration config, List<World> worlds) {
        regions = new ArrayList<>();

        for (World world : worlds) {
            for (Biome biome : Biome.values()) {
                regions.add(Configs.region(world, biome).load());
            }
        }
    }

    /**
     * Retrieve the name of a region by its unique World-Biome combination
     * 
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return String name of the Region
     */
    public String getRegionName(World world, Biome biome) {
        Region region = getRegion(world, biome);
        if (region != null)
            return region.getName();

        // If unable to find region, return "World-Biome"
        return world.getName() + "-" + biome.name();
    }

    /**
     * Gets a Region by its unique World-Biome combination
     * 
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return null if combination is invalid
     */
    public Region getRegion(World world, Biome biome) {
        for (Region region : regions) {
            if (region.getBiome() == biome && region.getWorld().equals(world)) {
                return region;
            } // if
        } // for

        return null;
    }

    /**
     * Gets a Region by its name
     * 
     * @param name String name of Region
     * @return null if there is no region with that name
     */
    public Region getRegionByName(String name) {
        for (Region region : regions) {
            if (name.equalsIgnoreCase(region.getName())) {
                return region;
            } // if
        } // for

        return null;
    }

    /**
     * Gets the Region of a Location
     * 
     * @param location Location location
     * @return null if the location is somehow outside of any region (this means
     *         something is broken)
     */
    public Region getRegionByLocation(Location location) {
        for (Region region : regions) {
            if (region.getBiome().equals(location.getBlock().getBiome())
                    && region.getWorld().equals(location.getWorld())) {
                return region;
            }
        }

        return null;
    } // getRegionByLocation

    /**
     * Retrieves a list of all regions
     * 
     * @return List of Region objects
     */
    public List<Region> getRegions() {
        return regions;
    }
} // class