package org.aksw.gpaba;

import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public abstract class Partitioning {
	
	protected Graph graph;
	protected int k;

	public Partitioning(Graph graph, int k) {
		this.graph = graph;
		this.k = k;
		System.out.println("Started: " + this.getName());
	}

	public abstract String getName();
	
	public abstract Set<Partition> compute();
	
	public Graph getGraph() {
		return graph;
	}
	
	public int getK() {
		return k;
	}


}
