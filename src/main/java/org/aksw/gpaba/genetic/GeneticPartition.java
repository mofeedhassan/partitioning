package org.aksw.gpaba.genetic;

import java.util.HashSet;
import java.util.Set;

import org.aksw.gpaba.Node;
import org.aksw.gpaba.Partition;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GeneticPartition implements Partition {
	
	private Set<Node> nodes;
	private int weight = 0; 

	public Set<Node> getNodes() {
		return nodes;
	}

	public GeneticPartition() {
		super();
		this.nodes = new HashSet<>();
	}
	
	public void addNode(Node n) {
		nodes.add(n);
		weight += n.getWeight();
	}

	@Override
	public String toString() {
		return "Partition [nodes=" + nodes + "]";
	}

	@Override
	public int getNumberOfNodes() {
		return nodes.size();
	}

	@Override
	public int getSumOfNodesWeights() {
		return weight;
	}
	
	
}
