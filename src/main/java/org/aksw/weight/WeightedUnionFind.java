package org.aksw.weight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Andre Valdestilhas {@literal valdestilhas@informatik.uni-leipzig.de}
 *
 */
public class WeightedUnionFind {
	Map<String, String> parents = new HashMap<>();
	Map<String, Integer> representantElements = new HashMap<>();

	public void union(PTSPNode p0, PTSPNode p1) {
		String cp0 = find(p0.getLabel());
		String cp1 = find(p1.getLabel());
		if (!cp0.equals(cp1)) {
			int size0 = representantElements.get(cp0);
			int size1 = representantElements.get(cp1);
			if (size1 < size0) {
				String swap = cp0;
				cp0 = cp1;
				cp1 = swap;
			}
			representantElements.put(cp0, size0 + size1);
			parents.put(cp1, cp0);
			representantElements.put(cp1, 0);
		}
	}

	public String find(String p) {
		if (!representantElements.containsKey(p)) {

			representantElements.put(p, 1);
			return p;
		}
		String result = parents.get(p);
		if (result == null) {
			result = p;
		} else {
			result = find(result);
			parents.put(p, result);
		}
		return result;
	}

	public Set<Set<String>> getPartitions() {
		Map<String, Set<String>> result = new HashMap<>();
		representantElements.forEach((key, value) -> {
			if (value > 0) {
				result.put(key, new HashSet<>(value));
			}
		});
		representantElements.forEach((key, value) -> {
			result.get(find(key)).add(key);
		});
		return new HashSet<>(result.values());
	}

	public static Set<UFPartition> partitions(Map<PTSPNode, Set<PTSPNode>> pairs, int pThreshold) {
		Set<UFPartition> ret = new HashSet<UFPartition>();
		Set<Set<String>> setPartitions = new HashSet<Set<String>>();
		int sumWeight = 0;
		UnionFind groups = new UnionFind();
		for (Map.Entry<PTSPNode, Set<PTSPNode>> entry : pairs.entrySet()) {
			PTSPNode first = entry.getKey();
			if (first.getWeight() >= pThreshold) {
				groups.union(first, new PTSPNode("", 0));
				setPartitions.addAll(groups.getPartitions());
				//groups = new UnionFind();
				//groups.union(first, new PTSPNode("", 0));
				continue;
			}
			for (PTSPNode pair : entry.getValue()) {
				if (pair.getWeight() >= pThreshold) {
					groups.union(pair, new PTSPNode("", 0));
					
					setPartitions.addAll(groups.getPartitions());
					groups = new UnionFind();
					groups.union(first, new PTSPNode("", 0));
					continue;
				}
				groups.union(first, pair);
				// groups.union(first, new PTSPNode("", 0));
				sumWeight = +(first.getWeight() + pair.getWeight());
				if (sumWeight >= pThreshold) {
					setPartitions.addAll(groups.getPartitions());
					groups = new UnionFind();
				}
			}
		}
		if (sumWeight < pThreshold) {
			setPartitions.addAll(groups.getPartitions());
		}
		ret = transformUFpartition(setPartitions, pairs);
		
		return ret;
	}
	
	private static Set<UFPartition> transformUFpartition(Set<Set<String>> setPartitions, Map<PTSPNode, Set<PTSPNode>> graph) {
		Set<UFPartition> ret = new HashSet<UFPartition>();
		for (Set<String> sPartition : setPartitions) {
			Set<PTSPNode> sNodes = new HashSet<>();
			for (String node : sPartition) {
				PTSPNode pNode = null;
				
				for (Map.Entry<PTSPNode, Set<PTSPNode>> entry : graph.entrySet()) {
					if(entry.getKey().getLabel().equals(node)){
						pNode = entry.getKey();
						break;
					}else{
						for (PTSPNode ptspNode : entry.getValue()) {
							if(ptspNode.getLabel().equals(node)){
								pNode = ptspNode;
								break;
							}
						}
					}
				}
				if(pNode != null)
					sNodes.add(pNode);
			}
			UFPartition ufPart = new UFPartition(sNodes);
			ret.add(ufPart);
		}
		
		return ret;
	}

	public static void main(String[] args) {
		Map<PTSPNode, Set<PTSPNode>> graph = new HashMap<PTSPNode, Set<PTSPNode>>();

		PTSPNode a = new PTSPNode("a", 1);
		PTSPNode b = new PTSPNode("b", 2);
		PTSPNode c = new PTSPNode("c", 1);
		PTSPNode d = new PTSPNode("d", 88);
		PTSPNode e = new PTSPNode("e", 1);
		PTSPNode f = new PTSPNode("f", 1);

		graph.put(a, new HashSet<PTSPNode>());
		graph.get(a).add(c);
		graph.put(b, new HashSet<PTSPNode>());
		graph.get(b).add(c);
		graph.put(c, new HashSet<PTSPNode>());
		graph.get(c).add(d);
		graph.put(e, new HashSet<PTSPNode>());
		graph.get(e).add(f);

		int threshold = 3;
		System.out.println("Input: " + graph);
		System.out.println("Output: " + partitions(graph, threshold));
		Set<UFPartition> ps = partitions(graph, threshold);
		for (UFPartition partition : ps) {
			System.out.println(partition);
		}
	}
}
