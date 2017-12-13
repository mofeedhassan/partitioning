package org.aksw.gpaba.genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

	public static final int POP_SIZE = 11;
	public static final int EPOCHS = 100; // [1, N]
	
	public static final double SELECTION = 0.9; // [0.0, 1.0]
	public static final double MUTATION = 0.1;

	public GeneticPartitioning(Graph graph, int k) {
		super(graph, k);
	}

	@Override
	public String getName() {
		return "Genetic Partitioning";
	}

	@Override
	public Set<Partition> compute() {

		PrintWriter bestFitnessWriter = null;
		try {
			bestFitnessWriter = new PrintWriter(new File("best_fitness.tsv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// build pool
		Pool pool = new Pool(POP_SIZE, k, graph.getNodes().size());
		pool.create();
		Map<Individual, Integer> map = fit(pool);
		print(map, 1);
		bestFitnessWriter.println("1\t" + getBestIndividual(map).getFitness() + "\t" +
				getAverageFitness(map));
		
		
		// apply genetic operators
		for (int epoch = 2; epoch <= EPOCHS; epoch++) {
			
			// natural selection = best population survives
			naturalSelection(pool);
			
			// newborns
			int newborns = pool.getSize() - pool.getIndividuals().size();
			System.out.println("newborns: " + newborns);
			
			// for each newborn
			for (int n=0; n<newborns; n++) {
				
				// crossover
				int randCut = (int)(pool.getNumGenes() * Math.random());
				Individual parent1 = pool.getIndividuals().get(2 * n);
				Individual parent2 = pool.getIndividuals().get(2 * n + 1);
				String genome = parent1.getGenome().substring(0, randCut) +
						parent2.getGenome().substring(randCut);
				
				// mutation
				if(Math.random() < MUTATION) {
					int randGene = (int)(pool.getNumGenes() * Math.random());
					int randType = (int)(pool.getK() * Math.random());
					char newGene = (char)(randType + 'A');
//					System.out.println(genome);
//					System.out.println("mutating gene "+randGene+" from "+genome.charAt(randGene) + 
//							" to " + newGene);
					genome = genome.substring(0, randGene) + newGene + genome.substring(randGene + 1);
//					System.out.println(genome);
				}
				
				if(!pool.validate(genome)) {
					n--;
					continue;
				}
				
				Individual ind = new Individual(genome);
				pool.addIndividual(ind);
			}

			map = fit(pool);
			print(map, epoch);
			bestFitnessWriter.println(epoch + "\t" + getBestIndividual(map).getFitness() + "\t" +
					getAverageFitness(map));
			
		}
		
		// get best individual
		Individual best = getBestIndividual(map);
		
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
		
		bestFitnessWriter.close();

		return new HashSet<>(parts.values());
	}
	
	private double getAverageFitness(Map<Individual, Integer> map) {
		double avg = 0.0;
		for(Integer v : map.values())
			avg += v;
		return avg / map.size();
	}

	private Individual getBestIndividual(Map<Individual, Integer> map) {
		return map.keySet().iterator().next();
	}

	private void naturalSelection(Pool pool) {
		int quantile = 0;
		Set<Individual> dying = new HashSet<>();
		for(Individual ind : sort(pool).keySet()) {
//			System.out.println(quantile + " >= " + SELECTION * pool.getSize());
			if(quantile >= SELECTION * pool.getSize()) {
				System.out.println("removing: "+ind);
				dying.add(ind);
			}
			quantile++;
		}
		for(Individual ind : dying)
			pool.removeIndividual(ind);
	}

	private void print(Map<Individual, Integer> map, int epoch) {
		System.out.println("=== EPOCH: " + epoch + " ===");
		System.out.println("iterating on " + map.size() + " individuals");
		for (Individual ind : map.keySet())
			System.out.println(ind);
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
	
	public Map<Individual, Integer> sort(Pool pool) {

		TreeMap<Individual, Integer> map = new TreeMap<>();

		// for each individual...
		for (Individual ind : pool.getIndividuals()) {
			map.put(ind, ind.getFitness());
		}
		
		// sort the map by value (asc)
		return MapUtil.sortByValue(map);
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
