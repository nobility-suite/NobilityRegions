package io.github.kingvictoria.nodes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.Region;

public class Node {
	public String name;
	public Region parent;
	int slots;
	ArrayList<ItemStack> output;
	
	public Node(String name, Region parent, int slots) {
		this.name = name;
		this.parent = parent;
		this.slots = slots;
		
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		output.add(new ItemStack(Material.STONE));
	}

}
