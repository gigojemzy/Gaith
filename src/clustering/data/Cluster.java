package clustering.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Cluster {
	private int clusterID;
	private Map<Integer, Double> representative;
	private Vector<Integer> points;

	public Cluster(int clusterID) {
		this.clusterID = clusterID;
		this.representative = new HashMap<Integer, Double>();
		this.points = new Vector<Integer>();
	}

	public void setRepresentative(Map<Integer, Double> representative) {
		this.representative = representative;
	}

	public Map<Integer, Double> getRepresentative() {
		return representative;
	}

	public void addPoint(int point) {
		points.add(point);
	}

	public void addPoints(Vector<Integer> points) {
		this.points = new Vector<Integer>(points);
	}

	public Vector<Integer> getPoints() {
		return points;
	}

	public int getClusterID() {
		return clusterID;
	}
}
