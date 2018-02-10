package org.aksw.gpaba.growingPartitioning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.gpaba.Graph;
import org.aksw.gpaba.Node;
import org.aksw.gpaba.Partition;
import org.aksw.gpaba.Edge;
import org.aksw.gpaba.GeneticPartition;
import org.aksw.gpaba.Partitioning;

public class GrowingPartitioning extends Partitioning{

	HashMap<Integer,GrowingPartition> partitions = new HashMap<>();
	
	public GrowingPartitioning(Graph graph, int k) {
		super(graph, k);
	}

	@Override
	public String getName() {
		return "Growing Partitioning";
	}

	@Override
	public Set<Partition> compute() {
		
		//get list of all nodes Ids in the graph
		Set<Long> nodesIds = graph.getNodesIds();
		Map<Long,Boolean> nodeIsAssigned = new HashMap<>();
		
		for (Long node : nodesIds) {
			nodeIsAssigned.put(node, false);
		}
		//get random nodes Ids as seeds for k partition
		Set<Long> seeds = getRandomKSeeds(nodesIds,k);
		//convert set of seeds into list
		List<Long> seedsList = new ArrayList<>();
		seedsList.addAll(seeds);
		// for each partition assign a node as a seed  		
		for(int i=1;i<=k;i++)
		{
			partitions.put(i, new GrowingPartition(i));
			partitions.get(i).addToOnEdgeNodsNode(seedsList.get(i-1), graph.getNodeByID(seedsList.get(i-1)).getWeight());// add the node and pass its weight (got from teh graph) to be accumulated inside the partition
			nodeIsAssigned.put(seedsList.get(i-1), true);
		}
		System.out.println(this);
		//the size of nodes included in the partitions
		int currentNodesInPartitions=k;
		while(currentNodesInPartitions < graph.size())//do that till it is not over
		{
			for(int partitionId=1;partitionId<=k;partitionId++)//iterate over each partition
			{
				Map<Long,Double> newNodeWithItsInsideNode=getNewNodeToPartition(partitions.get(partitionId), nodeIsAssigned);
				partitions.get(partitionId).addToOnEdgeNodsNode(newNodeWithItsInsideNode.entrySet().iterator().next().getKey(), graph.getNodeByID(newNodeWithItsInsideNode.entrySet().iterator().next().getKey()).getWeight());// add the node and pass its weight (got from teh graph) to be accumulated inside the partition
				//nodeIsAssigned.put(newNodeWithItsInsideNode.entrySet().iterator().next().getKey(), true);
				Double insideNode =  newNodeWithItsInsideNode.entrySet().iterator().next().getValue();
				if(!checkIfNodeOnBorder(insideNode,partitions.get(partitionId),graph))
				{
					partitions.get(partitionId).getNodesOnEdge().remove(insideNode);
					partitions.get(partitionId).getNodesNotOnEdge().add(insideNode.longValue());
				}
				currentNodesInPartitions++;
			}
		}
		
		Set<Partition> parties = new HashSet<>();
		for (Partition p : partitions.values()) {
			parties.add(p);
		}
		return parties;
	}
	private boolean checkIfNodeOnBorder(double nodeId,GrowingPartition partition, Graph graph)
	{
		Set<Node> neightbors = graph.getNodeNeighbors((long) nodeId);
		for (Node neighborNode : neightbors) {
			if(!partition.getNodesOnEdge().contains(neighborNode.getId()) && !partition.getNodesNotOnEdge().contains(neighborNode.getId()))
				return true;
		}
		return false;
	}
	private Map<Long,Double> getNewNodeToPartition(GrowingPartition partition, Map<Long,Boolean> nodeIsAssigned)
	{	
		double maxWeight =-1;
		long maxNode= -1;
		long focusNode=-1;
		Map<Long,Double> candidateWithEdge=null;
		
		for (long node : partition.getNodesOnEdge()) {//iterate over each node on the edge of each partition
			candidateWithEdge = getCandidateNieghborOfNode(node,nodeIsAssigned);
			if(candidateWithEdge.entrySet().iterator().next().getValue() > maxWeight)
			{
				maxNode=(long)candidateWithEdge.entrySet().iterator().next().getKey();
				maxWeight=candidateWithEdge.entrySet().iterator().next().getValue();
				focusNode=node;
			}
			
		}
		candidateWithEdge=new HashMap<>();//reuse the variable to send back the max neighbor node and its corresponding node in the partition
		candidateWithEdge.put(maxNode, (double)focusNode);
		return candidateWithEdge;
	}
	private Map<Long,Double> getCandidateNieghborOfNode(long node, Map<Long,Boolean> nodeIsAssigned)
	{
		double maxEdgeWeight =-1;
		long maxEdgePairNode=-1;
		Map<Long,Double> candidateWithEdge = new HashMap<>();
		Map<Node, Edge> neighborsNodesWithEdges = graph.getNodeNeighborsWithCorrespondingEdge(node);
		for (Node nodeNeighbor : neighborsNodesWithEdges.keySet()) {
			if(!nodeIsAssigned.get(nodeNeighbor.getId()) && neighborsNodesWithEdges.get(nodeNeighbor).getWeight() > maxEdgeWeight)// the node is not included in an partition
			{
				maxEdgeWeight= neighborsNodesWithEdges.get(nodeNeighbor).getWeight();
				maxEdgePairNode = nodeNeighbor.getId();
			}
		}
		candidateWithEdge.put(maxEdgePairNode, maxEdgeWeight);
		return candidateWithEdge;//return the node with the max edge weight to the target node
	}
	
	private void expandParition(Map<Integer,GrowingPartition> partitions)
	{
		
	}
	private void createParitionAndSeeds()
	{
		
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Integer key : partitions.keySet()) {
			sb.append("Partition Id: ");
			sb.append(key);
			sb.append("\nInner Nodes: ");
			sb.append(partitions.get(key).getNodesNotOnEdge());
			sb.append("\nOuter Nodes: ");
			sb.append(partitions.get(key).getNodesOnEdge());
			sb.append("\n");
		}
		return sb.toString();
	}

	// it returns the ith , jth,.. nodes Ids (id values are different 
	//from the indices in case the ids are strings or any different formatted values
	private Set<Long> getRandomKSeeds(Set<Long> nodesIds, int numberOfSeeds)  
	{
		Set<Long> seeds = new HashSet<>();
		int graphsize =  nodesIds.size();
		List<Integer> randomIds = getRandomXValues(numberOfSeeds, graphsize);
		Collections.sort(randomIds);
		int randomIdsStep=0;
		int nodesIdsStep=0;
		for (Long nodeId : nodesIds) {
			if(seeds.size()==numberOfSeeds)
				break;
			if(nodesIdsStep == randomIds.get(randomIdsStep))
			{
				seeds.add(nodeId);
				randomIdsStep++; // go to next random Id that represents the index of the next node of the graph to pick
			}
			nodesIdsStep++;
		}

		return seeds;
	}
	public List<Integer> getRandomXValues(int numberOfRandomValues, int graphsize)
	{
		List<Integer> randomXValues = new ArrayList<>();
		Double randomNumber = Math.random();
		while(randomXValues.size() != numberOfRandomValues)
		{
			randomNumber = Math.random();
			Integer randomIndex = (int) (randomNumber *(graphsize-1));
			if(randomXValues.contains(randomIndex))
				continue;
			randomXValues.add(randomIndex);
		}
 		return randomXValues; 
	}

}
