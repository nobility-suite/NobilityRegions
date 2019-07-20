package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RegionMaster {

    private List<Region> regions;

    protected RegionMaster(Configuration config) {
        regions = new ArrayList<>();

    } // constructor

    /**
     * Retrieve the name of a region by its unique World-Biome combination
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return String name of the Region
     */
    public String getRegionName(World world, Biome biome) {
        for(Region region: regions) {
            if(region.getBiome() == biome && region.getWorld().equals(world)) {
                 return region.getName();
            } // if
        } // for

        // If unable to find region, return "World-Biome"
        return world.getName() + "-" + biome.name();
    } // getRegionName

} // class