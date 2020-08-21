package io.github.kingvictoria;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.regions.nodes.Node;
import io.github.kingvictoria.regions.nodes.NodeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.civex4.nobilityitems.NobilityItem;
import net.civex4.nobilityitems.NobilityItems;

public class Configs {

    private static FileConfiguration regionsConfig;
    private static File regionsConfigFile;

    public static void init(File regionsFile) {
        if (!regionsFile.exists()) {
            NobilityRegions.getInstance().saveResource("regions.yml", false);
        }

        regionsConfigFile = regionsFile;

        try {
            regionsConfig = YamlConfiguration.loadConfiguration(regionsConfigFile);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().severe("Regions config failed to load!");
            e.printStackTrace();
        }
    }

    public static List<Region> loadRegions() {
        List<Region> regions = new ArrayList<>();

        Bukkit.getLogger().info("Loading Regions...");
        for (String worldString : regionsConfig.getKeys(false)) {
            for (String biomeString : regionsConfig.getConfigurationSection(worldString).getKeys(false)) {
                regions.add(
                        Configs.generateConfigRegion(Bukkit.getWorld(worldString), Biome.valueOf(biomeString)).load());
            }
        }
        Bukkit.getLogger().info("Regions Loaded!");

        return regions;
    }

    public static class ConfigRegion {

        public static class ConfigNode {
            private Map<String, Object> changes;
            private ConfigurationSection config;
            private ConfigRegion regionConfig;

            private ConfigNode(ConfigurationSection nodeConfig, ConfigRegion region) {
                changes = new HashMap<>();
                config = nodeConfig;
                regionConfig = region;
            }

            @Override
            public boolean equals(Object o) {
                if (o instanceof ConfigNode && ((ConfigNode) o).config.getCurrentPath().equals(config.getCurrentPath())) {
                    return true;
                } else {
                    return false;
                }
            }

            public void delete() {
                regionConfig.deleteNode(this);
            }

            /**
             * Saves the changes to the config
             */
            public void save() {
                save(true);
            }

            private void save(boolean saveFile) {
                for (String label : changes.keySet()) {
                    config.set(label, changes.get(label));
                }

                changes.clear();
                if (saveFile)
                    regionConfig.saveFile();
            }

            /**
             * Makes a Node and saves it to the config
             * 
             * @return Node created, null if unable to make
             */
            public Node make() {
                save(true);
                return load();
            }

            private Node load() {
                String name = null;
                int slots = 0;
                NodeType type = null;
                Map<NobilityItem, Integer> output = null;
                List<UUID> workers = new ArrayList<>();

                boolean returnNull = false;

                if (config.isString("name")) {
                    name = config.getString("name");
                } else {
                    Bukkit.getLogger().warning("Node " + config.getCurrentPath() + " has no name!");
                    returnNull = true;
                }

                if (config.isInt("slots")) {
                    slots = config.getInt("slots");
                } else {
                    Bukkit.getLogger().warning("Node " + config.getCurrentPath() + " has no slots!");
                    returnNull = true;
                }

                if (config.isString("type")) {
                    type = NodeType.valueOf(config.getString("type"));
                } else {
                    Bukkit.getLogger().warning("Node " + config.getCurrentPath() + " has no type!");
                    returnNull = true;
                }

                if (config.isConfigurationSection("output")) {
                    ConfigurationSection outputConfig = config.getConfigurationSection("output");
                    output = new HashMap<>();

                    for (String outputItem : outputConfig.getKeys(false)) {
                        if (!outputConfig.isInt(outputItem)) {
                            Bukkit.getLogger().severe(outputItem + " in " + outputConfig.getCurrentPath() + " is not an integer!");
                            returnNull = true;
                            continue;
                        }

                        try {
                            output.put(NobilityItems.getItemByName(outputItem), outputConfig.getInt(outputItem));
                        } catch (IllegalArgumentException e) {
                            Bukkit.getLogger().severe("Invalid NobilityItem " + outputItem + " in " + config.getCurrentPath());
                            returnNull = true;
                        }
                    }

                    
                } else {
                    Bukkit.getLogger().warning("Node " + config.getCurrentPath() + " has no output!");
                    returnNull = true;
                }

                if (config.isList("workers")) {
                    List<String> stringWorkers = config.getStringList("workers");
                    for (String worker : stringWorkers) {
                        try {
                            workers.add(UUID.fromString(worker));
                        } catch (IllegalArgumentException e) {
                            Bukkit.getLogger().warning(
                                    "Node " + config.getCurrentPath() + " has malformed UUID in workers " + worker);
                            returnNull = true;
                        }
                    }
                }

                if (returnNull)
                    return null;

                return new Node(name, slots, type, output, workers, this);
            }

            public ConfigNode setName(String name) {
                changes.put("name", name);
                return this;
            }

            public ConfigNode setSlots(int slots) {
                changes.put("slots", slots);
                return this;
            }

            public ConfigNode setType(NodeType type) {
                changes.put("type", type.name());
                return this;
            }

            public ConfigNode setOutput(Map<NobilityItem, Integer> output) {
                for (NobilityItem item : output.keySet()) {
                    changes.put("output." + item.getInternalName(), output.get(item));
                }
                return this;
            }

            public ConfigNode setWorkers(List<UUID> workers) {
                List<String> stringWorkers = new ArrayList<>();
                for (UUID worker: workers) {
                    stringWorkers.add(worker.toString());
                }

                changes.put("workers", stringWorkers);
                return this;
            }
        }

        private Map<String, Object> changes;
        private List<ConfigNode> nodeConfigs;
        private ConfigurationSection config;
        private World world;
        private Biome biome;

        private ConfigRegion(World world, Biome biome) {
            changes = new HashMap<>();
            nodeConfigs = new ArrayList<>();
            config = regionsConfig.getConfigurationSection(world.getName() + "." + biome.name());
            this.world = world;
            this.biome = biome;
        }

        private void deleteNode(ConfigNode configNode) {
            String path = "nodes" + configNode.config.getCurrentPath().substring(configNode.config.getCurrentPath().lastIndexOf("."));
            nodeConfigs.remove(configNode);
            changes.put(path, null);
            save();
        }

        /**
         * Checks to see if a name is unique
         * 
         * @param name String name
         * @return false if name is used more than once for a Region in the config
         */
        private boolean isUniqueName(String name) {
            boolean used = false;

            for (String worldString : regionsConfig.getKeys(false)) {
                for (String biomeString : regionsConfig.getConfigurationSection(worldString).getKeys(false)) {
                    ConfigurationSection regionSection = regionsConfig
                            .getConfigurationSection(worldString + "." + biomeString);

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

        private void saveFile() {
            try {
                regionsConfig.save(regionsConfigFile);
                changes.clear();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to save Regions config file!");
                e.printStackTrace();
            }
        }

        /**
         * Saves the changes to the config
         */
        public void save() {
            for (String label : changes.keySet()) {
                config.set(label, changes.get(label));
            }

            for (ConfigNode node : nodeConfigs) {
                node.save(false);
            }

            saveFile();
        }

        /**
         * Creates a Region and saves it to the config
         * 
         * @return Region created
         */
        public Region make() {
            config = regionsConfig.createSection(world.getName() + "." + biome.name());
            String name = world.getName() + "-" + biome.name();
            boolean habitable = true;
            List<Node> nodes = new ArrayList<>();

            setName(name);
            setHabitable(habitable);

            save();
            return new Region(name, world, biome, habitable, nodes, this);
        }

        /**
         * Loads a Region object from the config
         * 
         * @return Region object
         */
        public Region load() {
            String name = null;
            boolean habitable = true;
            List<Node> nodes = new ArrayList<>();
            boolean returnNull = false;

            // NAME
            if (config.isString("name")) {
                name = config.getString("name");

                if (!isUniqueName(name)) {
                    NobilityRegions.getInstance().getLogger()
                            .severe("The region name '" + name + "' has already been used." + " Re-naming to '"
                                    + world.getName() + "-" + biome.name() + "'");
                    name = world.getName() + "-" + biome.name();
                    setName(name);
                }
            } else {
                Bukkit.getLogger().severe("Region " + config.getCurrentPath() + " has no name!");
                returnNull = true;
            }

            // HABITABLE
            if (config.isBoolean("habitable")) {
                habitable = config.getBoolean("habitable");
            } else {
                Bukkit.getLogger().severe("Region " + config.getCurrentPath() + " has no habitable!");
                returnNull = true;
            }

            // NODES
            if (config.isConfigurationSection("nodes")) {
                for (String key : config.getConfigurationSection("nodes").getKeys(false)) {
                    Node node = new ConfigNode(config.getConfigurationSection("nodes." + key), this).load();

                    if (node == null) {
                        returnNull = true;
                        continue;
                    } else {
                        nodes.add(node);
                    }
                }
            }

            if (returnNull) {
                Bukkit.getLogger().severe("Failed to load Region " + config.getCurrentPath());
                return null;
            }
            return new Region(name, world, biome, habitable, nodes, this);
        }

        public ConfigRegion setName(String name) {
            changes.put("name", name);
            return this;
        }

        public ConfigRegion setHabitable(boolean habitable) {
            changes.put("habitable", habitable);
            return this;
        }

        public ConfigNode makeNode(String id) {
            return new ConfigNode(config.createSection("nodes." + id), this);
        }
    }

    /**
     * Generate the config for a region
     * 
     * @param world World the Region is in
     * @param biome Biome the Region is in
     * @return ConfigRegion object
     */
    public static ConfigRegion generateConfigRegion(World world, Biome biome) {
        return new ConfigRegion(world, biome);
    }

} // Config
