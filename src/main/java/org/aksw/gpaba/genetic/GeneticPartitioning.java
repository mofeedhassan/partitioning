package org.aksw.gpaba.genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aksw.gpaba.Edge;
import org.aksw.gpaba.Graph;
import org.aksw.gpaba.Node;
import org.aksw.gpaba.Partition;
import org.aksw.gpaba.GeneticPartition;
import org.aksw.gpaba.Partitioning;
import org.aksw.gpaba.util.MapUtil;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GeneticPartitioning extends Partitioning {

	static Logger log = Logger.getLogger("gpaba");
	
	public static final int POP_SIZE = 50;
	public static final int EPOCHS = 100; // [1, N]
	
	public static final double SELECTION = 0.8; // [0.0, 1.0]
	public static final double MUTATION = 0.1;
	
	// for fitness estimation
	private double costFitExpValue = Double.NaN;
	private double balanceFitExpValue = Double.NaN;

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
		pool.create();//create set/pool of individuals
		Map<Individual, Double> map = fit(pool);//calculate for the individuals their min-cut cost,, balance cost then fitness values, 
		print(map, 1);
		bestFitnessWriter.println("1\t" + getBestIndividual(map).getFitness() + "\t" +
				getAverageFitness(map));
		
		
		// apply genetic operators
		for (int epoch = 2; epoch <= EPOCHS; epoch++) {//epoch = generations level
			
			// natural selection = best population survives
			naturalSelection(pool);
			
			// newborns
			int newborns = pool.getSize() - pool.getIndividuals().size();//number of empt places in the pool to get new born genes
			log.log(Level.FINE, "newborns: " + newborns);
			
			// for each newborn
			for (int n=0; n<newborns; n++) {
				
				// crossover (new born is created from mix between two parents gene string)
				int randCut = (int)(pool.getNumGenes() * Math.random());
				Individual parent1 = pool.getIndividuals().get(2 * n);
				Individual parent2 = pool.getIndividuals().get(2 * n + 1);
				String genome = parent1.getGenome().substring(0, randCut) +
						parent2.getGenome().substring(randCut);
				
				// mutation
				if(Math.random() < MUTATION) {//based on randomization, will the new born be mutated or not
					int randGene = (int)(pool.getNumGenes() * Math.random());
					int randType = (int)(pool.getK() * Math.random());
					char newGene = (char)(randType + 'A');
					log.log(Level.FINEST, "Mutation, before: " + genome);
					log.log(Level.FINE, "mutating gene "+randGene+" from "+genome.charAt(randGene) + 
							" to " + newGene);
					genome = genome.substring(0, randGene) + newGene + genome.substring(randGene + 1);
					log.log(Level.FINEST, "Mutation, after: " + genome);
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
		
		// get best individual(best of all ages)
		Individual best = getBestIndividual(map);
		
		// set up empty partitions
		HashMap<Character, Partition> parts = new HashMap<>();
		for (int i=0; i<k; i++)
			parts.put((char)('A' + i), new GeneticPartition());
		
		// assign nodes to partitions
		for (int i=0; i<best.getGenome().length(); i++) {
//			Node ith = getIthNode(i, graph.getNodes());
			Node ith = graph.getNodeByID((long)(i + 1)); // they start from 1
			((GeneticPartition) parts.get(best.getGenome().charAt(i))).addNode( ith );
		}
		
		bestFitnessWriter.close();

		return new HashSet<>(parts.values());
	}
	
	private double getAverageFitness(Map<Individual, Double> map) {
		double avg = 0.0;
		for(Double v : map.values())
			avg += v;
		return avg / map.size();
	}

	private Individual getBestIndividual(Map<Individual, Double> map) {
		return map.keySet().iterator().next();
	}

	private void naturalSelection(Pool pool) {//kill ind having less than x fitness
		int quantile = 0;
		Set<Individual> dying = new HashSet<>();
		for(Individual ind : sort(pool).keySet()) {
			log.log(Level.FINEST, quantile + " >= " + SELECTION * pool.getSize());
			if(quantile >= SELECTION * pool.getSize()) {
				log.log(Level.FINE, "removing: "+ind);
				dying.add(ind);
			}
			quantile++;
		}
		for(Individual ind : dying)
			pool.removeIndividual(ind);
	}

	private void print(Map<Individual, Double> map, int epoch) {
		log.log(Level.FINE, "=== EPOCH: " + epoch + " ===");
		log.log(Level.FINE, "iterating on " + map.size() + " individuals");
		for (Individual ind : map.keySet())
			log.log(Level.FINE, ind.toString());
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public Map<Individual, Double> sort(Pool pool) {

		TreeMap<Individual, Double> map = new TreeMap<>();

		// for each individual...
		for (Individual ind : pool.getIndividuals()) {
			map.put(ind, ind.getFitness());
		}
		
		// sort the map by value (asc)
		return MapUtil.sortByValue(map);
	}


	public Map<Individual, Double> fit(Pool pool) {

		TreeMap<Individual, Double> map = new TreeMap<>();
		StandardDeviation stdev = new StandardDeviation();
		Mean mean = new Mean();

		ArrayList<Double> costFits = new ArrayList<>();
		ArrayList<Double> balanceFits = new ArrayList<>();
		
		// for each individual...
		for (Individual ind : pool.getIndividuals()) {
			String genome = ind.getGenome();
			double costFitness = 0;
			
			// compute edge-related fitness(min-cut)
			for (Edge e : graph.getEdges()) {
				// get node1 and node2
				long node1id = e.getNode1().getId();
				long node2id = e.getNode2().getId();
				// if they belong to different partitions, count weight
				char part1 = genome.charAt((int)(node1id - 1)); // IDs start from 1
				char part2 = genome.charAt((int)(node2id - 1)); // IDs start from 1
				if (part1 != part2)
					costFitness += e.getWeight();
			}
			costFits.add(costFitness);//cost fitness is the total weight between each partition (cut total cost)
			ind.setCostFitness(costFitness);
			
			// compute node-related fitness (balancing)
			double[] partWeights = new double[k];
			for(int i=0; i<genome.length(); i++) {
				// get partition number of the ith node 
				int part = (int)(genome.charAt(i) - 'A');
				partWeights[part] += graph.getNodeByID((long)(i + 1)).getWeight();
			}
			for(double w : partWeights) {
				log.log(Level.FINE, "Weight: "+w);
			}
			// graph balance is inv.prop. to partition weight variance
			double balanceFitness = stdev.evaluate(partWeights);
			balanceFits.add(balanceFitness);
			ind.setBalanceFitness(balanceFitness);
			log.log(Level.FINE, String.valueOf(balanceFitness));
			log.log(Level.FINE, "--");
			
		}
		//get average min-cut cost
		if(Double.isNaN(costFitExpValue)) {
			costFitExpValue = mean.evaluate(toDouble(costFits));
			log.log(Level.FINE, "costFitExpValue="+costFitExpValue);
		}
		if(Double.isNaN(balanceFitExpValue)) {//get averae balance cost
			balanceFitExpValue = mean.evaluate(toDouble(balanceFits));
			log.log(Level.FINE, "balanceFitExpValue="+balanceFitExpValue);
		}
		//normalize
		for(Individual ind : pool.getIndividuals()) {
			ind.setCostFitness(ind.getCostFitness() / costFitExpValue);//in both setcost and balance the fitness is updated
			ind.setBalanceFitness(ind.getBalanceFitness() / balanceFitExpValue);
			map.put(ind, ind.getFitness());
		}

		// sort the map by value (asc)
		return MapUtil.sortByValue(map);
	}

	private double[] toDouble(List<Double> doubles) {
		 double[] target = new double[doubles.size()];
		 for (int i = 0; i < target.length; i++) {
		    target[i] = doubles.get(i).doubleValue();  // java 1.4 style
		    // or:
		    target[i] = doubles.get(i);                // java 1.5+ style (outboxing)
		 }
		 return target;
	}
}
