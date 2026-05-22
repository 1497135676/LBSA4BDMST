
public class Edge implements Comparable<Edge>{
    int p1, p2;
    double dist;
    
    public Edge(int p1, int p2, double dist) {
   	    this.p1 = p1;
   	    this.p2 = p2;
    	this.dist = dist;
    }
    
    public double getDist() { return dist; }

	@Override
	public int compareTo(Edge arg) {
		if (this.dist > arg.dist) {
			return 1;
		} else if (this.dist == arg.dist) {
			return 0;
		} else {
		    return -1;
		}
	}
}

