package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Node {
	public String name;
	public int slots;
	public ArrayList<ItemStack> output;
	public NodeType type;
	public ArrayList<UUID> workers;

	public Node(String name, int slots) {
		this.name = name;
		this.slots = slots;

		output = new ArrayList<ItemStack>();
		output.add(new ItemStack(Material.STONE));

		workers = new ArrayList<UUID>();
	}

	public boolean addWorker(Player p) {
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
}
