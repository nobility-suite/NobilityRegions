package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RegionMaster {

    private List<Region> regions;

    protected RegionMaster(Configuration config, List<World> worlds) {
        regions = new ArrayList<>();
        List<String> regionNames = new ArrayList<>();

        for(World world: worlds) {
            for(Biome biome: Biome.values()) {
                String key = "regions." + world.getName() + "." + biome.name() + ".";
                String name;

                if(config.isSet(key + "name")) {
                    name = config.getString(key + "name");

                    for(String n: regionNames) {
                        if(n.equalsIgnoreCase(name)) {
                            NobilityRegions.instance.getLogger().severe("The region name '" + name
                                    + "' has already been used. Re-naming to '" + world.getName() + "-" + biome.name() + "'");
                            name = world.getName() + "-" + biome.name();
                            config.set(key + "name", name);
                        } // if
                    } // for
                } else {
                    name = world.getName() + "-" + biome.name();
                    config.set(key + "name", name);
                } // if/else

                regionNames.add(name);
                regions.add(new Region(name, world, biome));
            } // for
        } // for
    } // constructor

    /**
     * Retrieve the name of a region by its unique World-Biome combination
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return String name of the Region
     */
    public String getRegionName(World world, Biome biome) {
        Region region = getRegion(world, biome);
        if(region != null) return region.getName();

        // If unable to find region, return "World-Biome"
        return world.getName() + "-" + biome.name();
    } // getRegionName

    /**
     * Gets a Region by its unique World-Biome combination
     * @param world World of a given Region
     * @param biome Biome of a given Region
     * @return null if combination is invalid
     */
    public Region getRegion(World world, Biome biome) {
        for(Region region: regions) {
            if(region.getBiome() == biome && region.getWorld().equals(world)) {
                return region;
            } // if
        } // for

        return null;
    } // getRegion

    /**
     * Gets a Region by its name
     * @param name String name of Region
     * @return null if there is no region with that name
     */
    public Region getRegionByName(String name) {
        for(Region region: regions) {
            if(name.equalsIgnoreCase(region.getName())) {
                return region;
            } // if
        } // for

        return null;
    } // getRegionByName

    public List<Region> getRegions() {
        return regions;
    }
} // class