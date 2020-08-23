package io.github.kingvictoria.regions.nodes;

import org.bukkit.Material;

/**
 * The Types of Nodes (used for icons, organization, and to help 
 * players understand at a glance what a Node's resources might be)
 * 
 * Note: In the future we'll want to match these with custom textures
 */
public enum NodeType {
	MINE("Mine", Material.IRON_ORE), 
	FARM("Farm", Material.HAY_BLOCK), 
	FOREST("Forest", Material.OAK_LOG);

	/** A player-friendly name for this NodeType */
	public final String displayName;
	/** The GUI icon of this NodeType */
	public final Material icon;

	NodeType(String displayName, Material icon) {
		this.displayName = displayName;
		this.icon = icon;
	}
}
