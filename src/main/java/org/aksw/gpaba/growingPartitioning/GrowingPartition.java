package org.aksw.gpaba.growingPartitioning;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aksw.gpaba.Partition;

public class GrowingPartition implements Partition {
	static Logger log = Logger.getLogger("gpaba");

	public enum POSITION{
		INNER,OUTER
	}
	int partitionId;
	double totalSize=0;
	Set<Long> nodesNotOnEdge = null;
	Set<Long> nodesOnEDge = null;

	public GrowingPartition(int pid) {
		partitionId = pid;
		nodesNotOnEdge = new HashSet<>();
		nodesOnEDge = new HashSet<>();
	}

	public void addNodeToPartition(long node, double nodeWeight, POSITION nodePosition)
	{
		if(nodePosition.equals(POSITION.OUTER))
			addToOnEdgeNodsNode(node, nodeWeight);
		else if(nodePosition.equals(POSITION.INNER))
			addToNotOnEdgeNodsNode(node, nodeWeight);
		else
			log.log(Level.SEVERE, "ERROR: Wrong positin to add the node");
	}
	
	public void removeNodeFromPartition(long node, double nodeWeight)
	{
		if(nodesOnEDge.contains(node)){
			nodesOnEDge.remove(node);
			totalSize-=nodeWeight;
		}else if(nodesNotOnEdge.contains(node)){
			nodesNotOnEdge.remove(node);
			totalSize-=nodeWeight;
		}
		else
			log.log(Level.SEVERE, "ERROR: No node exists with this id");
	}
	private void addToOnEdgeNodsNode(long node, double nodeWeight) {
		nodesOnEDge.add(node);
		totalSize+=nodeWeight;
	}

	private void addToNotOnEdgeNodsNode(long node, double nodeWeight) {
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
