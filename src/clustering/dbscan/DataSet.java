package clustering.dbscan;

import java.util.Vector;


public class DataSet {
	private Vector<Point> points;
	
	public DataSet() {
		points = new Vector<Point>();
	}

	public DataSet(Vector<Point> pointsList) {
		points = new Vector<Point>(pointsList);
	}

	public void reset() {
		for (Point p : points) {
			p.setLabel(Point.UNCLASSIFIED);
		}
	}

	public int size() {
		if (points != null) {
			return points.size();
		} else {
			return 0;
		}
	}

	public Vector<Point> points() {
		return this.points;
	}

	public Point getPoint(int i) {
		return points.get(i);
	}

	public void addPoint(Point p) {
		points.add(p);
	}
}
