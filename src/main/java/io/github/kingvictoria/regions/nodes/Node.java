package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.Configs.ConfigRegion.ConfigNode;

public class Node {
	/** Replaced by {@link #getName()} and {@link #setName(String)} */
	@Deprecated
	public String name;
	/** Replaced by {@link #getSlots()} and {@link #setSlots(int)} */
	@Deprecated
	public int slots;
	/** Replaced by {@link #getOutput()} and {@link #setOutput(List)} */
	@Deprecated
	public List<ItemStack> output;
	/** Replaced by {@link #getType()} and {@link #setType(NodeType)} */
	@Deprecated
	public NodeType type;
	/** Replaced by {@link #getWorkers()} and {@link #setWorkers(List)}. Alternatively use {@link #addWorker(Player)} */
	@Deprecated
	public List<UUID> workers;
	private ConfigNode config;

	public Node(String name, int slots, NodeType type, List<ItemStack> output, List<UUID> workers, ConfigNode config) {
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

	public List<ItemStack> getOutput() {
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

	public void setOutput(List<ItemStack> output) {
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
