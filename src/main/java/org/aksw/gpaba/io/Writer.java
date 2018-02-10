package org.aksw.gpaba.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aksw.gpaba.Partition;

public class Writer {
	static Logger log = Logger.getLogger("gpaba");
	
	public static void writePartitions(String file, Set<Partition> partitions){
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			log.log(Level.INFO, "Start to write the graphs info. into file : {0}",file);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (Partition partition : partitions) {
				bw.write(partition.getNumberOfNodes()+"\t"+partition.getSumOfNodesWeights()+"\t"+partition+"\n");
			}

			log.log(Level.INFO, "DONE");

		} catch (IOException e) {
			log.log(Level.SEVERE, "ERROR: writing partitins into file has a problem as: \"{0}\"",e.getMessage());
			e.printStackTrace();

		} finally {
			try {
				log.log(Level.INFO, "Closing streams");
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				log.log(Level.SEVERE, "ERROR: problem in closing the streams as: {0}", ex.getMessage());
				ex.printStackTrace();

			}

		}
	}
}
