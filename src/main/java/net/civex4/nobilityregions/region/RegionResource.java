package net.civex4.nobilityregions.region;

public enum RegionResource {
  WOOD, STONE, IRON, WHEAT;

  public static RegionResource getResource(String resourceString) {
    for (RegionResource resource : RegionResource.values()) {
      if (resourceString.equalsIgnoreCase(resource.toString())) {
        return resource;
      }
    }

    return null;
  }
}
