
package org.aksw.gpaba;

public interface Partition {
	/**
	 * This method returns the number of nodes in each partition
	 * @return the number of included nodes in the graph
	 */
	int getNumberOfNodes();
	/**
	 * calculates the total size of the graph in terms of the total size of its nodes
	 * @return total size of the graph
	 */
	int getSumOfNodesWeights(); //
}
