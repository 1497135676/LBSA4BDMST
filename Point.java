import java.util.Objects;

public class Point implements Comparable<Point>{
	int id;
    double x;
    double y;
    double c;
    Point(int id, double x, double y) {
    	this.id = id;
    	this.x = x;
    	this.y = y;
    	c = (x - 0.5)*(x - 0.5) + (y - 0.5)*(y - 0.5);
    }
    
    public String toString() {
    	return  x + "\t" + y + "\t" + c;
    }

	@Override
	public int compareTo(Point o) {
	    if (this.c < o.c) {
	    	return -1;
	    } else if (this.c == o.c) {
	    	return 0;
	    } else {
	    	return 1;
	    }
	}
	
	@Override
	public boolean equals(Object o) {
		return this.compareTo((Point)o) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.x+"x", this.y+"y");
	}
}
