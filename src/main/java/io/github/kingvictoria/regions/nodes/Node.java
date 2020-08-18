package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.github.kingvictoria.Configs.ConfigRegion.ConfigNode;
import net.civex4.nobilityitems.NobilityItem;

public class Node {
	private String name;
	private int slots;
	private Map<NobilityItem, Integer> output;
	private NodeType type;
	private List<UUID> workers;
	private ConfigNode config;

	public Node(String name, int slots, NodeType type, Map<NobilityItem, Integer> output, List<UUID> workers, ConfigNode config) {
		this.name = name;
		this.slots = slots;
		this.output = output;
		this.type = type;
		this.workers = workers;
		this.config = config;
	}

	public boolean addWorker(Player p) {
		if (workers.size() < slots) {
			workers.add(p.getUniqueId());
			config.setWorkers(workers).save();
			return true;
		} else {
			return false;
		}
	}

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
