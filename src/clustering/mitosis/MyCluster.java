package clustering.mitosis;

import java.util.Vector;

public class MyCluster {
	Vector<Association> clusterList;
	int id;
	Vector<Integer> patterns;
	double aveDis;

	public double getAveDis() {
		return aveDis;
	}

	public void setAveDis(double aveDis) {
		this.aveDis = aveDis;
	}

	public Vector<Integer> getPatterns() {
		return patterns;
	}

	public void setPatterns(Vector<Integer> patterns) {
		this.patterns = patterns;
	}

	public MyCluster() {
		id = 0;
		aveDis = 0.0;
		clusterList = new Vector<Association>();
		patterns = new Vector<Integer>();
	};

	public MyCluster(int id1, Vector<Association> clusterList1) {
		id = id1;
		clusterList = clusterList1;

	};

	public Vector<Association> getClusterList() {
		return clusterList;
	}

	public void setClusterList(Vector<Association> clusterList) {
		this.clusterList = clusterList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}