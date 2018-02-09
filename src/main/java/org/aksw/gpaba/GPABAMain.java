package org.aksw.gpaba;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.aksw.gpaba.genetic.GeneticPartitioning;
import org.aksw.gpaba.growingPartitioning.GrowingPartition;
import org.aksw.gpaba.growingPartitioning.GrowingPartitioning;
import org.aksw.weight.WeightedUFPartitioning;

/**
 * GPABA = Graph Partitioning and Balancing Algorithm.
 * 
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GPABAMain {
	public enum Algorithm{
		GENETIC,GROWING,UNIONFIND
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		Algorithm algorithm = Algorithm.GENETIC;
		runAlgorithm(algorithm);

	}
	public static void runAlgorithm(Algorithm algorithm)
	{
		int k = 4;
		Graph g=loadGraph();
		if(g!=null && k <= g.size())
		{
			System.out.println(g);
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
			Set<Partition> parts = gp.compute();
			System.out.println(parts); 
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
	private static Graph loadGraph()
	{
		String file = "graph1k";

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
