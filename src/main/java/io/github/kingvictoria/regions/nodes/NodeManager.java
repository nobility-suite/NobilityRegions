package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;

public class NodeManager {

	HashMap<Region, ArrayList<Node>> nodeMap;

	public NodeManager() {
		nodeMap = new HashMap<Region, ArrayList<Node>>();

		ArrayList<Node> temp = new ArrayList<Node>();

		for (Region r : NobilityRegions.getRegionManager().getRegions()) {
			nodeMap.put(r, temp);
		}

		// Sample nodes for testing

		Node mine = new Node("Lorafaul Mines", 3);
		mine.type = NodeType.MINE;
		ArrayList<ItemStack> mats = new ArrayList<ItemStack>();
		mats.add(new ItemStack(Material.IRON_ORE, 25));
		mats.add(new ItemStack(Material.STONE, 60));
		mine.output = new ArrayList<ItemStack>(mats);

		mats.clear();
		Node forest = new Node("The Weeping Willows", 2);
		forest.type = NodeType.FOREST;
		mats.add(new ItemStack(Material.DARK_OAK_LOG, 30));
		mats.add(new ItemStack(Material.DARK_OAK_PLANKS, 50));
		forest.output = new ArrayList<ItemStack>(mats);

		mats.clear();
		Node farm = new Node("Verdant Valley", 5);
		farm.type = NodeType.FARM;
		mats.add(new ItemStack(Material.CARROT, 50));
		mats.add(new ItemStack(Material.HAY_BLOCK, 10));
		farm.output = new ArrayList<ItemStack>(mats);

		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(farm);
		nodes.add(forest);
		nodes.add(mine);
		Region test = NobilityRegions.getRegionManager().getRegion(Bukkit.getWorld("world"), Biome.MOUNTAINS);
		nodeMap.put(test, nodes);

	}

	public ArrayList<Node> getNodes(Region r) {
		return nodeMap.get(r);
	}

	public void addNode(Region r, Node n) {
		ArrayList<Node> temp = nodeMap.get(r);
		temp.add(n);
		nodeMap.put(r, temp);
	}

}
