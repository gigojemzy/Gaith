package query;

import indexing.Wikipedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Logging.Logger;

import constants.ConstantHandler;
import constants.ConstantHandler.LOG_TYPES;

public class QueryHandler {
	Map<Integer, Double> representative;
	Set<String> terms;

	Map<Integer, ArrayList<Double>> weights;

	public QueryHandler(Set<String> terms) {
		representative = new HashMap<Integer, Double>();
		this.terms = terms;
		_Do();
	}

	public Map<Integer, Double> _Representative() {
		return representative;
	}

	private void _Do() {
		for (String term : terms) {
			Map<Integer, Double> rep = _Term_WeightedConcepts(term);
			addWeights(rep);
		}
		calculateRepresentative();
	}

	private void calculateRepresentative() {
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

	private void addWeights(Map<Integer, Double> rep) {
		for (int conceptID : rep.keySet()) {
			if (weights.get(conceptID) == null)
				weights.put(conceptID, new ArrayList<Double>());
			weights.get(conceptID).add(rep.get(conceptID));
		}
	}

	// return weightedConcepts for each term in a specific document
	private Map<Integer, Double> _Term_WeightedConcepts(String term){
		try {
			return Wikipedia.getWikipedia().query(term);
		} catch (Exception e) {
			Logger.getLogger().log("TEEEEEEEEEEEEEEEEET", LOG_TYPES.ERROR);
		}
		return ConstantHandler.NULL_HASH;
	}
}