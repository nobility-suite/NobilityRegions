package io.github.kingvictoria.regions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Biome;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Configs;
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
     * DON'T USE THIS CONSTRUCTOR! Use {@link Configs#generateRegion(World, Biome)} to generate a Region
     */
    public Region(String name, World world, Biome biome, boolean habitable, List<Node> nodes, ConfigRegion config) {
        this.name = name;
        this.world = world;
        this.biome = biome;
        this.habitable = habitable;
        this.nodes = nodes;
        this.config = config;
    }

    /**
     * Makes a Node in this Region
     * 
     * @param id String config key of this Node
     * @param name String name of this Node to display to players
     * @param slots int number of slots for workers in this Node
     * @param type NodeType type of Node (used to display in GUIs)
     * @param output Map of NobilityItem to Integer output of the node (item : amount)
     */
    public void makeNode(String id, String name, int slots, NodeType type, Map<NobilityItem, Integer> output) {
        Node node = config.makeNode(id).setName(name).setSlots(slots).setType(type).setOutput(output).make();

        if (node != null)
            nodes.add(node);
    }

    /**
     * Removes a Node from this Region
     * 
     * @param name String name of the Node
     * @return false if there is no Node with that name
     */
    public boolean removeNode(String name) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName().equals(name)) {
                nodes.get(i).delete();
                nodes.remove(i);
                return true;
            }
        }

         return false;
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

    /**
     * Gets the name of this Region (displayed to players)
     * 
     * @return String name
     */
    public String getName() {
        return new String(name);
    }

    /**
     * Gets the World this Region is associated with
     * 
     * @return World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the Biome this Region is associated with
     * 
     * @return Biome
     */
    public Biome getBiome() {
        return biome;
    }

    /**
     * Whether this Region is habitable (will display 'You Have Entered/Left the Wilderness', etc.)
     * 
     * @return boolean
     */
    public boolean isHabitable() {
        return habitable;
    }

    /**
     * Gets a copy of the list of Nodes in this Region
     * 
     * @return List of Nodes
     */
    public List<Node> getNodes() {
        return new ArrayList<>(nodes);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Region && ((Region) o).biome.equals(biome) && ((Region) o).world.equals(world))
            return true;

        return false;
    }
}
