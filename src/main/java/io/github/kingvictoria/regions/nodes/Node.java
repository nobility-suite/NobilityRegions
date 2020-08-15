package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Node {
	private String name;
	private int slots;
	private List<ItemStack> output;
	private NodeType type;
	private List<UUID> workers;

	public Node(String name, int slots, NodeType type, List<ItemStack> output, List<UUID> workers) {
		this.name = name;
		this.slots = slots;
		this.output = output;
		this.type = type;
		this.workers = workers;
	}

	public boolean addWorker(Player p) {
		// TODO: Configs

		if (workers.size() < slots) {
			workers.add(p.getUniqueId());
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
		// TODO: Configs
	}

	public void setSlots(int slots) {
		this.slots = slots;
		// TODO: Configs
	}

	public void setOutput(List<ItemStack> output) {
		this.output = output;
		// TODO: Configss
	}

	public void setType(NodeType type) {
		this.type = type;
		// TODO: Configs
	}

	public void setWorkers(List<UUID> workers) {
		this.workers = workers;
		// TODO: Configs
	}
}
