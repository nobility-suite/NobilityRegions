package io.github.kingvictoria;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum RegionResource {
	WOOD(new ItemStack(Material.OAK_LOG)), 
	STONE(new ItemStack(Material.SMOOTH_STONE)),
	IRON(new ItemStack(Material.IRON_INGOT)), 
	WHEAT(new ItemStack(Material.WHEAT));
	
	private ItemStack resource;
	
	RegionResource (ItemStack resource) {
		this.resource = resource;
	}

	public ItemStack resource() {
		return resource;
	}
	
	public static RegionResource getResource(String resourceString) {
		for (RegionResource resource : RegionResource.values()) {
			if (resourceString.equalsIgnoreCase(resource.toString())) {
				return resource;
			}
		}
		Bukkit.getLogger().warning("The string \"" + resourceString + "\" does not correspond to any RegionResource" );
		return null;
	}

}
