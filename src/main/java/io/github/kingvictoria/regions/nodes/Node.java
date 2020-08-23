package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import io.github.kingvictoria.regions.Region;
import io.github.kingvictoria.Configs.ConfigRegion.ConfigNode;
import net.civex4.nobilityitems.NobilityItem;

/**
 * A resource Node in a Region
 */
public class Node {
	private String name;
	private int slots;
	private Map<NobilityItem, Integer> output;
	private NodeType type;
	private List<UUID> workers;
	private ConfigNode config;

	/**
	 * DON'T USE THIS CONSTRUCTOR! Create a node with {@link Region#makeNode(args...)}
	 */
	public Node(String name, int slots, NodeType type, Map<NobilityItem, Integer> output, List<UUID> workers, ConfigNode config) {
		this.name = name;
		this.slots = slots;
		this.output = output;
		this.type = type;
		this.workers = workers;
		this.config = config;
	}

	/**
	 * Deletes this Node from the config, use {@link Region#removeNode(String)} to properly delete a node.
	 */
	public void delete() {
		config.delete();
	}

	/**
	 * Adds a worker to this node
	 * 
	 * @param player Player to add
	 * @return false if there aren't enough slots to add a player
	 */
	public boolean addWorker(OfflinePlayer player) {
		if (workers.size() < slots) {
			workers.add(player.getUniqueId());
			config.setWorkers(workers).save();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes a worker from this node
	 * 
	 * @param player Player to remove
	 * @return false if the player does not have a worker on this node
	 */
	public boolean removeWorker(OfflinePlayer player) {
		if (workers.contains(player.getUniqueId())) {
			workers.remove(player.getUniqueId());
			config.setWorkers(workers).save();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the number of slots currently being used by workers on this node
	 * 
	 * @return int number of used slots
	 */
	public int getUsedSlots() {
		if (this.workers == null) {
			this.workers = new ArrayList<UUID>();
			return 0;
		} else {
			return this.workers.size();
		}
	}

	/**
	 * The name of this Node (displayed to players)
	 * 
	 * @return String name
	 */
	public String getName() {
		return new String(name);
	}

	/**
	 * The number of slots for workers in this Node
	 * 
	 * @return int slots
	 */
	public int getSlots() {
		return slots;
	}

	/**
	 * Gets a copy of the output of this node as a map of NobilityItems and Integers, 
	 * with the corresponding Integer being the amount of the NobilityItem resource
	 * 
	 * @return Map of NobilityItem to Integer output
	 */
	public Map<NobilityItem, Integer> getOutput() {
		return new HashMap<>(output);
	}

	/**
	 * The Type of this Node (used primarily for icons in GUIs and such)
	 * 
	 * @return NodeType
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * Gets a copy of the List of UUIDs which represents the workers on this node
	 * 
	 * @return List of UUID
	 */
	public List<UUID> getWorkers() {
		return new ArrayList<>(workers);
	}

	/**
	 * Sets the name of this Node and updates the config
	 * 
	 * @param name String name displayed to players
	 */
	public void setName(String name) {
		this.name = name;
		config.setName(name).save();
	}

	/**
	 * Sets the slots of this Node and updates the config.
	 * Note: Will not update the workers list! Will not remove
	 * workers if there are too many!
	 * 
	 * @param slots int number of workers allowed in this node
	 */
	public void setSlots(int slots) {
		this.slots = slots;
		config.setSlots(slots).save();
	}

	/**
	 * Sets the Output of this Node and updates the config -- Removes 
	 * all null values (Items not included that are currently in the 
	 * output will be retained)
	 * 
	 * @param output Map of NobilityItem to Integer output
	 */
	public void setOutput(Map<NobilityItem, Integer> output) {
		for (NobilityItem item : this.output.keySet()) {
			if (!output.containsKey(item)) {
				output.put(item, this.output.get(item));
			}
		}

		this.output = output;
		config.setOutput(output).save();

		Iterator<Map.Entry<NobilityItem, Integer>> iter = output.entrySet().iterator();
		while (iter.hasNext()) {
			if (iter.next().getValue() == null) {
				iter.remove();
			}
		}
	}

	/**
	 * Sets the Type (primarily used for GUIs and organization) of
	 * this Node
	 * 
	 * @param type NodeType
	 */
	public void setType(NodeType type) {
		this.type = type;
		config.setType(type).save();
	}

	/**
	 * Sets the workers working this Node (will not check with slots)!
	 * 
	 * @param workers List of UUID workers
	 */
	public void setWorkers(List<UUID> workers) {
		this.workers = workers;
		config.setWorkers(workers).save();
	}
}
