package net.civex4.nobilityregions.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class RegionMaster {

  private final List<Region> regions;

  public RegionMaster(List<World> worlds) {
    regions = new ArrayList<>();

    for (World world : worlds) {
      for (Biome biome : Biome.values()) {
        regions.add(new ConfigRegion(world.getName(), biome.name()));
      }
    }
  }

  /**
   * Retrieve the name of a Region by its unique World-Biome combination
   *
   * @param world World of a given Region
   * @param biome Biome of a given Region
   * @return String name of the Region
   */
  public String getRegionName(World world, Biome biome) {
    Region region = getRegion(world, biome);
    if (region == null) {
      // If unable to find Region, return "World-Biome"
      return world.getName() + "-" + biome.name();
    } else {
      return region.getName();
    }
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
      }
    }

    return null;
  }

  /**
   * Gets a Region by its name
   *
   * @param name String name of Region
   * @return null if there is no Region with that name
   */
  public Region getRegionByName(String name) {
    for (Region region : regions) {
      if (name.equalsIgnoreCase(region.getName())) {
        return region;
      }
    }

    return null;
  }

  /**
   * Gets the Region of a Location
   *
   * @param location Location location
   * @return null if the location is somehow outside of any Region (this means something is broken)
   */
  public Region getRegionByLocation(Location location) {
    for (Region region : regions) {
      if (region.getBiome().equals(location.getBlock().getBiome()) && region.getWorld().equals(location.getWorld())) {
        return region;
      }
    }

    return null;
  }

  /**
   * Retrieves a list of all regions
   *
   * @return List of Region objects
   */
  public List<Region> getRegions() {
    return regions;
  }
}