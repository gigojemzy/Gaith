package clustering.kmeans;

import java.util.Map;
import java.util.Vector;

public class Centroid {

	private int centroidID;
	private Map<Integer, Double> representative;
	private Vector<Integer> points;

	public Centroid(int centroidID, Map<Integer, Double> representative) {
		this.centroidID = centroidID;
		this.representative = representative;
		points = new Vector<Integer>();
	}

	public Map<Integer, Double> getRepresentative() {
		return representative;
	}

	public void setRepresentative(Map<Integer, Double> representative) {
		this.representative = representative;
	}

	public int getCentroidID() {
		return centroidID;
	}
	
	

	public Vector<Integer> getPoints() {
		return points;
	}

	public void addPoint(int point) {
		points.add(point);
	}

	public void clear() {
		points.clear();
	}
	
	public String toString(){
		return ""+this.centroidID;
	}

}
