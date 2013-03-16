package clustering.mitosis;

import info.Document;

import java.util.Map;
import java.util.Vector;

import clustering.data.Cluster;
import clustering.data.ClusteringInterface;
import clustering.data.DistanceHandler;

public class Mitosis implements ClusteringInterface {
	private double F;
	private double K;
	private Map<Integer, Document> documentMap;
	private Vector<Cluster> clusters;

	public Mitosis(Map<Integer, Document> documentMap, double F, double K) {
		this.F = F;
		this.K = K;
		this.documentMap = documentMap;
		this.clusters = new Vector<Cluster>();
	}

	@Override
	public void cluster() {
		Phase1 phase1 = new Phase1(documentMap, F);
		Vector<Association> l1 = phase1.getL1();
		Map<Integer, Double> averageDistanceMap = phase1
				.Get_AverageDistanceMap();

		Phase2 phase2 = new Phase2(K, l1, averageDistanceMap);
		Vector<Association> l2 = phase2.getL2();
		Map<Integer, MyCluster> clusterMap = phase2.getClusterMap();

		Phase3 phase3 = new Phase3(K, l2, clusterMap, averageDistanceMap);
		Map<Integer, MyCluster> finalClusterMap = phase3.getFinalClusterMap();

		DistanceHandler distanceHandler = new DistanceHandler();

		for (int clusterID : finalClusterMap.keySet()) {
			Cluster cluster = new Cluster(clusterID);
			Vector<Integer> points = finalClusterMap.get(clusterID)
					.getPatterns();
			cluster.addPoints(points);
			Map<Integer, Double> rep = distanceHandler.calculateRepresentative(
					points, documentMap);
			cluster.setRepresentative(rep);
			clusters.add(cluster);
		}
	}

	@Override
	public Vector<Cluster> _Clusters() {
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