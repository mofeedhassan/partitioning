package org.aksw.gpaba;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GeneticPartition implements Partition {
	
	private Set<Node> nodes;

	public Set<Node> getNodes() {
		return nodes;
	}

	public GeneticPartition() {
		super();
		this.nodes = new HashSet<>();
	}
	
	public void addNode(Node n) {
		nodes.add(n);
	}

	@Override
	public String toString() {
		return "Partition [nodes=" + nodes + "]";
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return nodes.size();
	}
	
	
}
