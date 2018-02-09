package org.aksw.gpaba.growingPartitioning;

import java.util.List;

public class GrowingPartitioningTest {
	public static void main(String[] args)
	{
		GrowingPartitioning g = new GrowingPartitioning(null, 0);
		List<Integer> rValues = g.getRandomXValues(3, 20);
		for (Integer integer : rValues) {
			System.out.println(integer);
		}
	}
}
