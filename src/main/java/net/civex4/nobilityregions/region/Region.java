package net.civex4.nobilityregions.region;

import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Map;

public interface Region {
  boolean setName(String name);

  void setHabitable(boolean value);

  void setResource(RegionResource resource, int value);

  void removeResource(RegionResource resource);

  String getName();

  World getWorld();

  Biome getBiome();

  boolean isHabitable();

  int getResource(RegionResource resource);

  default int getResource(String resourceString) {
    return getResource(RegionResource.getResource(resourceString));
  }

  Map<RegionResource, Integer> getResources();
}
