package io.github.kingvictoria.regions.nodes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.regions.Region;

/**
 * Old NodeManager (Nodes are now handled within the Region object).
 */
@Deprecated
public class NodeManager {

	/**
	 * Generates Lorafaul Mines, The Weeping Willows, and Verdant Valley Nodes for the world-MOUNTAINS Region.
	 * 
	 * Will be replaced with a default config.
	 */
	@Deprecated
	public static void generateSampleNodes() {
		// Sample nodes for testing

		ArrayList<ItemStack> mats = new ArrayList<ItemStack>();
		mats.add(new ItemStack(Material.IRON_ORE, 25));
		mats.add(new ItemStack(Material.STONE, 60));
		Node mine = new Node("Lorafaul Mines", 3, NodeType.MINE, new ArrayList<>(mats), new ArrayList<>());

		mats.clear();
		mats.add(new ItemStack(Material.DARK_OAK_LOG, 30));
		mats.add(new ItemStack(Material.DARK_OAK_PLANKS, 50));
		Node forest = new Node("The Weeping Willows", 2, NodeType.FOREST, new ArrayList<>(mats), new ArrayList<>());

		mats.clear();
		mats.add(new ItemStack(Material.CARROT, 50));
		mats.add(new ItemStack(Material.HAY_BLOCK, 10));
		Node farm = new Node("Verdant Valley", 5, NodeType.FARM, new ArrayList<>(mats), new ArrayList<>());

		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(farm);
		nodes.add(forest);
		nodes.add(mine);
		Region test = NobilityRegions.getRegionManager().getRegion(Bukkit.getWorld("world"), Biome.MOUNTAINS);

		for (Node node: nodes) test.addNode(node);
	}

	/**
	 * Gets the Nodes from a Region.
	 * 
	 * Replaced by {@link Region#getNodes()}.
	 * 
	 * @param region Region to get the Nodes from
	 * @return
	 */
	@Deprecated
	public ArrayList<Node> getNodes(Region region) {
		return (ArrayList<Node>) region.getNodes();
	}

	/**
	 * Adds a node to a Region.
	 * 
	 * Replaced by {@link Region#addNode(Node)}.
	 * 
	 * @param region Region to add the Node to
	 * @param node Node to add to the Region
	 */
	@Deprecated
	public void addNode(Region region, Node node) {
		region.addNode(node);
	}

}
