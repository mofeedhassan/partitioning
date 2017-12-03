package org.aksw.gpaba;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Node {

	private long id;
	private double weight;
	
	private Set<Edge> edges;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Node(long id, double weight) {
		super();
		this.id = id;
		this.weight = weight;
		this.edges = new HashSet<>();
	}
	
	public Set<Edge> getEdges() {
		return edges;
	}
	
	public Node followEdge(Edge e) {
		return e.getPairedNode(this);
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", weight=" + weight + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
