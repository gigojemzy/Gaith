package clustering.mitosis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Phase3 {

	private Vector<Association> l2;
	private Map<Integer, MyCluster> clusterMap;
	private Map<Integer, MyCluster> finalClusterMap;
	private Map<Integer, Double> averageDistanceMap;
	private double K;

	public Phase3(double K, Vector<Association> l2,
			Map<Integer, MyCluster> clusterMap,
			Map<Integer, Double> averageDistanceMap) {
		this.K = K;
		this.l2 = l2;
		this.clusterMap = clusterMap;
		this.averageDistanceMap = averageDistanceMap;
		_Do();
	}

	public Map<Integer, MyCluster> getFinalClusterMap() {
		return this.finalClusterMap;
	}

	private void _Do() {
		refineClusters();
	}

	private double Get_Harmonic_Average(MyCluster c) {
		double sum = 0.0;
		double harm = 0.0;
		for (int i = 0; i < c.getClusterList().size(); i++)
			sum = sum + (1 / (c.getClusterList().get(i).getDis()));
		harm = c.getClusterList().size() / sum;
		return harm;
	}

	private int searchInFinalCluList(int pattern) {
		for (MyCluster myCluster : finalClusterMap.values())
			if (myCluster.getPatterns().contains(pattern))
				return myCluster.getId();
		return -1;
	}

	private int getFinalClusterId(int pattern) {
		int res = searchInFinalCluList(pattern);
		if (res == -1) {
			MyCluster c = new MyCluster();
			c.setId(pattern);
			Vector<Integer> a = new Vector<Integer>();
			a.add(pattern);
			c.setPatterns(a);
			c.setAveDis(averageDistanceMap.get(pattern));
			finalClusterMap.put(pattern, c);
			return pattern;
		}
		return res;
	}

	private void refineClusters() {
		finalClusterMap = new HashMap<Integer, MyCluster>();
		Association a;
		Vector<Association> currListOfAss;
		int p;
		int q;
		double d;
		double harm;
		l2.clear();

		// why sorting .. ?
		ArrayList<MyCluster> listOfClusters = new ArrayList<MyCluster>();
		for (MyCluster currCluster : clusterMap.values()) {
			listOfClusters.add(currCluster);
		}
		Collections.sort(listOfClusters, new Comparator<MyCluster>() {
			@Override
			public int compare(MyCluster c1, MyCluster c2) {
				if (c1.clusterList.size() == c2.clusterList.size())
					return 0;
				if (c1.clusterList.size() > c2.clusterList.size())
					return -1;
				return 1;
			}
		});

		for (MyCluster currCluster : listOfClusters) {
			harm = Get_Harmonic_Average(currCluster);
			currListOfAss = currCluster.getClusterList();
			for (int j = 0; j < currListOfAss.size(); j++) {
				a = currListOfAss.get(j);
				p = a.getP();
				q = a.getQ();
				d = a.getDis();
				MyCluster c1 = finalClusterMap.get(getFinalClusterId(p));
				MyCluster c2 = finalClusterMap.get(getFinalClusterId(q));

				if (d < K * harm) {
					if (c1.getId() != c2.getId()) {
						MyCluster c1Temp;
						MyCluster c2Temp;
						if (c1.getId() > c2.getId()) {
							c1Temp = c1;
							c2Temp = c2;
						} else {
							c1Temp = c2;
							c2Temp = c1;
						}
						Vector<Integer> c2Patterns = c2Temp.getPatterns();
						for (Integer c2Pattern : c2Patterns)
							if (!c1Temp.getPatterns().contains(c2Pattern))
								c1Temp.getPatterns().add(c2Pattern);
						finalClusterMap.remove(c2Temp.getId());
					}
					l2.add(a);
				}
			}
		}

		// Initialize
		for (int i = 0; i < finalClusterMap.size(); i++)
			finalClusterMap.get(i).setClusterList(new Vector<Association>());

		// set associations
		for (int i = 0; i < l2.size(); i++) {
			a = l2.get(i);
			p = a.getP();
			q = a.getQ();
			d = a.getDis();
			MyCluster c1 = finalClusterMap.get(getFinalClusterId(p));
			MyCluster c2 = finalClusterMap.get(getFinalClusterId(q));
			if (c1.getId() == c2.getId() && !c1.getClusterList().contains(a))
				c1.getClusterList().add(a);
		}
	}
}
