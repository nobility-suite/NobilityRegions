package io.github.kingvictoria.regions;

import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Biome;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Configs.ConfigRegion;
import io.github.kingvictoria.regions.nodes.Node;
import io.github.kingvictoria.regions.nodes.NodeType;
import net.civex4.nobilityitems.NobilityItem;

/**
 * A unique, World-Biome combination
 */
public class Region {
    private String name;
    private World world;
    private Biome biome;
    private boolean habitable;
    private List<Node> nodes;
    private ConfigRegion config;

    /**
     * Creates a Region from a String name, World, and Biome
     * 
     * @param name      The String name of this Region
     * @param world     The World of this Region
     * @param biome     The Biome of this Region
     * @param habitable Whether this region is habitable (true) or wilderness
     *                  (false)
     * @param nodes     The list of resource nodes that exist in this region
     * @param config    ConfigRegion configuration
     */
    public Region(String name, World world, Biome biome, boolean habitable, List<Node> nodes, ConfigRegion config) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
        this.nodes = nodes;
        this.config = config;
    }

    public void makeNode(String id, String name, int slots, NodeType type, Map<NobilityItem, Integer> output) {
        Node node = config.makeNode(id).setName(name).setSlots(slots).setType(type).setOutput(output).make();

        if (node != null)
            nodes.add(node);
    }

    /**
     * Sets the name of a Region and updates the config to match
     * 
     * @param name String name
     * @return false if the name is already taken
     */
    public boolean setName(String name) {
        if (NobilityRegions.getRegionManager().getRegionByName(name) != null) {
            return false;
        }

        this.name = name;
        config.setName(name).save();

        return true;
    }

    /**
     * Sets whether this region is habitable or wilderness and updates the config to
     * match
     * 
     * @param value true if habitable
     */
    public void setHabitable(boolean value) {
        habitable = value;
        config.setHabitable(value).save();
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Biome getBiome() {
        return biome;
    }

    public boolean isHabitable() {
        return habitable;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Region && ((Region) o).biome.equals(biome) && ((Region) o).world.equals(world))
            return true;

        return false;
    }
}
