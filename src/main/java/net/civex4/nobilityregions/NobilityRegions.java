package net.civex4.nobilityregions;

import net.civex4.nobilityregions.commands.CommandGetRegion;
import net.civex4.nobilityregions.commands.CommandSetRegionHabitability;
import net.civex4.nobilityregions.commands.CommandSetRegionName;
import net.civex4.nobilityregions.commands.CommandSetRegionResource;
import net.civex4.nobilityregions.commands.DebugCommandListRegions;
import net.civex4.nobilityregions.commands.DebugCommandRegionInfo;
import net.civex4.nobilityregions.commands.DebugCommandSearchRegions;
import org.bukkit.plugin.java.JavaPlugin;
import net.civex4.nobilityregions.region.RegionMaster;

public class NobilityRegions extends JavaPlugin {
  private static RegionMaster regionMaster;
  private static NobilityRegions instance;

  @Override
  public void onEnable() {
    instance = this;

    registerListeners();
    registerCommands();
    initialiseRegions();
  }

  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new RegionChangeListener(), this);
  }

  private void registerCommands() {
    getCommand("listregions").setExecutor(new DebugCommandListRegions());
    getCommand("searchregions").setExecutor(new DebugCommandSearchRegions());
    getCommand("regioninfo").setExecutor(new DebugCommandRegionInfo());
    getCommand("getregion").setExecutor(new CommandGetRegion());
    getCommand("setregionname").setExecutor(new CommandSetRegionName());
    getCommand("setregionhabitability").setExecutor(new CommandSetRegionHabitability());
    getCommand("setregionresource").setExecutor(new CommandSetRegionResource());
  }

  private void initialiseRegions() {
    regionMaster = new RegionMaster(getServer().getWorlds());
    saveConfig();
  }


  @Override
  public void onDisable() {
    saveConfig();
  }

  /**
   * Access the RegionMaster to retrieve a Region object.
   */
  public static RegionMaster getRegionMaster() {
    if (regionMaster == null) {
      throw new IllegalStateException("RegionMaster instance not ready yet!");
    }
    return regionMaster;
  }

  /**
   * Access the NobilityRegions Instance.
   */
  public static NobilityRegions getInstance() {
    if (instance == null) {
      throw new IllegalStateException("NobilityRegions instance not ready yet!");
    }
    return instance;
  }
}
