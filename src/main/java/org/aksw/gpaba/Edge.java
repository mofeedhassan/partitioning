package org.aksw.gpaba;


/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Edge {

	private long id;
	private double weight;
	
	private Node node1, node2;

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

	public Edge(long id, double weight, Node node1, Node node2) {
		super();
		this.id = id;
		this.weight = weight;
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Node getNode1() {
		return node1;
	}

	public Node getNode2() {
		return node2;
	}

	public Node getPairedNode(Node node) {
		if(node == node1)
			return node2;
		if(node == node2)
			return node1;
		return null;
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + ", weight=" + weight + ", node1=" + node1
				+ ", node2=" + node2 + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
