package net.civex4.nobilityregions.region;

import net.civex4.nobilityregions.NobilityRegions;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Map;

/**
 * A unique, World-Biome combination.
 */
public abstract class AbstractRegion implements Region {
  protected String name;
  protected World world;
  protected Biome biome;
  protected boolean habitable;
  protected Map<RegionResource, Integer> resources;

  /**
   * Sets the name of a Region and updates the config to match.
   *
   * @param name String name
   * @return false if the name is already taken
   */
  @Override
  public boolean setName(String name) {
    if (NobilityRegions.getRegionMaster().getRegionByName(name) != null) {
      return false;
    }

    this.name = name;
    return true;
  }

  /**
   * Sets whether this Region is habitable or wilderness and updates the config to match.
   *
   * @param value true if habitable
   */
  @Override
  public void setHabitable(boolean value) {
    habitable = value;
  }

  /**
   * Sets a resource with a value (overwrites existing resources with the same name)
   * and updates the config to match.
   *
   * @param resource String name of resource
   * @param value    int value
   */
  @Override
  public void setResource(RegionResource resource, int value) {
    resources.put(resource, value);
  }

  /**
   * Removes a resource from this Region and updates the config to match.
   *
   * @param resource String name of resource
   */
  @Override
  public void removeResource(RegionResource resource) {
    resources.remove(resource);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public Biome getBiome() {
    return biome;
  }

  @Override
  public boolean isHabitable() {
    return habitable;
  }

  @Override
  public int getResource(RegionResource resource) {
    return resources.get(resource);
  }

  @Override
  public Map<RegionResource, Integer> getResources() {
    return resources;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof AbstractRegion && ((AbstractRegion) o).name.equalsIgnoreCase(name);
  }
}
