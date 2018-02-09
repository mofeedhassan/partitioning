package org.aksw.gpaba.genetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
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
	private int numGenes;

	private List<Individual> inds;

	public Pool(int size, int k, int numGenes) {
		this.size = size;
		this.k = k;
		this.numGenes = numGenes;
		this.inds = new ArrayList<>();
	}

	public void addIndividual(Individual ind) {
		inds.add(ind);
	}
	
	public void removeIndividual(Individual ind) {
		inds.remove(ind);
	}
	
	public void create() {//the method creates different genomes for individuals and add them to the individual set

		ArrayList<String> types = new ArrayList<>();
		for (char c = 'A'; c <= 'A' + k - 1; c++)//create groups/partitions labels starting from A
			types.add(String.valueOf(c));

		System.out.println("types=" + types);

		// for each individual
		while ( inds.size() < size ) {
			String genome = "";
			// for each gene
			for (int j = 0; j < numGenes; j++) {//create a random distributed genes for the genome 
				String gene = types.get((int) (k * Math.random()));//create random parition selection that the corresponding gene (node) should belongs to
				genome += gene;
			}
//			long start = System.nanoTime();
			boolean valid = validate(genome);//check if in the generated genome if each gene is assigned and all gene values (A/B) are represented
//			System.out.println("runtime="+(System.nanoTime()-start));
			if(!valid) {
				System.out.println("rejected: "+genome);
				continue;
			}
			Individual ind = new Individual(genome);
			addIndividual(ind);
			System.out.println(inds.size() + " => " + genome);
		}

	}
	
	/**
	 * Alternative implementation of validate.
	 * 
	 * @param genome
	 * @return
	 */
	public boolean validate2(String genome) {
		OfInt chr = genome.chars().iterator();
		Set<Integer> set = new HashSet<>();
		int cnt = 0;
		while(chr.hasNext()) {
			// record that the value is present
			if(set.add(chr.next()))
				cnt++;
			if(cnt == k)
				return true;
		}
		// if it gets here, all genes have been visited
		return false;
	}


	public boolean validate(String genome) {
		OfInt chr = genome.chars().iterator();
		boolean[] valid = new boolean[k]; 
		ext: while(chr.hasNext()) {
			// record that the value is present
			valid[chr.next() - (int) 'A'] = true;
			for(boolean v : valid)
				// if a value is still not present, check next gene 
				if(!v)
					continue ext;
			// if it gets here, all values are present
			return true;
		}
		// if it gets here, all genes have been visited
		return false;
	}

	public List<Individual> getIndividuals() {
		return inds;
	}
	
	public int getSize() {
		return size;
	}

	public int getK() {
		return k;
	}

	public int getNumGenes() {
		return numGenes;
	}


}
