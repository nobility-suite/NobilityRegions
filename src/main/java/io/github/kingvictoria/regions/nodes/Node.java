package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

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
	 * Adds a worker to this node
	 * 
	 * @param player Player to add
	 * @return false if there aren't enough slots to add a player
	 */
	public boolean addWorker(Player player) {
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
	public boolean removeWorker(Player player) {
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

	public String getName() {
		return name;
	}

	public int getSlots() {
		return slots;
	}

	/**
	 * Gets the output of this node as a map of NobilityItems and Integers, with the
	 * corresponding Integer being the amount of the NobilityItem resource
	 * 
	 * @return Map of NobilityItem to Integer output
	 */
	public Map<NobilityItem, Integer> getOutput() {
		return output;
	}

	public NodeType getType() {
		return type;
	}

	public List<UUID> getWorkers() {
		return workers;
	}

	public void setName(String name) {
		this.name = name;
		config.setName(name).save();
	}

	public void setSlots(int slots) {
		this.slots = slots;
		config.setSlots(slots).save();
	}

	public void setOutput(Map<NobilityItem, Integer> output) {
		this.output = output;
		config.setOutput(output).save();
	}

	public void setType(NodeType type) {
		this.type = type;
		config.setType(type).save();
	}

	public void setWorkers(List<UUID> workers) {
		this.workers = workers;
		config.setWorkers(workers).save();
	}
}
