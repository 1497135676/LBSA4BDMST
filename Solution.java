
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Solution implements Comparable<Solution> {
	public static Problem prob;
	protected static Point[] nodes;
	protected static int maxDiameter;
	protected static int maxDepth;
	protected static int[][] eLists;// = new int[Problem.get().getNodeNum()][10];
	//protected static boolean[][] inEdges;// = new boolean[nodeNum][nodeNum];
	List<Integer>[] priorList;
	protected static Random rand;
	static int[] ps;
	static int[] ds;
    static boolean[] processed;
    static int[] position;
	static int[] u;
	public static void init(int bound) {
		prob = Problem.get();
		int nodeNum = prob.getNodeNum();
		nodes = new Point[nodeNum];
		for (int i = 0; i < nodeNum; i++) {
			nodes[i] = prob.getNode(i);
		}
		maxDiameter = bound;
		maxDepth = bound / 2;
		eLists = new int[nodeNum][maxDiameter];
		ps = new int[nodeNum];
		ds = new int[nodeNum];
		processed = new boolean[nodeNum];
		position = new int[nodeNum];
		rand = new Random();

		}



	
	public static Solution make() {
		 if (Simulation.repType == ERepType.LEVEL_LIST10) {
			 return new SolutionLevelList10();
		} else {
			return null;
		}
	}

	

	protected double cost;
	protected double bestCost;
	protected int lastImprove = 0;
	public abstract double eval();
	public abstract Edge[] getEdges();
	public abstract double neighbor(double t);
	
	public double getCost() { return cost; }
	public double getBestCost() { return bestCost; }
	public int getLastImprove() { return lastImprove; }
	public int[] localSearch( ) {
		return u;
	}

	@Override
	public int compareTo(Solution s) {
		if (this.cost < s.cost) {
			return 1;
		} else if (this.cost == s.cost) {
			return 0;
		} else {
			return -1;
		}
	}

	public void save(String fileName) {
		if (!Simulation.SAVING_SPANNING_TREE) {
			return;
		}

		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			int nodeNum = prob.getNodeNum();
			printWriter.write(nodeNum + "\t0\t0\t0\t0\n");
			printWriter.write(nodeNum + "\t0\t0\t0\t0\n");
			for (int i = 0; i < nodeNum; i++) {
				printWriter.write(i + "\t" + prob.getNode(i).x + "\t" +  prob.getNode(i).y + "\t0\t0\n");
			}

			Edge[] edges = getEdges();//获取边，保存边
			for (int i = 0; i <edges.length; i++) {

				printWriter.write(nodes[edges[i].p1].x + "\t" + nodes[edges[i].p1].y + "\t");
				printWriter.write(nodes[edges[i].p2].x + "\t" + nodes[edges[i].p2].y + "\t" + edges[i].dist + "\n");
			}

			printWriter.write(bestCost + "\t0\t0\t0\t0\n");

			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

    
    public String toString() {
    	String str = "\n" + cost + "\n";//""; //cost
    	Edge[] edges = getEdges();
        for (int i = 0; i < edges.length; i++) {
        	str += edges[i].p1 + "\t" + edges[i].p2 + "\t" + edges[i].dist + "\n";
        }
    	str += "\n";
    	return str;
    }
}
