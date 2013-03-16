package clustering.kmeans;

import info.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import clustering.data.Cluster;
import clustering.data.ClusteringInterface;
import clustering.data.DistanceHandler;

public class KMeans implements ClusteringInterface {

	private int numberOfCentroids; // number of clusters
	private int maxIterations; // max number of iterations

	private Map<Integer, Integer> pointCentroidMap;
	private Map<Integer, Centroid> centroidMap;

	// ****************************
	private Map<Integer, Cluster> clustersMap;
	private Map<Integer, Document> documentMap;
	private Vector<Cluster> clusters;
	private DistanceHandler distanceHandler;

	// ****************************

	public KMeans(Map<Integer, Document> documentMap, int numberOfCentroids,
			int maxIterations) {
		pointCentroidMap = new HashMap<Integer, Integer>();
		clustersMap = new HashMap<Integer, Cluster>();
		this.documentMap = documentMap;
		this.numberOfCentroids = numberOfCentroids;
		this.maxIterations = maxIterations;
		clusters = new Vector<Cluster>();
		distanceHandler = new DistanceHandler();
	}

	private void _Initialize_Centroids() {
		centroidMap = new HashMap<Integer, Centroid>();

		/*
		 * initial centroidMap - randomly choose centroidMap between documentMap
		 */
		Random random = new Random(System.nanoTime());
		int id = 0;
		while (++id <= numberOfCentroids) {
			int randomID = random.nextInt();
			while (documentMap.get(randomID) == null)
				randomID = random.nextInt();
			Centroid randomCentroid = new Centroid(id, documentMap
					.get(randomID).getRepresentative());
			centroidMap.put(id, randomCentroid);
		}

		/*
		 * initials pointCentroidMap each point is the cluster of itself
		 */
		for (int point : pointCentroidMap.keySet()) {
			pointCentroidMap.put(point, point);
		}
	}

	private void _Update_Centroids() {
		for (int centroid : centroidMap.keySet()) {
			Centroid temp = centroidMap.get(centroid);
			temp.setRepresentative(distanceHandler.calculateRepresentative(
					temp.getPoints(), documentMap));
		}
	}

	private double getDistance(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		return distanceHandler.calculateDistance(point1, point2,
				DistanceHandler.COSINE);
	}

	private int getNearstCentroid(int point) {
		double min = Double.MAX_VALUE;
		Map<Integer, Double> pointVector = documentMap.get(point)
				.getRepresentative();
		int minCentroid = -1;
		for (int i = 1; i <= numberOfCentroids; i++) {
			double d = getDistance(pointVector, centroidMap.get(i)
					.getRepresentative());
			if (min > d) {
				min = d;
				minCentroid = i;
			}
		}
		return minCentroid;
	}

	private void _DO() {
		boolean finish = true;
		int iteration = 0;
		while (++iteration <= maxIterations && !finish) {
			_Clear_Centroids();
			finish = true;
			for (int point : documentMap.keySet()) {
				int nearstCentroid = getNearstCentroid(point);
				if (pointCentroidMap.get(point) != nearstCentroid) {
					pointCentroidMap.put(point, nearstCentroid);
					centroidMap.get(nearstCentroid).addPoint(point);
					finish = false;
				}
			}
			if (!finish)
				_Update_Centroids();
		}
	}

	private void _Clear_Centroids() {
		for (Centroid centroid : centroidMap.values()) {
			centroid.clear();
		}
	}

	@Override
	public void cluster() {
		_Initialize_Centroids();
		_DO();
	}

	@Override
	public Vector<Cluster> _Clusters() {
		int id = 0;
		while (++id <= numberOfCentroids) {
			clustersMap.put(id, new Cluster(id));
		}

		for (int point : pointCentroidMap.keySet()) {
			clustersMap.get(pointCentroidMap.get(point)).addPoint(point);
		}

		for (Cluster cluster : clustersMap.values()) {
			cluster.setRepresentative(centroidMap.get(cluster.getClusterID())
					.getRepresentative());
			clusters.add(cluster);
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
	}
}