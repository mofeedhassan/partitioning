package org.aksw.gpaba.genetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Pool {

	// number of individuals
	private int size;

	// number of partitions / gene types
	private int k;

	// number of nodes / genes
	private int numNodes;

	private Set<Individual> inds;

	public Pool(int size, int k, int numNodes) {
		this.size = size;
		this.k = k;
		this.numNodes = numNodes;
		this.inds = new HashSet<>();
	}

	public void create() {

		ArrayList<String> types = new ArrayList<>();
		for (char c = 'A'; c <= 'A' + k - 1; c++)
			types.add(String.valueOf(c));

		System.out.println("types=" + types);

		// for each individual
		for (int i = 0; i < size; i++) {
			String genome = "";
			// for each gene
			for (int j = 0; j < numNodes; j++) {
				String gene = types.get((int) (k * Math.random()));
				genome += gene;
			}
			Individual ind = new Individual(genome);
			inds.add(ind);
			System.out.println(i + " => " + genome);
		}

	}

	public Set<Individual> getIndividuals() {
		return inds;
	}

}
