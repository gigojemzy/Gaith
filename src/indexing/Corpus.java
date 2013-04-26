package indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import constants.ConstantHandler;

public class Corpus {

	private static Corpus corpus;
	private HashMap<String, Integer> corpusMapper;
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
	private IndexReader reader;
	private String [] docName;

	private Corpus() throws Exception {
		// TODO Auto-generated constructor stub
		reader = DirectoryReader.open(FSDirectory.open(new File(
				ConstantHandler.CORPUS_INDEX)));
		initCorpusMapper();
	}

	private void initCorpusMapper() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(
				ConstantHandler.CORPUS_MAPPER_INIT));
		corpusMapper = new HashMap<String, Integer>();
		docName = new String[ConstantHandler.CORPUS_SIZE];
		for (int i = 0; i < ConstantHandler.CORPUS_SIZE; i++) {
			String[] def = in.readLine().split(",");
			corpusMapper.put(def[2], Integer.parseInt(def[0]));
			docName[Integer.parseInt(def[0])] = def[2].split("/")[5];
		}
		in.close();
	}

	public static Corpus getCorpus() throws Exception {
		if (corpus == null)
			corpus = new Corpus();
		return corpus;
	}

	public Map<Integer, Double> query(String s) throws Exception {
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				ConstantHandler.INDEX_CORPUS_LIMIT, true);
		Query q = new QueryParser(Version.LUCENE_41, "contents", analyzer)
				.parse(s);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			ret.put(corpusMapper.get(d.get("path")), (double) hits[i].score);
		}
		return ret;
	}

	public String docName(int id) {
		return docName[id];
	}
	
}
