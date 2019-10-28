package net.civex4.nobilityregions.region;

import net.civex4.nobilityregions.NobilityRegions;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegion extends AbstractRegion {

  private final String key;
  private final Map<String, Object> changes;
  private final Configuration config;

  protected ConfigRegion(String worldName, String biomeName) {
    this.key = "regions." + worldName + "." + biomeName;
    this.changes = new HashMap<>();
    this.config = NobilityRegions.getInstance().getConfig();

    this.world = Bukkit.getWorld(worldName);
    this.biome = Biome.valueOf(biomeName);
    this.resources = new HashMap<>();

    String name;

    // NAME
    if (config.isSet(key + ".name")) {
      name = config.getString(key + ".name");

      if (!isUniqueName(name)) {
        NobilityRegions.getInstance().getLogger().severe("The Region name '" + name
            + "' has already been used. Re-naming to '" + worldName + "-" + biomeName + "'");
        name = worldName + "-" + biomeName;
        setName(name);
      }
    } else {
      name = worldName + "-" + biomeName;
      setName(name);
    }

    // HABITABLE
    if (config.isSet(key + ".habitable")) {
      habitable = config.getBoolean(key + ".habitable");
    } else {
      setHabitable(true);
    }

    // RESOURCES
    resources = new HashMap<>();
    if (config.isSet(key + ".resource")) {
      ConfigurationSection resourceConfig = config.getConfigurationSection(key + ".resource");

      for (String resourceString : resourceConfig.getKeys(false)) {
        RegionResource resource = RegionResource.getResource(resourceString);
        resources.put(resource, resourceConfig.getInt(resourceString));
      }
    }

    save();
  }

  /**
   * Checks to see if a name is unique.
   *
   * @param name String name
   * @return false if name is used more than once for a Region in the config
   */
  private boolean isUniqueName(String name) {
    boolean used = false;

    for (String worldString : config.getConfigurationSection("regions").getKeys(false)) {
      for (String biomeString :
          config.getConfigurationSection("regions." + worldString).getKeys(false)) {
        ConfigurationSection regionSection = config.getConfigurationSection(
            "regions." + worldString + "." + biomeString);

        if (regionSection.getString("name").equalsIgnoreCase(name)) {
          if (used) {
            return false;
          } else {
            used = true;
          }
        }
      }
    }

    return true;
  }

  /**
   * Saves the changes to the config.
   */
  private void save() {
    for (String label : changes.keySet()) {
      config.set(key + "." + label, changes.get(label));
    }

    NobilityRegions.getInstance().saveConfig();
  }

  @Override
  public boolean setName(String name) {
    if (!super.setName(name)) {
      return false;
    }

    changes.put("name", name);
    save();
    return true;
  }

  @Override
  public void setHabitable(boolean value) {
    changes.put("habitable", habitable);
    save();
    super.setHabitable(value);
  }

  @Override
  public void setResource(RegionResource resource, int value) {
    super.setResource(resource, value);
    changes.put("resource." + resource.toString(), value);
    save();
  }

  @Override
  public void removeResource(RegionResource resource) {
    super.removeResource(resource);
    changes.put("resource." + resource.toString(), null);
    save();
  }
}
