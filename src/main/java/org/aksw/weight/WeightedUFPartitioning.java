package org.aksw.weight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aksw.gpaba.Edge;
import org.aksw.gpaba.Graph;
import org.aksw.gpaba.Node;
import org.aksw.gpaba.Partition;
import org.aksw.gpaba.GeneticPartition;
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

		// transform 'graph' into 'g'
		Map<PTSPNode, Set<PTSPNode>> g = new HashMap<PTSPNode, Set<PTSPNode>>();

		HashMap<String, PTSPNode> nodesOut = new HashMap<>();
		for (Node nodeIn : graph.getNodes()) {
			PTSPNode nodeOut = new PTSPNode(String.valueOf(nodeIn.getId()),
					(int) nodeIn.getWeight());
			nodesOut.put(nodeOut.getLabel(), nodeOut);
		}

		for(Edge edgeIn : graph.getEdges()) {
			Node nodeIn1 = edgeIn.getNode1();
			Node nodeIn2 = edgeIn.getNode2();
			PTSPNode nodeOut1 = nodesOut.get(String.valueOf(nodeIn1.getId()));
			PTSPNode nodeOut2 = nodesOut.get(String.valueOf(nodeIn2.getId()));
			Set<PTSPNode> ranges;
			if(g.containsKey(nodeOut1))
				ranges = g.get(nodeOut1);
			else {
				ranges = new HashSet<PTSPNode>();
				g.put(nodeOut1, ranges);
			}
			ranges.add(nodeOut2);
		}

		// TODO control on the number of partitions (as input value)
		int threshold = 3;
		System.out.println("Input: " + g);
		System.out.println("Output: "
				+ WeightedUnionFind.partitions(g, threshold));
		Set<Set<String>> ps = WeightedUnionFind.partitions(g, threshold);
		for (Set<String> set : ps) {
			System.out.println(set);
		}

		// TODO transform Set<String> to Partition and output them
		
		return null;
	}

}
