package org.aksw.weight;

import java.util.Set;

import org.aksw.gpaba.Graph;
import org.aksw.gpaba.Partition;
import org.aksw.gpaba.Partitioning;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class WeightedUFPartitioning extends Partitioning {

	public WeightedUFPartitioning(Graph graph, int k) {
		super(graph, k);
	}

	@Override
	public String getName() {
		return "Weighted UnionFind Partitioning";
	}

	@Override
	public Set<Partition> compute() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
