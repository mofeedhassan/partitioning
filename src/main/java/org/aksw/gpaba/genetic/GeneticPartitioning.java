package org.aksw.gpaba.genetic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.aksw.gpaba.Edge;
import org.aksw.gpaba.Graph;
import org.aksw.gpaba.Node;
import org.aksw.gpaba.Partition;
import org.aksw.gpaba.Partitioning;
import org.aksw.gpaba.util.MapUtil;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GeneticPartitioning extends Partitioning {

	public static final int POP_SIZE = 10;
	public static final int EPOCHS = 1;

	public GeneticPartitioning(Graph graph, int k) {
		super(graph, k);
	}

	@Override
	public String getName() {
		return "Genetic Partitioning";
	}

	@Override
	public Set<Partition> compute() {

		// build pool
		Pool pool = new Pool(POP_SIZE, k, graph.getNodes().size());
		pool.create();

		Map<Individual, Integer> map = null;
		for (int i = 0; i < EPOCHS; i++) {
			map = fit(pool);
			for (Individual ind : map.keySet())
				System.out.println(ind);
		}
		
		// get best individual
		Individual best = map.keySet().iterator().next();
		
		// set up empty partitions
		HashMap<Character, Partition> parts = new HashMap<>();
		for (int i=0; i<k; i++)
			parts.put((char)('A' + i), new Partition());
		
		// assign nodes to partitions
		for (int i=0; i<best.getGenome().length(); i++) {
			// XXX get i-th node
			Node ith = getIthNode(i, graph.getNodes());
			parts.get(best.getGenome().charAt(i)).addNode( ith );
		}

		return new HashSet<>(parts.values());
	}

	private Node getIthNode(int i, Set<Node> nodes) {
		
		// TODO get node by ID!
		
		Iterator<Node> it = nodes.iterator();
		while (it.hasNext()) {
			Node node = it.next();
			if (node.getId() == i + 1) // XXX they start from 1
				return node;
		}
		
		System.out.println("!! " + i);
		return null;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Map<Individual, Integer> fit(Pool pool) {

		TreeMap<Individual, Integer> map = new TreeMap<>();

		// for each individual...
		for (Individual ind : pool.getIndividuals()) {
			String genome = ind.getGenome();
			int fitness = 0;
			// for each edge in the graph...
			for (Edge e : graph.getEdges()) {
				// get node1 and node2
				long node1id = e.getNode1().getId();
				long node2id = e.getNode2().getId();
				// if they belong to different partitions, count weight
				char part1 = genome.charAt((int)(node1id - 1)); // XXX they start from 1
				char part2 = genome.charAt((int)(node2id - 1)); // XXX they start from 1
				if (part1 != part2)
					fitness += e.getWeight();
			}
			ind.setFitness(fitness);
//			System.out.println(ind);
			map.put(ind, fitness);
		}
		
		// sort the map by value (asc)
		return MapUtil.sortByValue(map);
	}

}
