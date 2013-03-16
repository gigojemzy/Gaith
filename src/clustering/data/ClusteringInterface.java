package clustering.data;

import java.util.Vector;

public interface ClusteringInterface {

	public void cluster();

	public Vector<Cluster> _Clusters();

	public int getClustersNumbers();

	public void printClusters();
}
