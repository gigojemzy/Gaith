package clustering.mitosis;

import info.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import clustering.data.DistanceHandler;

public class Phase1 {

	private Map<Integer, Document> documentMap;
	private Map<Integer, Double> averageDistanceMap;
	private Map<Integer, Double> minsMap;
	private double F;
	private Vector<Association> l1;

	public Phase1(Map<Integer, Document> documentMap, double F) {
		this.documentMap = documentMap;
		this.F = F;
		initialize();
		_DO();
	};

	public Vector<Association> getL1() {
		return l1;
	}

	public Map<Integer, Double> Get_AverageDistanceMap() {
		return averageDistanceMap;
	}

	private void initialize() {
		averageDistanceMap = new HashMap<Integer, Double>();
		minsMap = new HashMap<Integer, Double>();
	}

	private void _DO() {
		calcDisBetweenPatterns();
		deleteDuplicated(l1);
	}

	private void calcDisBetweenPatterns() {
		DistanceHandler distanceHandler = new DistanceHandler();
		Map<Integer, Double> distanceMap = new HashMap<Integer, Double>();
		for (Document d1 : documentMap.values()) {
			distanceMap.clear();
			for (Document d2 : documentMap.values()) {
				double dis = distanceHandler.calculateDistance(
						d1.getRepresentative(), d2.getRepresentative(),
						DistanceHandler.COSINE);
				distanceMap.put(d2.getDocumentID(), dis);
			}

			double min = Double.MAX_VALUE;
			double localAverageDistance = calculateLocalAverageDistance(
					distanceMap, min);
			averageDistanceMap.put(d1.getDocumentID(), localAverageDistance);
			minsMap.put(d1.getDocumentID(), min);

			Vector<Integer> dynamicPatterns = _DynamicNearestNeighbors(
					distanceMap, F * min);
			for (int j = 0; j < dynamicPatterns.size(); j++) {
				l1.add(new Association(d1.getDocumentID(), dynamicPatterns
						.get(j), distanceMap.get(dynamicPatterns.get(j))));
			}

		}

		QuickSort q = new QuickSort();
		q.quicksort(l1, 0, l1.size() - 1);
	}

	private double calculateLocalAverageDistance(
			Map<Integer, Double> distanceMap, Double min) {
		double sum = 0;
		for (Double dis : distanceMap.values()) {
			sum += dis;
			min = dis < min ? dis : min;
		}
		return (sum / distanceMap.size());
	}

	private Vector<Integer> _DynamicNearestNeighbors(
			Map<Integer, Double> distanceMap, double range) {
		Vector<Integer> patterns = new Vector<Integer>();
		for (int documentID : distanceMap.keySet()) {
			if (distanceMap.get(documentID) < range) {
				patterns.add(documentID);
			}
		}
		return patterns;
	}

	private void deleteDuplicated(Vector<Association> l2) {
		Vector<Association> l1 = new Vector<Association>();
		int p;
		int q;
		double d;
		for (int i = 0; i < l2.size(); i++) {
			Association association = l2.get(i);
			p = association.getP();
			q = association.getQ();
			d = association.getDis();
			if (p > q && d <= F * minsMap.get(q))
				continue;
			l1.add(association);
		}
	}
}