package org.aksw.gpaba;

import java.io.FileNotFoundException;
import java.util.Set;

import org.aksw.gpaba.genetic.GeneticPartitioning;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GPABAMain {

	public static void main(String[] args) throws FileNotFoundException {

		String file = "graph.txt";
		int k = 4;

		Graph g = FileParser.load(file);
		System.out.println(g);

		Partitioning gp = new GeneticPartitioning(g, k);
		Set<Partition> parts = gp.compute();
		System.out.println(parts);

	}

}
