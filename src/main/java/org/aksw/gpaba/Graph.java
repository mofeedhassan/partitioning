package org.aksw.gpaba;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Graph {
	
	private HashMap<Long, Node> nodesMap;
	private Set<Node> nodes;
	private Set<Edge> edges;
	
	public Graph(Set<Node> nodes, Set<Edge> edges) {
		this.setNodes(nodes);
		this.setEdges(edges);
		nodesMap = new HashMap<>();
		for(Node n : nodes)
			nodesMap.put(n.getId(), n);
	}
	
	public Node getNodeByID(Long id) {
		return nodesMap.get(id);
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
	public Set<Long> getNodesIds() {
		return nodesMap.keySet();
	}
	
	public Set<Node> getNodeNeighbors(long nodeId)
	{
		Set<Node> neighbors= new HashSet<>();
		Node  node = nodesMap.get(nodeId);
		/*if(null == node.getEdges())/// the node is isolated and has no neighbors
			return null;*/
		for (Edge edge : node.getEdges()) {
			if(edge.getNode1().getId() != nodeId)//get the other node of the edge that is neighbor to the node on the focus
				neighbors.add(edge.getNode1());
			else if(edge.getNode2().getId() != nodeId)
				neighbors.add(edge.getNode2());
		}
		return neighbors;
	}
	
	public Map<Node,Edge> getNodeNeighborsWithCorrespondingEdge(long nodeId)
	{
		Map<Node,Edge> neighbors= new HashMap<>();
		Node  node = nodesMap.get(nodeId);
		for (Edge edge : node.getEdges()) {
			if(edge.getNode1().getId() != nodeId)//get the other node of the edge that is neighbor to the node on the focus
				neighbors.put(edge.getNode1(),edge);
			else if(edge.getNode2().getId() != nodeId)
				neighbors.put(edge.getNode2(),edge);
		}
		return neighbors;
	}
	
	public int size()
	{
		return nodesMap.keySet().size();
	}
	private void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + "]";
	}
	
	
	
}
