package org.aksw.gpaba.genetic;


/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class Individual implements Comparable<Individual> {

	private String genome;
	private double costFitness = Double.NaN;
	private double balanceFitness = Double.NaN;
	private double fitness = Double.NaN;
	
	public Individual(String genome) {
		this.setGenome(genome);
	}

	public double getFitness() {
		return fitness;
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
		return "Individual [genome=" + genome + ", costFitness=" + costFitness
				+ ", balanceFitness=" + balanceFitness + ", fitness=" + fitness
				+ "]";
	}

	@Override
	public int compareTo(Individual o) {
		return this.hashCode() - o.hashCode();
	}

	private void updateFitness() {
		this.fitness = Math.pow(this.costFitness, 2) + Math.pow(this.balanceFitness, 2);
	}
	
	public double getCostFitness() {
		return costFitness;
	}

	public void setCostFitness(double costFitness) {
		this.costFitness = costFitness;
		updateFitness();
	}

	public double getBalanceFitness() {
		return balanceFitness;
	}

	public void setBalanceFitness(double balanceFitness) {
		this.balanceFitness = balanceFitness;
		updateFitness();
	}
	
}
