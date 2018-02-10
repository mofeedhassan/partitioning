package org.aksw.gpaba;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class FileParser {
	
	public static Graph load(String file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(new File(file));
		
		HashMap<Long, Node> nodes = new HashMap<>();
		Set<Edge> edges = new HashSet<>();
		
		long c = 0;
		
		while(in.hasNextLine()) {
			
			String[] data = in.nextLine().split("\\s");
			
			double w = Double.parseDouble(data[1]);
			
			if(data[0].contains("-")) {
				
				c++;
				
				// edge
				String[] eData = data[0].split("-");
				long id1 = Long.parseLong(eData[0]);
				long id2 = Long.parseLong(eData[1]);
				
				Edge e = new Edge(c, w, nodes.get(id1), nodes.get(id2));
				edges.add(e);
				
				//set edges for both nodes
				nodes.get(id1).getEdges().add(e);
				nodes.get(id2).getEdges().add(e);
				
			} else {
				// node
				long id = Long.parseLong(data[0]);
				
				Node n = new Node(id, w);
				nodes.put(id, n);
			}
			
		}
		in.close();
		
		Graph g = new Graph(new HashSet<>(nodes.values()), edges);
		
		return g;
	}


}
