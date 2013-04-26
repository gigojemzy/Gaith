package constants;

import java.util.HashMap;

public class ConstantHandler {
	
	
	// Indexing Wiki
	public static final int INDEX_WIKI_LIMIT = 5;
	public static final int WIKIPEDIA_SIZE = 4489;
	public static final String WIKI_MAPPER_INIT = "WikiInit.txt";
	public static final String WIKI_INDEX = "./index";
	// Indexing Corpus
	public static final int INDEX_CORPUS_LIMIT = 25;
	public static final int CORPUS_SIZE = 134;
	public static final String CORPUS_MAPPER_INIT = "CorpusInit.txt";
	public static final String CORPUS_INDEX = "./corpusIndex";
	
	// Query Handler
	public static final HashMap<Integer, Double> NULL_HASH = new HashMap<Integer, Double>();
	
	// Logger
	public static enum LOG_TYPES {DEBUG,EVENT,WORNING,ERROR};
	public static boolean LOG_DEBUG = true;
	public static boolean LOG_EVENT = true;
	public static boolean LOG_WORNING = true;
	public static boolean LOG_ERROR = true;
	
	
	/*
	 * Index
	 */
	public static final int KMEANS = 1;
	public static final int DBSCAN = 2;
	public static final int MITOSIS = 3;
	public static final int SHC = 4;

	/*
	 * Constants
	 */
	public static final int NUMBEROFDOCUMENTS = 0;
	public static final int NUMBEROFCONCEPTS = 0;
	public static final int NUMBEROFCENTROIDS = 0;
	public static final int MAXITERATIONS = 0;
	public static final double EPSLION = 1.0;
	public static final int MINPOINTS = 5;
	public static final double F = 0.0;
	public static final double K = 0.0;
	public static final double ESA_MAX_SCORE = 1.0;
}
