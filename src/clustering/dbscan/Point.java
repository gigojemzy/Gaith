package clustering.dbscan;

public class Point {

	public static final int UNCLASSIFIED = -2;
	public static final int NOISE = -1;
	public static final int CLASSIFIED = 0;

	private int label; // State
	private int clusterID;
	private int numOfNeighboors;
	private int pointID;

	public Point(int pointID) {
		label = UNCLASSIFIED;
		clusterID = 0;
		numOfNeighboors = 0;
		this.pointID = pointID;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		if (label == UNCLASSIFIED || label == NOISE) {
			clusterID = UNCLASSIFIED;
		}
		this.label = label;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterId) {
		clusterID = clusterId;
		this.label = CLASSIFIED;
	}

	public int getID() {
		return pointID;
	}

	public void setID(int ID) {
		this.pointID = ID;
	}

	public boolean isClassified() {
		return label != UNCLASSIFIED;

	}

	public boolean isUnClassified() {
		return label == UNCLASSIFIED;

	}

	public int getNumOfNeighboors() {
		return numOfNeighboors;
	}

	public void setNumOfNeighboors(int numOfNeighboors) {
		this.numOfNeighboors = numOfNeighboors;
	}

	public void reset() {
		this.label = UNCLASSIFIED;
	}
}
