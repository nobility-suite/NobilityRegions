package io.github.kingvictoria;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configs {

    public static class ConfigRegion {

        private Map<String, Object> changes;
        private ConfigurationSection config;
        private World world;
        private Biome biome;

        private ConfigRegion(World world, Biome biome) {
            changes = new HashMap<>();
            config = NobilityRegions.getInstance().getConfig()
                    .getConfigurationSection("regions." + world.getName() + "." + biome.name());
            this.world = world;
            this.biome = biome;
        }

        /**
         * Checks to see if a name is unique
         * 
         * @param name String name
         * @return false if name is used more than once for a Region in the config
         */
        private boolean isUniqueName(String name) {
            boolean used = false;
            Configuration config = NobilityRegions.getInstance().getConfig();

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
                    }
                }
            }

            return true;
        }

        /**
         * Saves the changes to the config
         */
        public void save() {
            for (String label : changes.keySet()) {
                config.set(label, changes.get(label));
            }

            NobilityRegions.getInstance().saveConfig();
        }

        /**
         * Loads a Region object from the config
         * 
         * @return Region object
         */
        public Region load() {
            String name;
            boolean habitable;
            List<Node> nodes = new ArrayList<>();

            // NAME
            if (config.isSet("name")) {
                name = config.getString("name");

                if (!isUniqueName(name)) {
                    NobilityRegions.getInstance().getLogger()
                            .severe("The region name '" + name + "' has already been used." + " Re-naming to '"
                                    + world.getName() + "-" + biome.name() + "'");
                    name = world.getName() + "-" + biome.name();
                    setName(name);
                }
            } else {
                name = world.getName() + "-" + biome.name();
                setName(name);
            }

            // HABITABLE
            if (config.isSet("habitable")) {
                habitable = config.getBoolean("habitable");
            } else {
                habitable = true;
                setHabitable(true);
            }

            if (config.isConfigurationSection("nodes")) {
                // TODO: load nodes
            } else {
                // TODO: set empty list of nodes
            }

            save();
            return new Region(name, world, biome, habitable, nodes);
        }

        public ConfigRegion setName(String name) {
            changes.put("name", name);
            return this;
        }

        public ConfigRegion setHabitable(boolean habitable) {
            changes.put("habitable", habitable);
            return this;
        }

        public ConfigRegion addNode(Node node) {
            
            return this;
        }
    }

    /**
     * Accesses the config for a region
     * 
     * @param region Region to configure
     * @return ConfigRegion object
     */
    public static ConfigRegion region(Region region) {
        return region(region.getWorld(), region.getBiome());
    }

    /**
     * Accesses the config for a region
     * 
     * @param world World the Region is in
     * @param biome Biome the Region is in
     * @return ConfigRegion object
     */
    public static ConfigRegion region(World world, Biome biome) {
        return new ConfigRegion(world, biome);
    }

} // Config
