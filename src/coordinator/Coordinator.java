package coordinator;

import indexing.Corpus;
import indexing.Wikipedia;
import info.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import constants.ConstantHandler;



import semanticAnnotation.ESA.ESA;

public class Coordinator {

	public static void main(String[] args) throws Exception {
		HashMap<Integer, Document> docmentSet = new HashMap<Integer, Document>();
		BufferedReader in = new BufferedReader(new FileReader("termsInit.txt"));
		try {
			while(true){
				StringTokenizer line = new StringTokenizer( in.readLine());
				int id = Integer.parseInt(line.nextToken());
				String term = line.nextToken();
				double tfidf = Double.parseDouble(line.nextToken());
				if(!docmentSet.containsKey(id))docmentSet.put(id, new Document(id));
				docmentSet.get(id).setTermTfidf(term, tfidf);
			}
		} catch (Exception e) {
		}
		ESA esa = new ESA(docmentSet);
		esa._Documents_weightedConcepts();
		HashMap<Integer, TreeSet<Integer>> inindex = new HashMap<Integer, TreeSet<Integer>>();
		for (int docId : docmentSet.keySet()) {
			for (int concpetId : docmentSet.get(docId).getRepresentative().keySet()) {
				if(!inindex.containsKey(concpetId))inindex.put(concpetId, new TreeSet<Integer>());
				inindex.get(concpetId).add(docId);
			}
		}
		Wikipedia w = Wikipedia.getWikipedia();
		
		
		String s = new JOptionPane().showInputDialog("search ya bn 3amy");
		
		HashMap<Integer, Double> qc = (HashMap<Integer, Double>) w.query(s);
		
		Hit[] h = new Hit[ConstantHandler.CORPUS_SIZE];
		for (int i = 0; i < h.length; i++) {
			h[i]=new Hit(i);
		}
		
		
		for (int  concept : qc.keySet()) {
			double weightCQ = qc.get(concept);
//			System.out.println(concept +" "+w.getConceptName(concept)+" "+inindex.get(concept).toString());
			for (int docId : inindex.get(concept)) {
				double weightCD = docmentSet.get(docId).getRepresentative().get(concept);
				h[docId].updateRank(weightCD*weightCQ);
			}
		
		}
		
		Corpus c = Corpus.getCorpus();
		
		HashMap<Integer, Double> qd = (HashMap<Integer, Double>) c.query(s);
		
		for (int  docId : qd.keySet()) {
			h[docId].updateRank(qd.get(docId).doubleValue());
		}
		
		Arrays.sort(h);
		
		System.out.println(Arrays.toString(h));
		
	}

}

class Hit implements Comparable<Hit>{
	
	int docId;
	double rank;
	Corpus c;
	public Hit(int docId) throws Exception {
		super();
		this.docId = docId;
		this.rank = 0.0;
		c = Corpus.getCorpus();
	}
	@Override
	public int compareTo(Hit o) {
		if(Double.compare(rank, o.rank)==0)
			return docId-o.docId;
		return -1*Double.compare(rank, o.rank);
	}
	
	public void updateRank(double r){
		rank+=r;
	}
	
	public String toString() {
		return docId+" "+c.docName(docId)+" => "+rank+"\n";
	}
}
