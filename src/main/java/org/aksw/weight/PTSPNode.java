package org.aksw.weight;

/**
 * @author Andre Valdestilhas {@literal valdestilhas@informatik.uni-leipzig.de}
 *
 */
public class PTSPNode {
	private String label;
	private int weight;
	
	public PTSPNode(String pLabel, int pWeight) {
		setLabel(pLabel);
		setWeight(pWeight);
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "[" + this.getLabel() + "," + this.weight + "]";
	}
}
