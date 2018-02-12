package org.aksw.gpaba;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aksw.gpaba.genetic.GeneticPartitioning;
import org.aksw.gpaba.growingPartitioning.GrowingPartition;
import org.aksw.gpaba.growingPartitioning.GrowingPartitioning;
import org.aksw.weight.WeightedUFPartitioning;

/**
 * GPABA = Graph Partitioning and Balancing Algorithm.
 * 
 * @author Mofeed Hassan {@literal mounir@informatik.uni-leipzig.de}
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 * @author Andre Valdestilhas {@literal valdestilhas@informatik.uni-leipzig.de}
 *
 */
public class GPABAMain {
	static Logger log = Logger.getLogger("gpaba");
	public enum Algorithm{
		GENETIC,GROWING,UNIONFIND
	}
	static Algorithm algorithm1 = null;
	static String graphFile ="";
	static String partitionsInfoFile="";
	
	private static void loadParamaeters(String[] args)
	{
		algorithm1 =Algorithm.valueOf(args[0]);
		graphFile = args[1];
		partitionsInfoFile = args[2];
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Algorithm algorithm = Algorithm.GROWING;
		String file = "graph10k";
		log.log(Level.INFO, "The partition algorithm is {0}",algorithm);
		runAlgorithm(algorithm, file);

	}
	public static void runAlgorithm(Algorithm algorithm, String file)
	{
		int k = 4;
		Graph g=loadGraph(file);
		if(g!=null && k <= g.size())
		{
			System.out.println(g);
			log.log(Level.INFO, "The graph size = {0}",g.size());
			log.log(Level.INFO, "The required partitions = {0}",k);

			Partitioning gp = null;
			switch(algorithm)
			{
			case GROWING:gp = new GrowingPartitioning (g, k);
						break;
			case GENETIC:gp = new GeneticPartitioning (g, k);
						break;
			case UNIONFIND:gp = new WeightedUFPartitioning (g, k);
						break;
			default: System.out.println("Algorithm is not available");
				
			}
			long startTime = System.currentTimeMillis();
			log.log(Level.INFO, "The start time = {0}",startTime);
			
			Set<Partition> parts = gp.compute();
			
			log.log(Level.INFO, "The end time = {0}",System.currentTimeMillis());
			log.log(Level.INFO, "The elapsed time = {0} milliseconds", (System.currentTimeMillis() - startTime));
			log.log(Level.INFO, "The Number of nodes per partition ");
			int i=1;
			for (Partition partition : parts) {
				log.log(Level.INFO, "The partition number "+(i++)+" has {0} Nodes and weighs {1}",
						new Object[]{partition.getNumberOfNodes(), partition.getSumOfNodesWeights()});
			}
			// TODO write total cost
			
		}
	}
/*	public static void testGrowing()
	{
		int k = 2;
		Graph g=loadGraph();
		if(g!=null && k <= g.size())
		{
			System.out.println(g);
			Partitioning gp = new GrowingPartitioning (g, k);
			Set<Partition> parts = gp.compute();
			System.out.println(parts); 
		}

	
	}
	public static void testGenetic()
	{
		int k = 2;

		Graph g=loadGraph();
		if(g!=null && k <= g.size())
			{
				System.out.println(g);
				Partitioning gp = new GeneticPartitioning(g, k);
//				Partitioning gp = new WeightedUFPartitioning(g, k);
				Set<Partition> parts = gp.compute();
				for (Partition partition : parts) {
					System.out.println(((GrowingPartition)partition));
				}
			}

		
	}*/
	private static Graph loadGraph(String file)
	{
		Graph g=null;
		try {
			g = FileParser.load(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}

}
