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

public class Wikipedia {

	private static Wikipedia wiki;
	private static HashMap<String, Integer> wikiMapper;
	private String [] conceptName;
	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
	private IndexReader reader;

	private Wikipedia() throws Exception {
		// TODO Auto-generated constructor stub
		reader = DirectoryReader.open(FSDirectory.open(new File(
				ConstantHandler.WIKI_INDEX)));
		initWikiMapper();
	}

	private void initWikiMapper() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(
				ConstantHandler.WIKI_MAPPER_INIT));
		wikiMapper = new HashMap<String, Integer>();
		conceptName = new String[ConstantHandler.WIKIPEDIA_SIZE];
		for (int i = 0; i < ConstantHandler.WIKIPEDIA_SIZE; i++) {
			String[] def = in.readLine().split(" ");
			wikiMapper.put(def[2], Integer.parseInt(def[0]));
			conceptName[Integer.parseInt(def[0])] = def[1];
		}
		in.close();
	}

	public static Wikipedia getWikipedia() throws Exception {
		if (wiki == null)
			wiki = new Wikipedia();
		return wiki;
	}

	public Map<Integer, Double> query(String s) throws Exception {
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				ConstantHandler.INDEX_WIKI_LIMIT, true);
		Query q = new QueryParser(Version.LUCENE_41, "contents", analyzer)
				.parse(s);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		Map<Integer, Double> ret = new HashMap<Integer, Double>();

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			ret.put(wikiMapper.get(d.get("path")), (double) hits[i].score);
		}

		return ret;

	}

	public String getConceptName(int id){
		return conceptName[id];
	}
	
	
}
