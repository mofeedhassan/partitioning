package org.aksw.gpaba;

import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Graph {
	
	private Set<Node> nodes;
	private Set<Edge> edges;
	
	public Graph(Set<Node> nodes, Set<Edge> edges) {
		this.setNodes(nodes);
		this.setEdges(edges);
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	private void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	private void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + "]";
	}
	
	
	
}
