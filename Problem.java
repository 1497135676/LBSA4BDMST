import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Problem {
	private static Problem problem = null;
	   
	private String fileName;
    private int nodeNum;
    private int bound;
    private double bestCost;
    private double[][] pos;
    protected double[][] dists;
    private int[][] rank;
    List<Point> points;
    List<Edge> orderedEdges;
    List<Edge>[] orderedEdgesOfNode;
    List<Integer>[] orderedNodeOfNode;
	//List<Integer>[] priorList;
    public int getNodeNum() { return nodeNum;}
    public int getBound() { return bound; }
    public double getBestCost() { return bestCost;}
    public Point getNode(int index) { 
    	if (pos != null) {
    	    return new Point(index, pos[index][0], pos[index][1]); 
    	} else {
    		return new Point(-1, 0,0);
    	}
    }
    public int getRank(int n1, int n2) { return rank[n1][n2]; }
    public double getDistance(int n1, int n2) { return dists[n1][n2]; }
    public List<Point> getNodes() {
    	List<Point> nodes = new ArrayList<>();
    	for (int i = 0; i < pos.length; i++) {
    		nodes.add(new Point(i, pos[i][0], pos[i][1]));
    	}
    	return nodes;
    }
    
    public List<Edge> getOrderedEdges() {
    	List<Edge> es = new ArrayList<>();
    	for (int i = 0; i < orderedEdges.size(); i++) {
    		Edge e = new Edge(orderedEdges.get(i).p1, orderedEdges.get(i).p2, orderedEdges.get(i).dist);
    		e.dist = orderedEdges.get(i).dist;
    		es.add(e);
    	}
    	return es;
    }
    
    public List<Edge> getOrderedEdges(int node) {
    	return orderedEdgesOfNode[node];
    }
    
    public List<Integer> getOrderedNodes(int node) {
    	return orderedNodeOfNode[node];
    }
    
    private Problem(String fileName) throws FileNotFoundException,IOException {
    	FileReader data;
    	Scanner scan;
        this.fileName = fileName;
    	data = new FileReader(fileName);
    	scan = new Scanner(data);
    	if (fileName.contains("ran")) {
    		readFileEdges(scan);
    	} else {
    		readFilePoint(scan);
    		Collections.sort(points);
    		//Collections.reverse(points);
    	}
    	scan.close();
    	//
    	sort();
     }
    
    private void sort() {
    	orderedEdges = new ArrayList<Edge>();
    	for (int i = 0; i < dists.length; i++) {
    		for (int j = i + 1; j < dists[i].length; j++) {
    			Edge e = new Edge(i, j, dists[i][j]);
    			e.dist = dists[i][j];
    			orderedEdges.add(e);
    		}
    	}

    	Collections.sort(orderedEdges);
    	orderedEdgesOfNode = new ArrayList[nodeNum];
    	orderedNodeOfNode = new ArrayList[nodeNum];
    	for (int i = 0; i < nodeNum; i++) {
    		orderedEdgesOfNode[i] = new ArrayList<>();
    		orderedNodeOfNode[i] = new ArrayList<>();
    	}
    	for (Edge e : orderedEdges) {
    		orderedEdgesOfNode[e.p1].add(e);
    		orderedEdgesOfNode[e.p2].add(e);
    		
    		orderedNodeOfNode[e.p1].add(e.p2);
    		orderedNodeOfNode[e.p2].add(e.p1);
    	}

    	int[] index = new int[nodeNum];
    	rank = new int[nodeNum][nodeNum];
    	for (Edge e : orderedEdges) {
    		rank[e.p1][e.p2] = index[e.p1];
    		index[e.p1] += 1;

    		rank[e.p2][e.p1] = index[e.p2];
    		index[e.p2] += 1;
    	}
    }


    
    private void readFilePoint(Scanner scan) {
    	points = new ArrayList<>();
    	nodeNum = scan.nextInt();
    	bound = scan.nextInt();
    	pos = new double[nodeNum][2];
    	for (int i = 0; i < nodeNum; i++) {
    		pos[i][0] = scan.nextDouble();
    		pos[i][1] = scan.nextDouble();
    		points.add(new Point(i, pos[i][0], pos[i][1]));
    	}
    	
    	dists = new double[nodeNum][nodeNum];
    	for (int i = 0; i < nodeNum; i++) {
    		for (int j = i + 1; j < nodeNum; j ++) {
    			double s = (pos[i][0] - pos[j][0]) * (pos[i][0] - pos[j][0]);
    			s += (pos[i][1] - pos[j][1]) * (pos[i][1] - pos[j][1]);
    			dists[i][j] = Math.sqrt(s);
    			dists[j][i] = dists[i][j];
    		}
    	}
    }
    
    private void readFileEdges(Scanner scan) {
    	points = new ArrayList<>();
    	nodeNum = scan.nextInt();
    	bound = scan.nextInt();
    	pos = new double[nodeNum][2];
//    	for (int i = 0; i < nodeNum; i++) {
//    		pos[i][0] = scan.nextDouble();
//    		pos[i][1] = scan.nextDouble();
//    		points.add(new Point(i, pos[i][0], pos[i][1]));
//    	}
    	
    	dists = new double[nodeNum][nodeNum];
    	for (int i = 0; i < nodeNum; i++) {
    		for (int j = 0; j < nodeNum; j ++) {
    			dists[i][j] = scan.nextDouble();
    		}
    	}

    }
    
    public static Problem load(String fileName) {
    	try {
    		problem = new Problem(fileName);
        	return problem;
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    public static Problem get() { return problem; }
    public static String getFileName() {
    	String f = problem.fileName;
    	if (f.lastIndexOf("\\") >= 0) {
    	    return f.substring(f.lastIndexOf("\\")+1);
		} else {
			return f.substring(f.lastIndexOf("/")+1);
		}
    }
    
    public String toString() {
    	String str = "" + nodeNum  + "\n";
    	
    	for (int i = 0; i < nodeNum && pos != null; i++) {
    		str += pos[i][0] + "\t" + pos[i][1] + "\n";
    	}
    	
    	for (Point p : points) {
    		str += p.id + ": " + p.x + ", " + p.y + "," + p.c + "\n";
    	}
    	
    	for (Edge e : orderedEdgesOfNode[0]) {
    		str += e.p1 + "->" + e.p2 + " = " + e.dist + "\n";
    	}
    	
    	
    	return str;
    }
    
    public static void main(String[] args) {
     	String fileName = (new File("")).getAbsolutePath() + "/datas/estein/estein100.txt";
     	//String fileName = (new File("")).getAbsolutePath() + "/datas/rand100/ran100-01.txt";
    	try {
    	    Problem problem = Problem.load(fileName);
    	    System.out.println(problem);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
     }
}



