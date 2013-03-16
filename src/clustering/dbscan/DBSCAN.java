package clustering.dbscan;

import info.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import clustering.data.Cluster;
import clustering.data.ClusteringInterface;
import clustering.data.DistanceHandler;

public class DBSCAN implements ClusteringInterface {
	private DataSet dataset;
	private int clustersNumbers;
	private double epslion;
	private int minPoints;

	// ****************************
	private Map<Integer, Document> documentMap;
	private Map<Integer, Vector<Point>> clustersMap;
	private Vector<Cluster> clusters;
	private DistanceHandler distanceHandler;

	// ****************************

	public DBSCAN(Map<Integer, Document> documentMap, double epslion,
			int minPoints) {
		this.documentMap = documentMap;
		clustersNumbers = 0;
		this.epslion = epslion;
		this.minPoints = minPoints;
		clustersMap = new HashMap<Integer, Vector<Point>>();
		clusters = new Vector<Cluster>();
		distanceHandler = new DistanceHandler();
		prepareDataSet();
	}

	private void prepareDataSet() {
		this.dataset = new DataSet();
	}

	@Override
	public void cluster() {
		clustersMap = new HashMap<Integer, Vector<Point>>();
		dataset.reset();
		for (Point p : dataset.points()) {
			if (!p.isClassified()) {
				ArrayList<Point> pNeighbors = getNeighbors(p);
				if (pNeighbors.size() < this.minPoints) { // -1
					p.setLabel(Point.NOISE);
				} else {
					p.setClusterID(++clustersNumbers);
					p.setNumOfNeighboors(pNeighbors.size());
					// **********New Cluster**************
					Vector<Point> clusterPoints = new Vector<Point>();
					clusterPoints.add(p);
					for (Point q : pNeighbors) {
						q.setClusterID(clustersNumbers);
						clusterPoints.add(q);
					}
					clustersMap.put(clustersNumbers, clusterPoints);
					expandCluster(pNeighbors, clustersNumbers);
					// ***********************************
				}
			}
		}
	}

	private void expandCluster(ArrayList<Point> pNeighbors, int clusternumber) {
		while (!pNeighbors.isEmpty()) {
			Point q = pNeighbors.get(0);
			ArrayList<Point> qNeighbors = getNeighbors(q);
			q.setNumOfNeighboors(qNeighbors.size());
			if (qNeighbors.size() >= this.minPoints) { // -1
				for (Point p : qNeighbors) {
					if (p.isUnClassified()) {
						pNeighbors.add(p);
						p.setClusterID(clusternumber);
						(clustersMap.get(clusternumber)).add(p);
					} else if (p.getLabel() == Point.NOISE) {
						p.setClusterID(clusternumber);
						clustersMap.get(clusternumber).add(p);
					}
				}
			}
			pNeighbors.remove(0);
		}
	}

	private double getDistance(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		return distanceHandler.calculateDistance(point1, point2,
				DistanceHandler.COSINE);
	}

	private ArrayList<Point> getNeighbors(Point p) {
		ArrayList<Point> neighbors = new ArrayList<Point>();
		for (Point q : dataset.points()) {
			if ((p.getID() != q.getID())
					&& getDistance(documentMap.get(p.getID())
							.getRepresentative(), documentMap.get(q.getID())
							.getRepresentative()) <= this.epslion) {
				neighbors.add(q);
			}
		}
		return neighbors;
	}

	@Override
	public Vector<Cluster> _Clusters() {
		for (int clusterNumber : clustersMap.keySet()) {
			Vector<Point> points = clustersMap.get(clusterNumber);
			Vector<Integer> pointsID = new Vector<Integer>();
			for (Point point : points) {
				pointsID.add(point.getID());
			}
			Cluster temp = new Cluster(clusterNumber);
			temp.addPoints(pointsID);
			temp.setRepresentative(distanceHandler.calculateRepresentative(
					pointsID, documentMap));
			clusters.add(temp);
		}
		return clusters;
	}

	@Override
	public int getClustersNumbers() {
		return clusters.size();
	}

	@Override
	public void printClusters() {
		for (Cluster cluster : clusters) {
			System.out.println("Cluster # " + cluster.getClusterID()
					+ " has documentMap: ");
			for (Integer point : cluster.getPoints())
				System.out.print(point + " ");
			System.out.println();
		}
		
		System.out
				.println("-----------------------------------------------------------------------------------");

		System.out.println("Noise Points: ");
		for (Point point : dataset.points())
			if (point.getLabel() == Point.NOISE)
				System.out.print(point.getID() + " ");
		System.out.println();
	}
}