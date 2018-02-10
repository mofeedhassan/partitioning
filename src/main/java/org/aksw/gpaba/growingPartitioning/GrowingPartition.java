package org.aksw.gpaba.growingPartitioning;

import java.util.HashSet;
import java.util.Set;

import org.aksw.gpaba.Partition;

public class GrowingPartition implements Partition {
	
	int partitionId;
	Set<Long> nodesNotOnEdge = null;
	Set<Long> nodesOnEDge = null;

	public GrowingPartition(int pid) {
		partitionId = pid;
		nodesNotOnEdge = new HashSet<>();
		nodesOnEDge = new HashSet<>();
	}

	public void addToOnEdgeNodsNode(long node) {
		nodesOnEDge.add(node);
	}

	public void addToNotOnEdgeNodsNode(long node) {
		nodesNotOnEdge.add(node);
	}

	public Set<Long> getNodesOnEdge() {
		return nodesOnEDge;
	}

	public Set<Long> getNodesNotOnEdge() {
		return nodesNotOnEdge;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(partitionId);
		sb.append("\nInner Nodes: ");
		for (Long key : nodesNotOnEdge) {
			sb.append(key + "-");
		}
		sb.append("\nOuter Nodes: ");
		for (Long key : nodesOnEDge) {
			sb.append(key + "-");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return nodesOnEDge.size() + nodesNotOnEdge.size();
	}

}
