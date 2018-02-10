package org.aksw.gpaba.growingPartitioning;

import java.util.HashSet;
import java.util.Set;

import org.aksw.gpaba.Partition;

public class GrowingPartition implements Partition {
	
	int partitionId;
	double totalSize=0;
	Set<Long> nodesNotOnEdge = null;
	Set<Long> nodesOnEDge = null;

	public GrowingPartition(int pid) {
		partitionId = pid;
		nodesNotOnEdge = new HashSet<>();
		nodesOnEDge = new HashSet<>();
	}

	public void addToOnEdgeNodsNode(long node, double nodeWeight) {
		nodesOnEDge.add(node);
		totalSize+=nodeWeight;
	}

	public void addToNotOnEdgeNodsNode(long node, double nodeWeight) {
		nodesNotOnEdge.add(node);
		totalSize+=nodeWeight;
	}

	public Set<Long> getNodesOnEdge() {
		return nodesOnEDge;
	}

	public Set<Long> getNodesNotOnEdge() {
		return nodesNotOnEdge;
	}
	public Set<Long> getNodes(){
		Set<Long> tmp = new HashSet<>(nodesOnEDge);
		tmp.addAll(nodesNotOnEdge);
		return new HashSet<Long>(tmp);
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
	public int getNumberOfNodes() {
		// TODO Auto-generated method stub
		return nodesOnEDge.size() + nodesNotOnEdge.size();
	}
	
	@Override
	public int getSumOfNodesWeights() {
		return (int)totalSize; //won't affect thing as weight all are in integer
	}
	
}
