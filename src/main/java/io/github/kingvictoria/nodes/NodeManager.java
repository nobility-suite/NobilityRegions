package io.github.kingvictoria.nodes;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.kingvictoria.NobilityRegions;
import io.github.kingvictoria.Region;

public class NodeManager {
	
	HashMap<Region, ArrayList<Node>> nodeMap;
	
	public NodeManager() {
		nodeMap = new HashMap<Region, ArrayList<Node>>();
		
		ArrayList<Node> temp = new ArrayList<Node>();
		
		for(Region r : NobilityRegions.getRegionMaster().getRegions()) {
			nodeMap.put(r,temp);
		}
	}
	
	public ArrayList<Node> getNodes(Region r){
		return nodeMap.get(r);
	}
	
	public void addNode(Region r, Node n) {
		ArrayList<Node> temp = nodeMap.get(r);
		temp.add(n);
		nodeMap.put(r, temp);
	}

}
