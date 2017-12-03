package org.aksw.gpaba;

import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public interface Partitioning {
	
	public String getName();
	
	public Set<Partition> compute();

}
