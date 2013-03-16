package clustering.data;

import info.Document;

import java.util.Map;
import java.util.Vector;

import clustering.dbscan.DBSCAN;
import clustering.kmeans.KMeans;
import clustering.mitosis.Mitosis;
import constants.ConstantHandler;

public class ClusterHandler {
	private Map<Integer, Document> documentMap;
	private ClusteringInterface clusteringInterface;

	public ClusterHandler(Map<Integer, Document> documentMap) {
		this.documentMap = documentMap;
	}

	public Vector<Cluster> _Start_Clustering(int type) {
		switch (type) {
		case ConstantHandler.KMEANS:
			clusteringInterface = new KMeans(documentMap,
					ConstantHandler.NUMBEROFCENTROIDS,
					ConstantHandler.MAXITERATIONS);
			break;
		case ConstantHandler.DBSCAN:
			clusteringInterface = new DBSCAN(documentMap,
					ConstantHandler.EPSLION, ConstantHandler.MINPOINTS);
			break;
		case ConstantHandler.MITOSIS:
			clusteringInterface = new Mitosis(documentMap, ConstantHandler.F,
					ConstantHandler.K);
			break;
		case ConstantHandler.SHC:
//			clusteringInterface = new SHC(documentMap);
			break;
		}

		if (clusteringInterface != null) {
			clusteringInterface.cluster();
			clusteringInterface.printClusters();
			return clusteringInterface._Clusters();
		} else {
			return new Vector<Cluster>();
		}
	}
}
