package de.uni_leipzig.simba.memorymanagement.lazytsp.graphPartitioning.partitioners.GoModified;

import java.util.logging.Logger;


public class Main {

	static Logger logger = Logger.getLogger("LIMES"); 

	static Graph g = new Graph(5, 7);//nodes,edges
	public static void main(String[] args) {
		initGraph();
		Display();
		KLFM.Results res=null;
		KLFM k = new KLFM();
		for(int i=0;i<15;i++)
		{
			res = k.klfm(g);
			System.out.println("The results are: ");
			System.out.println("The min cut value = "+res.MIN+" The noeds destributions: ");
			for(int j=0;j<res.parts.length;j++)
				System.out.print(res.parts[j]+":");
			System.out.println();

		}
		split(res);
		Display();

	}
	
	private static void Display()
	{
		for (Node n : g.nodes) {
			System.out.println("Node "+n.id + ":" + n.part + ":" + n.gain);
			for (Edge e : n.edges) {
				if(e!=null)
					System.out.println("Edge "+e.id + ":" + e.w);
			}
			System.out.println("======================================");
		}
	}
	
	private static void initGraph()
	{
		initNodes();
		initEdges();
		initNodesEdges();
	}
	
	private static void split(de.uni_leipzig.simba.memorymanagement.lazytsp.graphPartitioning.partitioners.GoModified.KLFM.Results res)
	{
		 
		for(int i=0;i<g.edges.length; i++)
		{
			if(res.parts[g.edges[i].n1.id] != res.parts[g.edges[i].n2.id])// the nodes are in different partition
			{
				g.edges[i]=null;
			}
		}
	}
	private static void split(de.uni_leipzig.simba.memorymanagement.lazytsp.graphPartitioning.partitioners.GoModified.KLFM.Results res, Graph g2)
	{
		 
		for(int i=0;i<g.edges.length; i++)
		{
			if(res.parts[g.edges[i].n1.id] != res.parts[g.edges[i].n2.id])// the nodes are in different partition
			{
				g.edges[i]=null;
			}
		}
	}
	//
	private static void initEdges()
	{
		for(int i=0;i<g.edges.length; i++)
		{
			g.edges[i] = new Edge();
			g.edges[i].id = i;
		}
		g.edges[0].n1 = g.nodes[0];
		g.edges[0].n2 = g.nodes[1];
		g.edges[0].w = 18;
		
		g.edges[1].n1 = g.nodes[1];
		g.edges[1].n2 = g.nodes[2];
		g.edges[1].w = 14;
		
		g.edges[2].n1 = g.nodes[2];
		g.edges[2].n2 = g.nodes[4];
		g.edges[2].w = 15;
		
		g.edges[3].n1 = g.nodes[3];
		g.edges[3].n2 = g.nodes[4];
		g.edges[3].w = 3;
		
		g.edges[4].n1 = g.nodes[0];
		g.edges[4].n2 = g.nodes[3];
		g.edges[4].w = 17;
		
		g.edges[5].n1 = g.nodes[2];
		g.edges[5].n2 = g.nodes[3];
		g.edges[5].w = 2;
		
		g.edges[6].n1 = g.nodes[0];
		g.edges[6].n2 = g.nodes[2];
		g.edges[6].w = 1;
				
	}
	
	//create nodes with their data and the create the edges array for each node (without filling them)
	private static void initNodes()
	{
		for(int i=0;i<g.nodes.length; i++)
		{
			g.nodes[i] = new Node();
			g.nodes[i].id = i;
			g.nodes[i].gain=0;
			g.nodes[i].lock =false;
			g.nodes[i].part=-1;
		}
		g.nodes[0].edges = new Edge[3];
		g.nodes[1].edges = new Edge[2];
		g.nodes[2].edges = new Edge[4];
		g.nodes[3].edges = new Edge[3];
		g.nodes[4].edges = new Edge[2];
		
		
	}
	//assigns for each node's edges collection the edges in/out of the node
	private static void initNodesEdges()
	{
		g.nodes[0].edges[0] = g.edges[0];
		g.nodes[0].edges[1] = g.edges[4];
		g.nodes[0].edges[2] = g.edges[6];
		
		g.nodes[1].edges[0] = g.edges[0];
		g.nodes[1].edges[1] = g.edges[1];

		g.nodes[2].edges[0] = g.edges[1];
		g.nodes[2].edges[1] = g.edges[2];
		g.nodes[2].edges[2] = g.edges[5];
		g.nodes[2].edges[3] = g.edges[6];

		g.nodes[3].edges[0] = g.edges[3];
		g.nodes[3].edges[1] = g.edges[4];
		g.nodes[3].edges[2] = g.edges[5];
		
		g.nodes[4].edges[0] = g.edges[2];
		g.nodes[4].edges[1] = g.edges[3];
	}
}
