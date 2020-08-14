package io.github.kingvictoria;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import io.github.kingvictoria.regions.Region;

import java.util.HashMap;
import java.util.Map;

public class Configs {

    public static class ConfigRegion {

        private String worldName;
        private String biomeName;
        private String key;
        private Map<String, Object> changes;
        private Configuration config;

        private ConfigRegion(String worldName, String biomeName) {
            this.biomeName = biomeName;
            this.worldName = worldName;
            this.key = "regions" + "." + worldName + "." + biomeName;
            changes = new HashMap<>();
            config = NobilityRegions.getInstance().getConfig();
        } // Constructor

        /**
         * Checks to see if a name is unique
         * 
         * @param name String name
         * @return false if name is used more than once for a Region in the config
         */
        private boolean isUniqueName(String name) {
            boolean used = false;

            for (String worldString : config.getConfigurationSection("regions").getKeys(false)) {
                for (String biomeString : config.getConfigurationSection("regions." + worldString).getKeys(false)) {
                    ConfigurationSection regionSection = config
                            .getConfigurationSection("regions." + worldString + "." + biomeString);

                    if (regionSection.getString("name").equalsIgnoreCase(name)) {
                        if (used) {
                            return false;
                        } else {
                            used = true;
                        }
                    } // if
                } // for
            } // for

            return true;
        } // name

        /**
         * Saves the changes to the config
         */
        public void save() {
            for (String label : changes.keySet()) {
                config.set(key + "." + label, changes.get(label));
            } // for

            NobilityRegions.getInstance().saveConfig();
        } // save

        /**
         * Loads a Region object from the config
         * 
         * @return Region object
         */
        public Region load() {
            World world = Bukkit.getWorld(worldName);
            Biome biome = Biome.valueOf(biomeName);
            String name;
            boolean habitable;

            // NAME
            if (config.isSet(key + ".name")) {
                name = config.getString(key + ".name");

                if (!isUniqueName(name)) {
                    NobilityRegions.getInstance().getLogger().severe("The region name '" + name
                            + "' has already been used. Re-naming to '" + worldName + "-" + biomeName + "'");
                    name = worldName + "-" + biomeName;
                    setName(name);
                } // if
            } else {
                name = worldName + "-" + biomeName;
                setName(name);
            } // if/else

            // HABITABLE
            if (config.isSet(key + ".habitable")) {
                habitable = config.getBoolean(key + ".habitable");
            } else {
                habitable = true;
                setHabitable(true);
            } // if/else

            save();
            return new Region(name, world, biome, habitable);
        } // load

        public ConfigRegion setName(String name) {
            changes.put("name", name);
            return this;
        } // setName

        public ConfigRegion setHabitable(boolean habitable) {
            changes.put("habitable", habitable);
            return this;
        } // setHabitable
    } // ConfigRegion

    /**
     * Accesses the config for a region
     * 
     * @param region Region to configure
     * @return ConfigRegion object
     */
    public static ConfigRegion region(Region region) {
        return region(region.getWorld(), region.getBiome());
    } // region(Region)

    /**
     * Accesses the config for a region with the World and Biome objects
     * 
     * @param world World world
     * @param biome Biome biome
     * @return ConfigRegion object
     */
    public static ConfigRegion region(World world, Biome biome) {
        return region(world.getName(), biome.name());
    } // region(World, Biome)

    /**
     * Accesses the config for a region with the name of the world and biome
     * 
     * @param worldName String world name
     * @param biomeName String biome name
     * @return ConfigRegion object
     */
    public static ConfigRegion region(String worldName, String biomeName) {
        return new ConfigRegion(worldName, biomeName);
    } // region(String, String)

} // Config
