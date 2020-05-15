package io.github.kingvictoria.nodes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.Region;

public class Node {
	public String name;
	public int slots;
	public ArrayList<ItemStack> output;
	public NodeType type;
	
	public Node(String name, int slots) {
		this.name = name;
		this.slots = slots;
		
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		output.add(new ItemStack(Material.STONE));
	}

}
