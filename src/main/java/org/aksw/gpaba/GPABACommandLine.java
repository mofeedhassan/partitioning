package org.aksw.gpaba;

/**
 * @author Tommaso Soru {@literal tsoru@informatik.uni-leipzig.de}
 *
 */
public class GPABACommandLine {

	public static void main(String[] args) {
		
		String algo = args[0];
		String file = args[1];
		
		GPABAMain.Algorithm algorithm;
		
		switch(algo.toLowerCase()) {
		case "genetic": 
			algorithm = GPABAMain.Algorithm.GENETIC; break;
		case "growing": 
			algorithm = GPABAMain.Algorithm.GROWING; break;
		case "unionfind": 
			algorithm = GPABAMain.Algorithm.UNIONFIND; break;
		default:
			System.out.println("Algorithm is not available.");
			return;
		}
		
		GPABAMain.runAlgorithm(algorithm, file);

	}

}
