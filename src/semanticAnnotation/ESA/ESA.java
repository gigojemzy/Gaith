package semanticAnnotation.ESA;

import indexing.Wikipedia;
import info.Document;

import java.util.HashMap;
import java.util.Map;

import Logging.Logger;

import constants.ConstantHandler;
import constants.ConstantHandler.LOG_TYPES;

public class ESA {

	Map<Integer, Document> documentSet;

	public ESA(Map<Integer, Document> documentSet) {
		this.documentSet = documentSet;
	}

	public void _Documents_weightedConcepts() {
		for (Document document : documentSet.values()) {
			Map<String, Double> tfidf = document.getTFIDF();
			Map<Integer, Double> tempRepresentative = new HashMap<Integer, Double>();
			for (String term : tfidf.keySet()) {
				Map<Integer, Double> tWeightedConcepts = _Term_WeightedConcepts(term);
				_Document_WeightedConcepts(tempRepresentative, tfidf.get(term),
						tWeightedConcepts);
			}
			normalize(tempRepresentative);
			document.setRepresentative(tempRepresentative);
		}
	}

	private void normalize(Map<Integer, Double> tempRepresentative) {
		for (int conceptID : tempRepresentative.keySet()) {
			double weight = tempRepresentative.get(conceptID);
			weight = Math.min(1.0, weight / ConstantHandler.ESA_MAX_SCORE);
			tempRepresentative.put(conceptID, weight);
		}
	}

	// Update the weightedConcepts array
	private void _Document_WeightedConcepts(
			Map<Integer, Double> tempRepresentative, double termWeight,
			Map<Integer, Double> tWeightedConcepts) {
		for (int conceptID : tWeightedConcepts.keySet()) {
			if (tempRepresentative.get(conceptID) == null) {
				tempRepresentative.put(conceptID, 0.0);
			}
			tempRepresentative.put(conceptID, tempRepresentative.get(conceptID)
					+ (termWeight * tWeightedConcepts.get(conceptID)));
		}
	}

	// return weightedConcepts for each term in a specific document
	private Map<Integer, Double> _Term_WeightedConcepts(String term) {
		try {
			return Wikipedia.getWikipedia().query(term);
		} catch (Exception e) {
			Logger.getLogger().log("Error", LOG_TYPES.ERROR);
		}
		return ConstantHandler.NULL_HASH;
	}
}