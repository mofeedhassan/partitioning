package org.aksw.gpaba;

import java.io.FileNotFoundException;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GPABAMain {
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		String file = "graph.txt";
		
		Graph g = FileParser.load(file);
		
		System.out.println(g);
		
	}


}
