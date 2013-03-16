package clustering.data;

import info.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DistanceHandler {

	public static final int ECULIDIAN = 1, COSINE = 2, PEAERSON = 3, KL = 4,
			JACCARD = 5;

	public double calculateDistance(Map<Integer, Double> point1,
			Map<Integer, Double> point2, int type) {
		double result = 0.0;
		switch (type) {
		case ECULIDIAN:
			result = _Eculidan(point1, point2);
			break;
		case COSINE:
			result = _Cosine(point1, point2);
			break;
		case PEAERSON:
			result = _Pearson(point1, point2);
			break;
		case KL:
			result = _KL(point1, point2);
			break;
		case JACCARD:
			result = _Jaccard(point1, point2);
			break;
		}
		return result;
	}

	private double _Eculidan(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		double ECDis = 0f;
		double conceptWeight1 = 0f;
		double conceptWeight2 = 0f;

		for (Integer conceptNumber : point1.keySet()) {
			conceptWeight2 = 0;
			conceptWeight1 = point1.get(conceptNumber);
			if (point2.containsKey(conceptNumber))
				conceptWeight2 = point2.get(conceptNumber);
			ECDis += Math.pow(conceptWeight1 - conceptWeight2, 2);
		}

		ECDis = Math.sqrt(ECDis);
		return ECDis;
	}

	private double _Cosine(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		double similarity = 0f;
		double num = 0f;
		double dem1 = 0f;
		double dem2 = 0f;
		double conceptWeight1 = 0f;
		double conceptWeight2 = 0f;

		for (Integer conceptNumber : point1.keySet()) {
			conceptWeight2 = 0;
			conceptWeight1 = point1.get(conceptNumber);
			if (point2.containsKey(conceptNumber))
				conceptWeight2 = point2.get(conceptNumber);
			num += (conceptWeight1 * conceptWeight2);
			dem1 += (conceptWeight1 * conceptWeight1);
		}

		for (Integer conceptNumber : point2.keySet()) {
			conceptWeight2 = point2.get(conceptNumber);
			dem2 += (conceptWeight2 * conceptWeight2);
		}

		similarity = (num) / Math.sqrt(dem1 * dem2);
		return Math.max(1 - similarity, 0f); // because round off error
	}

	private double _Pearson(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		double r = 0f;
		double conceptWeight1 = 0f;
		double conceptWeight2 = 0f;
		int size = 0;

		for (Integer conceptNumber : point1.keySet()) {
			conceptWeight2 = 0;
			conceptWeight1 = point1.get(conceptNumber);
			if (point2.containsKey(conceptNumber)) {
				conceptWeight2 = point2.get(conceptNumber);
				size++;
			}
			r += (conceptWeight1 * conceptWeight2);
		}
		r /= size;

		return 1 - r;
	}

	private double _KL(Map<Integer, Double> point1, Map<Integer, Double> point2) {
		double result = 0.0;
		double conceptWeight1 = 0.0;
		double conceptWeight2 = 0.0;
		for (Integer conceptNumber : point1.keySet()) {
			conceptWeight1 = point1.get(conceptNumber);
			conceptWeight2 = 0.0;
			if (point2.containsKey(conceptNumber))
				conceptWeight2 = point2.get(conceptNumber);

			double log1 = 0;
			if (conceptWeight1 != 0)
				log1 = Math.log(conceptWeight1);
			double log2 = 0;
			if (conceptWeight2 != 0)
				log2 = Math.log(conceptWeight2);
			result += conceptWeight1 * (log1 - log2);
		}

		return Math.abs(result);
	}

	private double _Jaccard(Map<Integer, Double> point1,
			Map<Integer, Double> point2) {
		double AB = 0f;
		double A2 = 0f;
		double B2 = 0f;
		double conceptWeight1;
		double conceptWeight2;
		for (Integer conceptNumber : point1.keySet()) {
			conceptWeight1 = point1.get(conceptNumber);
			if (point2.containsKey(conceptNumber))
				AB += conceptWeight1 * point2.get(conceptNumber);
			A2 += Math.pow(conceptWeight1, 2);
		}

		for (Integer conceptNumber : point2.keySet()) {
			conceptWeight2 = point2.get(conceptNumber);
			B2 += Math.pow(conceptWeight2, 2);
		}

		double f = AB / (A2 + B2 - AB);
		if (A2 + B2 - AB == 0)
			return Double.MAX_VALUE;
		return (1 - f);
	}

	public Map<Integer, Double> calculateRepresentative(Vector<Integer> points,
			Map<Integer, Document> documentMap) {

		Map<Integer, ArrayList<Double>> weights = new HashMap<Integer, ArrayList<Double>>();
		Map<Integer, Double> representative = new HashMap<Integer, Double>();

		for (int documentID : points) {
			Map<Integer, Double> rep = documentMap.get(documentID)
					.getRepresentative();
			for (int conceptID : rep.keySet()) {
				if (weights.get(conceptID) == null)
					weights.put(conceptID, new ArrayList<Double>());
				weights.get(conceptID).add(rep.get(conceptID));
			}
		}

		calculateRepresentative(representative, weights);
		return representative;
	}

	private void calculateRepresentative(Map<Integer, Double> representative,
			Map<Integer, ArrayList<Double>> weights) {
		for (int conceptID : weights.keySet()) {
			ArrayList<Double> w = weights.get(conceptID);
			double count = 0.0;
			for (double weight : w) {
				count += weight;
			}
			count /= w.size();
			representative.put(conceptID, count);
		}
	}

}
