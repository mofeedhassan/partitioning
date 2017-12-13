package org.aksw.gpaba.genetic;


/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Individual implements Comparable<Individual> {

	private String genome;
	private int fitness;
	
	public Individual(String genome) {
		this.setGenome(genome);
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public String getGenome() {
		return genome;
	}

	private void setGenome(String genome) {
		this.genome = genome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if( this == other )
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Individual [genome=" + genome + ", fitness=" + fitness + "]";
	}

	@Override
	public int compareTo(Individual o) {
		return this.hashCode() - o.hashCode();
	}
	
}
