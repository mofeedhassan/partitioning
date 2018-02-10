package org.aksw.weight;

import org.aksw.gpaba.Partition;
import java.util.Set;

public class UFPartition implements Partition {

	Set<PTSPNode> data = null;
	
	public UFPartition(Set<PTSPNode> node) {
		data = node;
	}

	public Set<PTSPNode> getData() {
		return data;
	}

	public void setData(Set<PTSPNode> data) {
		this.data = data;
	}

	@Override
	public int getNumberOfNodes() {
		return data.size();
	}

	@Override
	public int getSumOfNodesWeights() {
		int ret = 0;
		for (PTSPNode node : data) {
			ret += node.getWeight();
		}
		return ret;
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
