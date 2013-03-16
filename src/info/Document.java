package info;

import java.util.HashMap;
import java.util.Map;

public class Document {

	private int documentID;
	private Map<Integer, Double> representative;
	private Map<String, Double> tfidf;

	public Document(int documentID) {
		this.documentID = documentID;
	}

	public int getDocumentID() {
		return documentID;
	}

	public Map<Integer, Double> getRepresentative() {
		return representative;
	}

	public void setRepresentative(Map<Integer, Double> representative) {
		this.representative = new HashMap<Integer, Double>(representative);
	}

	public Map<String, Double> getTFIDF() {
		return tfidf;
	}

	public void setTFIDF(Map<String, Double> tfidf) {
		this.tfidf = tfidf;
	}

}
